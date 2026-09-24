# Movie Ticket Booking System API - Complete Usage Guide

This guide describes the current 17-endpoint API in a practical setup and usage order. Replace UUIDs and timestamps with values from your environment.

## Prerequisites

1. The Spring Boot application is running at `http://localhost:8080`.
2. MySQL is configured and running.
3. A strong `JWT_SECRET` is configured.
4. JWT tokens are required for protected endpoints.

## Common request headers

For protected endpoints, send the token returned by `POST /login`:

```http
Authorization: Bearer <your-jwt-token>
```

Request bodies should use:

```http
Content-Type: application/json
```

## Step-by-step usage

### Step 1: Register a user

**Endpoint**: `POST /register`
**Authentication**: None

```json
{
  "username": "john_doe123",
  "email": "john.doe@gmail.com",
  "phoneNumber": "9876543210",
  "password": "Password123!",
  "dateOfBirth": "1990-05-15"
}
```

Public registration always creates `ROLE_USER`. Registration validation requires a Gmail address, a phone number beginning with `7`, `8`, or `9`, an 8–12 character password containing upper/lowercase letters, a digit, and a special character, and a past date of birth.

Example response:

```json
{
  "status": 201,
  "message": "UserDetail Created",
  "data": {
    "userId": "uuid-string",
    "username": "john_doe123",
    "email": "john.doe@gmail.com",
    "userRole": "ROLE_USER"
  }
}
```

Privileged roles are not assigned by this public request. There is no public role-promotion endpoint.

### Step 2: Log in

**Endpoint**: `POST /login`
**Authentication**: None

```json
{
  "email": "john.doe@gmail.com",
  "password": "Password123!"
}
```

Example response:

```json
{
  "status": 200,
  "message": "Login successful",
  "data": "eyJhbGciOiJIUzUxMiJ9..."
}
```

Save the token in `data` for subsequent requests.

### Step 3: Update a user profile

**Endpoint**: `PUT /update?email=john.doe@gmail.com`
**Authentication**: JWT for the target user or `ROLE_ADMIN`

```json
{
  "username": "john_doe_updated",
  "phoneNumber": "9876543210",
  "dateOfBirth": "1990-05-15"
}
```

A user attempting to update another user's profile without `ROLE_ADMIN` receives `403 Forbidden`.

### Step 4: Soft delete a user account

**Endpoint**: `DELETE /delete?email=john.doe@gmail.com`
**Authentication**: JWT for the target user or `ROLE_ADMIN`

Example response:

```json
{
  "status": 200,
  "message": "User account deleted successfully (soft delete).",
  "data": null
}
```

### Step 5: Register a theater

**Endpoint**: `POST /theater/register`
**Authentication**: JWT with `ROLE_THEATER_OWNER`

```json
{
  "name": "PVR Cinemas",
  "address": "123 Main Street",
  "city": "Bangalore",
  "landmark": "Near Central Mall"
}
```

The theater is created for the authenticated owner.

### Step 6: Get theater details

**Endpoint**: `GET /theater/{id}`
**Authentication**: JWT token

The response contains the theater identified by `{id}`.

### Step 7: Update a theater

**Endpoint**: `PUT /theater/{id}`
**Authentication**: JWT with `ROLE_THEATER_OWNER`

```json
{
  "name": "PVR Cinemas - Updated",
  "address": "456 New Street",
  "city": "Bangalore",
  "landmark": "Near Airport"
}
```

The theater must belong to the authenticated owner.

### Step 8: Add a screen

**Endpoint**: `POST /screen?theaterId={theaterId}`
**Authentication**: JWT with `ROLE_THEATER_OWNER`

```json
{
  "screenType": "THREE_D",
  "capacity": 150,
  "noOfRows": 10
}
```

The theater must belong to the authenticated owner. `screenType` can be `TWO_D`, `THREE_D`, or `IMAX`. Capacity must be `1`–`1000`, rows must be `1`–`26`, and capacity must be evenly divisible by the number of rows. The service generates the seat layout.

### Step 9: Get screen details

**Endpoint**: `GET /screen/{screenId}`
**Authentication**: JWT token

The response includes screen details and its generated seats.

### Step 10: Create a movie

**Endpoint**: `POST /movies`
**Authentication**: JWT with `ROLE_ADMIN`

```json
{
  "title": "Example Movie",
  "description": "A movie description.",
  "runtime": "PT2H",
  "certificate": "UA",
  "genre": "DRAMA",
  "castList": [
    "Actor One",
    "Actor Two"
  ]
}
```

Allowed certificates are `U`, `UA`, `A`, and `S`. Allowed genres are `ACTION`, `ANIMATION`, `COMEDY`, `DRAMA`, `HORROR`, `ROMANCE`, `SCIENCE_FICTION`, and `THRILLER`. Runtime must be a positive ISO-8601 duration of no more than 24 hours.

### Step 11: Update a movie title

**Endpoint**: `PUT /movies/{movieId}`
**Authentication**: JWT with `ROLE_ADMIN`

```json
{
  "title": "Updated movie title"
}
```

### Step 12: Update a movie description

**Endpoint**: `PUT /movies/{movieId}/description`
**Authentication**: JWT with `ROLE_ADMIN`

```json
{
  "description": "Updated movie description."
}
```

### Step 13: Update a movie cast

**Endpoint**: `PUT /movies/{movieId}/cast`
**Authentication**: JWT with `ROLE_ADMIN`

```json
{
  "castList": [
    "Updated Actor"
  ]
}
```

### Step 14: Get movie details

**Endpoint**: `GET /movies/{movieId}`
**Authentication**: JWT token

The response includes the movie details and its average feedback rating.

### Step 15: Add a show to a screen

**Endpoint**: `POST /theaters/{theaterId}/screens/{screenId}/shows`
**Authentication**: JWT with `ROLE_THEATER_OWNER`

```json
{
  "startTimeEpochMillis": 1735094400000,
  "movieId": "movie-uuid-string"
}
```

The movie must already exist. The start time must not be in the past. The end time is calculated from the movie runtime. The theater and screen must belong to the authenticated owner. A show that overlaps an existing show on the same screen returns `409 Conflict`.

### Step 16: Create feedback for a movie

**Endpoint**: `POST /movies/{movieId}/feedback`
**Authentication**: JWT with an active `ROLE_USER` account

```json
{
  "rating": 5,
  "review": "Excellent movie! Highly recommended."
}
```

The rating must be from `1` to `5`. The review must be nonblank and no longer than 500 characters. A user may submit only one feedback entry per movie. Theater-owner and administrator accounts cannot use this endpoint.

### Step 17: Get feedback for a movie

**Endpoint**: `GET /movies/{movieId}/feedback`
**Authentication**: JWT token

The response contains all feedback entries for `{movieId}`.

## Error response format

Application errors use this structure:

```json
{
  "statusCode": 400,
  "error_message": "Validation failed for one or more fields",
  "timestamp": "2026-09-24T12:00:00",
  "path": null,
  "data": [
    {
      "field": "email",
      "rejectedValue": "invalid-email",
      "errorMessage": "Enter a valid Gmail ID"
    }
  ]
}
```

The `path` property is currently `null` because the application does not populate it. Clients should use the HTTP status code and `error_message` for application errors.

## Common status codes

- `200` — successful read, update, login, or delete.
- `201` — resource created.
- `400` — validation error or bad request.
- `401` — missing or invalid JWT, or failed authentication.
- `403` — insufficient role or resource ownership.
- `404` — resource or route not found.
- `405` — HTTP method not supported.
- `409` — duplicate resource, invalid state, or overlapping show.
- `500` — internal server error.

## Workflow summary

1. **Register and log in:** Public registration creates `ROLE_USER`; privileged accounts are provisioned out of band.
2. **Movie catalog:** An `ROLE_ADMIN` creates a movie before it can be scheduled.
3. **Theater management:** An owner registers a theater, adds a screen, and schedules a show for an owned screen.
4. **Lookup:** Use the ID-based movie, theater, screen, and feedback retrieval endpoints. There are currently no listing endpoints.
5. **Feedback:** An active normal user submits and retrieves feedback for a movie.
