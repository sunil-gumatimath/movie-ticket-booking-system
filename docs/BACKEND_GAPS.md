# Backend Gaps & API Enhancement Proposal
**Project:** CineBook — Movie Ticket Booking System  
**Document Version:** 1.0.0  
**Status:** Proposal (For Architecture Review)  
**Author:** Frontend Engineering Team  

---

## Executive Summary

During the implementation of the CineBook modern frontend (`movie-ticket-booking-frontend`), the frontend was wired to the 17 existing endpoints provided by `movie-ticket-booking-system-api`. 

To maintain production stability without inventing fake data, the client currently relies on an honest fallback strategy:
1. Local persistence of entity IDs (`src/store/recent.store.ts`) for items created or opened by the user.
2. Explicit "Open by ID" lookups and Command Palette (`Cmd+K`) navigation.
3. Clean, honest empty states explaining missing catalog capabilities to users.
4. Code locations dependent on future backend enhancements are marked with `// BACKEND-GAP: <endpoint>`.

This document outlines the **8 architectural and contract gaps**, ranked in descending order by the amount of consumer UI experience they unlock. Every proposal conforms to the existing backend envelope:

```json
{
  "status": 200,
  "message": "Operation description",
  "data": { ... }
}
```

---

## Ranked Proposal Matrix

| Rank | Proposal | Method & Path | Access Role | Nature | Frontend Views Unblocked |
|---|---|---|---|---|---|
| **1** | **Movie Catalog & Search** | `GET /movies` | Any authenticated (or Public) | Additive | `/`, `/movies`, Global Command Palette |
| **2** | **Showtimes & Theater Discovery** | `GET /movies/{id}/shows`<br>`GET /theaters` | Any authenticated | Additive | `/movies/:id`, `/theaters/:id`, Showtime grid |
| **3** | **Owner Theaters & Screens Hierarchy** | `GET /theaters/mine`<br>`GET /theaters/{id}` (nested screens) | `ROLE_THEATER_OWNER` | Additive / Non-breaking update | `/owner`, `/owner/theaters/:id` |
| **4** | **Authenticated User Profile** | `GET /users/me` | Any authenticated | Additive | `/profile`, Global Header user badge |
| **5** | **Seat Status & Ticket Booking** | `GET /shows/{id}/seats`<br>`POST /shows/{id}/bookings`<br>`GET /bookings/me`<br>`DELETE /bookings/{id}` | Any / `ROLE_USER` | Additive | `/screens/:id`, Booking checkout modal, `/profile` booking history |
| **6** | **Movie Poster & Backdrop Image URL** | Add `posterUrl`, `backdropUrl` to `MovieResponse` | `ADMIN` (write)<br>Any (read) | Non-breaking field addition | Hero banner, `/movies`, Movie cards |
| **7** | **Username in JWT Claims** | Add `username` claim to JWT payload | Public (`/login`) | Non-breaking token claim | Token storage, session re-hydration |
| **8** | **Feedback Pagination & Reviewer Identity** | `GET /movies/{id}/feedback` envelope & DTO update | Any authenticated | Non-breaking field addition | `/movies/:id` reviews pagination |

---

## Detailed Gap Specifications

---

### Gap 1: Movie Catalog, Filtering, and Search

#### Problem Statement
Currently, only `GET /movies/{movieId}` exists. There is no endpoint to list or search movies. The home page (`/`) and movies catalog (`/movies`) cannot display an active movie directory without the user having pre-existing knowledge of internal integer IDs.

#### Proposed Endpoint
- **Method & Path:** `GET /movies?page=0&size=12&genre=&certificate=&search=&sortBy=ratings&direction=desc`
- **Required Role:** Any authenticated (or public if unauthenticated browsing is enabled in Spring Security)
- **Request Parameters:**
  - `page` *(optional, default: 0)*: Zero-based page index.
  - `size` *(optional, default: 12)*: Page size (1–50).
  - `genre` *(optional)*: Filter by `Genre` enum (`ACTION`, `COMEDY`, `DRAMA`, `HORROR`, `ROMANCE`, `SCIENCE_FICTION`, `THRILLER`, `ANIMATION`).
  - `certificate` *(optional)*: Filter by `Certificate` enum (`U`, `UA`, `A`, `S`).
  - `search` *(optional)*: Case-insensitive substring match against movie title or cast.
  - `sortBy` *(optional, default: "title")*: `title`, `ratings`, `runtime`.
  - `direction` *(optional, default: "asc")*: `asc` or `desc`.

#### Response Body Schema (`ResponseStructure<PageResponse<MovieResponse>>`)
```json
{
  "status": 200,
  "message": "Movies retrieved successfully",
  "data": {
    "content": [
      {
        "movieId": 101,
        "title": "Interstellar",
        "description": "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.",
        "ratings": 4.8,
        "runtime": "PT2H49M",
        "certificate": "UA",
        "genre": "SCIENCE_FICTION",
        "castList": ["Matthew McConaughey", "Anne Hathaway", "Jessica Chastain"]
      }
    ],
    "pageNumber": 0,
    "pageSize": 12,
    "totalElements": 48,
    "totalPages": 4,
    "isLast": false
  }
}
```

#### Impact & Feasibility
- **Views Unblocked:** `/`, `/movies`, `/admin` (catalog overview table).
- **Breaking Status:** **Additive**. Does not modify `GET /movies/{movieId}`.
- **Backend Implementation:** Can be implemented in `MovieController` using Spring Data `JpaSpecificationExecutor<Movie>` or standard `Page<Movie> findAll(Specification<Movie> spec, Pageable pageable)`.

---

### Gap 2: Movie Showtimes & City-Based Theater Discovery

#### Problem Statement
`POST /theaters/{theaterId}/screens/{screenId}/shows` allows theater owners to schedule shows, but there is no customer-facing read endpoint to find when and where a movie is showing. Users can open a movie detail page, but cannot see which theaters are screening it or select a time slot.

#### Proposed Endpoints
1. **List Shows for a Movie:**
   - **Method & Path:** `GET /movies/{movieId}/shows?city=&date=`
   - **Required Role:** Any authenticated user
   - **Query Parameters:**
     - `city` *(optional)*: Filter by theater city.
     - `date` *(optional)*: Filter by ISO LocalDate (`YYYY-MM-DD`).
   - **Response Body Schema (`ResponseStructure<List<MovieShowScheduleResponse>>`):**
   ```json
   {
     "status": 200,
     "message": "Shows retrieved successfully",
     "data": [
       {
         "theaterId": 12,
         "theaterName": "PVR Cinemas Grand",
         "city": "Bengaluru",
         "address": "Forum Mall, Koramangala",
         "screens": [
           {
             "screenId": 4,
             "screenType": "IMAX",
             "shows": [
               {
                 "showId": 801,
                 "startTimeEpochMillis": 1773648000000,
                 "endTimeEpochMillis": 1773658200000
               }
             ]
           }
         ]
       }
     ]
   }
   ```

2. **Discover Theaters by City:**
   - **Method & Path:** `GET /theaters?city=&search=&page=0&size=20`
   - **Required Role:** Any authenticated user
   - **Response Body Schema (`ResponseStructure<PageResponse<TheaterResponse>>`):**
   ```json
   {
     "status": 200,
     "message": "Theaters retrieved successfully",
     "data": {
       "content": [
         {
           "theaterId": 12,
           "name": "PVR Cinemas Grand",
           "address": "Forum Mall, Koramangala",
           "city": "Bengaluru",
           "landmark": "Near Checkpost"
         }
       ],
       "pageNumber": 0,
       "pageSize": 20,
       "totalElements": 1,
       "totalPages": 1,
       "isLast": true
     }
   }
   ```

#### Impact & Feasibility
- **Views Unblocked:** `/movies/:movieId` (showtime selection & booking entrypoint), `/theaters` (theater catalog).
- **Breaking Status:** **Additive**.
- **Backend Implementation:** Query `Show` entity joined with `Screen`, `Theater`, and `Movie` where `startTimeEpochMillis >= current_time`.

---

### Gap 3: Owner-Scoped Theaters & Nested Screens Hierarchy

#### Problem Statement
`GET /theater/{id}` returns only `{ theaterId, name, address, city, landmark }`. It contains neither the theater's owner ID nor the list of screens belonging to that theater. Furthermore, there is no endpoint for an authenticated `ROLE_THEATER_OWNER` to list the theaters they own (`GET /theater/mine`). Consequently, theater owners must record and manage numeric IDs manually.

#### Proposed Endpoints
1. **Get Authenticated Owner's Theaters:**
   - **Method & Path:** `GET /theater/mine`
   - **Required Role:** `ROLE_THEATER_OWNER`
   - **Response Body Schema (`ResponseStructure<List<TheaterDetailResponse>>`):**
   ```json
   {
     "status": 200,
     "message": "Owner theaters retrieved",
     "data": [
       {
         "theaterId": 12,
         "name": "Cinepolis VIP",
         "address": "Bannerghatta Main Road",
         "city": "Bengaluru",
         "landmark": "Royal Meenakshi Mall",
         "screens": [
           {
             "screenId": 4,
             "screenType": "IMAX",
             "capacity": 200,
             "noOfRows": 10
           }
         ]
       }
     ]
   }
   ```

2. **Include Screens in `GET /theater/{id}`:**
   - Either augment `TheaterResponse` with `screens: List<ScreenSummaryResponse>` (empty list if none), or provide `GET /theater/{id}/screens`.

#### Impact & Feasibility
- **Views Unblocked:** `/owner` dashboard, `/owner/theaters/:theaterId`.
- **Breaking Status:** **Non-breaking**. Adding optional/nested fields to JSON responses does not break existing consumers.
- **Backend Implementation:** Filter `theaterRepository.findByUser(authenticatedUser)`.

---

### Gap 4: Authenticated User Profile Endpoint (`GET /users/me`)

#### Problem Statement
The application provides `PUT /users/{userId}` and `DELETE /users/{userId}`, but there is no `GET /users/{userId}` or `GET /users/me`.
When a user navigates to `/profile`, the form cannot prefill their current `username`, `phoneNumber`, or `dateOfBirth`. Because `PUT /users/{userId}` requires all three fields (`@NotNull`, `@NotBlank`), users are forced to re-type their phone number and birth date from memory, creating unnecessary friction and validation errors.

#### Proposed Endpoint
- **Method & Path:** `GET /users/me`
- **Required Role:** Any authenticated user (`ROLE_USER`, `ROLE_THEATER_OWNER`, `ROLE_ADMIN`)
- **Response Body Schema (`ResponseStructure<UserResponse>`):**
```json
{
  "status": 200,
  "message": "User profile retrieved successfully",
  "data": {
    "userId": 5,
    "username": "sarah_connor",
    "email": "sarah.connor@gmail.com",
    "phoneNumber": "9876543210",
    "dateOfBirth": "1995-05-12",
    "userRole": "ROLE_USER"
  }
}
```

#### Impact & Feasibility
- **Views Unblocked:** `/profile` edit form prefill, user avatar in top navigation bar.
- **Breaking Status:** **Additive**.
- **Backend Implementation:** Extract email from `SecurityContextHolder.getContext().getAuthentication().getName()`, find user entity in repository, return DTO.

---

### Gap 5: Seat Availability, Reservations, and Ticket Booking Flow

#### Problem Statement
While named "Movie Ticket Booking System", the backend currently stops at screen layouts and show creation. There are no entities or endpoints for:
1. Checking which seats are booked or held for a specific show.
2. Creating a booking/reservation for a set of seats.
3. Viewing booking history or issuing tickets.
4. Cancelling an existing reservation.

#### Proposed Endpoints
1. **Get Seat Layout with Live Availability for a Show:**
   - **Method & Path:** `GET /shows/{showId}/seats`
   - **Required Role:** Any authenticated user
   - **Response Body Schema (`ResponseStructure<ShowSeatMapResponse>`):**
   ```json
   {
     "status": 200,
     "message": "Show seats retrieved",
     "data": {
       "showId": 801,
       "screenId": 4,
       "screenType": "IMAX",
       "capacity": 200,
       "noOfRows": 10,
       "seats": [
         {
           "seatId": 401,
           "seatName": "A1",
           "status": "AVAILABLE",
           "price": 350.00
         },
         {
           "seatId": 402,
           "seatName": "A2",
           "status": "BOOKED",
           "price": 350.00
         }
       ]
     }
   }
   ```
   *(Statuses: `AVAILABLE`, `SELECTED`, `BOOKED`, `BLOCKED`)*

2. **Create Ticket Booking:**
   - **Method & Path:** `POST /shows/{showId}/bookings`
   - **Required Role:** `ROLE_USER`
   - **Request Body Schema (`BookingRequest`):**
   ```json
   {
     "seatIds": [401, 403]
   }
   ```
   - **Response Body Schema (`ResponseStructure<BookingResponse>`):**
   ```json
   {
     "status": 201,
     "message": "Tickets booked successfully",
     "data": {
       "bookingId": "BK-902184",
       "showId": 801,
       "movieTitle": "Interstellar",
       "theaterName": "PVR Cinemas Grand",
       "screenType": "IMAX",
       "startTimeEpochMillis": 1773648000000,
       "seatNames": ["A1", "A3"],
       "totalAmount": 700.00,
       "bookingStatus": "CONFIRMED",
       "bookingTime": "2026-10-15T12:00:00Z"
     }
   }
   ```

3. **User Booking History:**
   - **Method & Path:** `GET /bookings/me?page=0&size=10`
   - **Required Role:** `ROLE_USER`

4. **Cancel Booking:**
   - **Method & Path:** `DELETE /bookings/{bookingId}`
   - **Required Role:** `ROLE_USER` (self) or `ROLE_ADMIN`

#### Impact & Feasibility
- **Views Unblocked:** `/screens/:screenId` interactive seat selection & checkout dialog, `/profile` "My Bookings" tab, printable ticket passes.
- **Breaking Status:** **Additive**.

---

### Gap 6: Poster Image & Backdrop URLs on Movie Entity

#### Problem Statement
`MovieResponse` contains only textual metadata (`movieId`, `title`, `description`, `ratings`, `runtime`, `certificate`, `genre`, `castList`). Without image assets, the frontend must generate dynamic procedural HTML5 canvas gradient posters.

#### Proposed Contract Update
- Add optional `posterUrl` and `backdropUrl` to `MovieRequest` and `MovieResponse`:
```java
// Inside MovieRequest:
@Pattern(regexp = "^(https?://.*\\.(?:png|jpg|jpeg|webp))?$", message = "Invalid image URL")
private String posterUrl;

@Pattern(regexp = "^(https?://.*\\.(?:png|jpg|jpeg|webp))?$", message = "Invalid image URL")
private String backdropUrl;

// Inside MovieResponse:
private String posterUrl;
private String backdropUrl;
```

#### Impact & Feasibility
- **Views Unblocked:** Hero spotlight banners, poster card grids, immersive movie backdrop blur.
- **Breaking Status:** **Non-breaking**. Existing consumers ignore extra fields in JSON response.

---

### Gap 7: Include `username` in JWT Token Claims

#### Problem Statement
Currently, the JWT payload created upon `POST /login` contains only:
- `sub`: user email
- `userId`: numeric ID
- `role`: string (e.g. `ROLE_USER`)
- `iat`, `exp`

Because `username` is absent, the client must store the username in local storage when receiving `UserResponse` at registration or profile update. If a user logs in on a new device or browser, the UI must fall back to parsing the email local-part (`email.split('@')[0]`) as the display name.

#### Proposed Solution
Include `username` in `JwtService.generateToken()` claims:
```java
claims.put("username", user.getUsername());
```

#### Impact & Feasibility
- **Views Unblocked:** Immediate, accurate display of user handle across all sessions and devices.
- **Breaking Status:** **Non-breaking**. Standard JWT decoders gracefully handle additional custom claims.

---

### Gap 8: Feedback Pagination Metadata and Reviewer Attribution

#### Problem Statement
`GET /movies/{movieId}/feedback?page=0&size=20` returns a plain JSON array of `FeedbackResponse` rather than a paginated envelope (`PageResponse`). The client cannot know `totalElements` or `totalPages`. Furthermore, `FeedbackResponse` contains only `{ feedbackId, rating, review, createdAt, movieId, movieTitle }` with no author display name or user identifier, forcing the UI to display all reviews anonymously as "Verified CineBook Viewer".

#### Proposed Update
1. Wrap feedback in standard pagination structure:
```json
{
  "status": 200,
  "message": "Feedback list retrieved",
  "data": {
    "content": [
      {
        "feedbackId": 12,
        "rating": 5,
        "review": "Exceptional visual storytelling and immersive sound design.",
        "authorName": "sarah_connor",
        "createdAt": "2026-10-15T09:30:00Z",
        "movieId": 101,
        "movieTitle": "Interstellar"
      }
    ],
    "pageNumber": 0,
    "pageSize": 20,
    "totalElements": 1,
    "totalPages": 1,
    "isLast": true
  }
}
```

#### Impact & Feasibility
- **Views Unblocked:** Accurate review count, numbered pagination controls, reviewer attribution.
- **Breaking Status:** Wrapping the plain array in a pagination envelope is a **minor breaking change** for clients expecting an array. However, `FeedbackResponse.authorName` is additive.

---

## Code Reference Mapping

The following table documents where the frontend handles these gaps with honest fallbacks and code markers:

| Source File | Marker Comment | Fallback Implementation |
|---|---|---|
| `src/routes/MoviesPage.tsx` | `// BACKEND-GAP: GET /movies?page=&size=&genre=` | Renders locally tracked movies from `recent.store.ts` with explicit ID search and empty state |
| `src/routes/MovieDetailPage.tsx` | `// BACKEND-GAP: GET /movies/{movieId}/shows` | Explains that showtime scheduling requires owner assignment; displays movie details and feedback |
| `src/routes/TheaterDetailPage.tsx` | `// BACKEND-GAP: screens on GET /theater/{id}` | Allows looking up screens by ID, renders recent screens cached for that theater |
| `src/routes/ProfilePage.tsx` | `// BACKEND-GAP: GET /users/me` | Provides explicit inputs for all 3 required fields (`username`, `phoneNumber`, `dateOfBirth`) with helper hints |
| `src/routes/OwnerDashboardPage.tsx` | `// BACKEND-GAP: GET /theater/mine` | Tracks registered theaters in `recent.store.ts` with quick-open cards |
| `src/routes/OwnerTheaterManagePage.tsx`| `// BACKEND-GAP: GET /theaters/{theaterId}/screens` | Tracks added screens in `recent.store.ts` for that specific theater |
| `src/routes/ScreenDetailPage.tsx` | `// BACKEND-GAP: GET /shows/{showId}/seats` | Renders the exact physical seat layout from `ScreenDetailResponse.seatResponses` with keyboard navigation |
| `src/api/token.storage.ts` | `// BACKEND-GAP: username claim in JWT` | Caches username from register/profile responses, falling back to email prefix |

---

## Conclusion & Recommendation

The CineBook frontend is fully architected to consume these endpoints the moment they become available on the backend. The API layer (`src/api/*`) and TanStack Query hooks are designed to switch from the ID-based fallback to full server queries with zero UI refactoring required.
