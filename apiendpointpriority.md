# API Endpoints by Priority

The current controller layer exposes 17 endpoints. The order below is a typical setup and usage order: create a movie before scheduling a show, because show creation requires an existing movie.

## Priority 1
**POST** `/register` - Register a new user account
- Authentication: None
- Public registration always creates `ROLE_USER`.
- Validation requires a Gmail address, a valid phone number, a strong password, and a past date of birth.

## Priority 2
**POST** `/login` - Log in and get a JWT token
- Authentication: None
- Email is normalized before authentication.

## Priority 3
**PUT** `/update?email={email}` - Update a user profile
- Authentication: JWT token for the target user or `ROLE_ADMIN`
- Users may modify only their own account unless they are administrators.

## Priority 4
**DELETE** `/delete?email={email}` - Soft delete a user account
- Authentication: JWT token for the target user or `ROLE_ADMIN`
- Users may delete only their own account unless they are administrators.

## Priority 5
**POST** `/theater/register` - Register a theater
- Authentication: JWT with `ROLE_THEATER_OWNER`
- The theater is created for the authenticated owner.

## Priority 6
**GET** `/theater/{id}` - Get theater details
- Authentication: JWT token
- Returns theater details by ID.

## Priority 7
**PUT** `/theater/{id}` - Update a theater
- Authentication: JWT with `ROLE_THEATER_OWNER`
- The theater must belong to the authenticated owner.

## Priority 8
**POST** `/screen?theaterId={theaterId}` - Add a screen to a theater
- Authentication: JWT with `ROLE_THEATER_OWNER`
- The theater must belong to the authenticated owner.
- Capacity must be evenly divisible by the number of rows; a seat layout is generated.

## Priority 9
**GET** `/screen/{screenId}` - Get screen details
- Authentication: JWT token
- Returns screen details and the generated seat layout.

## Priority 10
**POST** `/movies` - Create a movie
- Authentication: JWT with `ROLE_ADMIN`
- Required fields: `title`, `description`, `runtime`, `certificate`, `genre`, and non-empty `castList`.
- Runtime must be positive and no longer than 24 hours.

## Priority 11
**PUT** `/movies/{movieId}` - Update movie title
- Authentication: JWT with `ROLE_ADMIN`
- Request body contains the new `title`.

## Priority 12
**PUT** `/movies/{movieId}/description` - Update movie description
- Authentication: JWT with `ROLE_ADMIN`
- Request body contains the new `description`.

## Priority 13
**PUT** `/movies/{movieId}/cast` - Update movie cast
- Authentication: JWT with `ROLE_ADMIN`
- Request body contains a non-empty `castList`.

## Priority 14
**GET** `/movies/{movieId}` - Get movie details
- Authentication: JWT token
- Returns movie details and the average feedback rating.

## Priority 15
**POST** `/theaters/{theaterId}/screens/{screenId}/shows` - Add a show to a screen
- Authentication: JWT with `ROLE_THEATER_OWNER`
- The theater and screen must belong to the authenticated owner.
- The movie must exist, the start time must not be in the past, and overlapping shows return `409 Conflict`.

## Priority 16
**POST** `/movies/{movieId}/feedback` - Create feedback
- Authentication: JWT with an active `ROLE_USER` account
- A user may submit only one feedback entry per movie.

## Priority 17
**GET** `/movies/{movieId}/feedback` - Get feedback for a movie
- Authentication: JWT token
- Returns all feedback entries for the movie.
