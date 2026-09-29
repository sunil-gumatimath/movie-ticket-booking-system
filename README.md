# Movie Ticket Booking System API

A Spring Boot REST API for managing users, theaters, screens, movies, shows, and movie feedback. The API uses JWT authentication and role-based authorization.

## Features

### User and authentication management

- Public registration always creates `ROLE_USER`; privileged roles are provisioned through a trusted out-of-band process.
- JWT login and stateless authentication.
- User profile updates and soft deletion.
- Users may modify only their own account; `ROLE_ADMIN` may modify any user account.

### Theater and show management

- Theater registration and lookup.
- Theater updates, screen creation, and show scheduling for the authenticated theater owner.
- Screen lookup includes the generated seat layout.
- Show overlap detection and scheduling conflict handling.

### Movie and feedback management

- Admin-only movie creation and field-specific updates.
- Movie lookup by movie ID.
- Active normal users can submit one feedback per movie.
- Feedback retrieval for a movie.

The current controller layer exposes 17 endpoints. Ticket booking and seat-reservation endpoints are not currently exposed by the controllers, and there are no movie, theater, show, or seat listing endpoints.

## Tech Stack

- **Framework:** Spring Boot 3.4.4
- **Language:** Java 17+
- **Database:** MySQL 8.0+
- **Security:** Spring Security with JSON Web Tokens (JWT)
- **ORM:** Spring Data JPA / Hibernate
- **Validation:** Jakarta Bean Validation
- **API documentation:** springdoc-openapi / Swagger UI
- **Build tool:** Maven

## Prerequisites

Install the following before running the application:

- Java 17 or newer
- Maven 3.6+
- MySQL 8.0+
- Git

## Setup and installation

### 1. Clone the repository

```bash
git clone https://github.com/sunil-gumatimath/movie-ticket-booking-system.git
cd movie-ticket-booking-system
```

### 2. Configure the database

The application reads database settings from environment variables. Configuration is split into two files:

- [`application.yml`](src/main/resources/application.yml) is the base configuration. It has no credential defaults (`DB_USERNAME` and `DB_PASSWORD` are required), uses `JPA_DDL_AUTO=validate`, and logs at `INFO`.
- [`application-dev.yml`](src/main/resources/application-dev.yml) is the local development profile. It defaults to `root`/`root`, creates the database if missing, uses `JPA_DDL_AUTO=update`, and enables SQL and `DEBUG` logging.

For local development, activate the `dev` profile. Set `JWT_SECRET` as shown below; the database values here are optional overrides, not required setup. Change the default `root`/`root` credentials if your local MySQL uses another account:

```bash
export SPRING_PROFILES_ACTIVE=dev
# Optional overrides; the dev profile defaults to these values.
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=movie-ticket-booking-app-db
export DB_USERNAME=root
export DB_PASSWORD=root
```

Never use the `dev` profile in a deployed environment. Production must use `JPA_DDL_AUTO=validate` (the base default) and follow the reviewed migration guidance in [`docs/production-migration.md`](docs/production-migration.md).

### 3. Configure JWT and CORS

The application requires a strong externally supplied secret in every environment. For example, generate and export one locally with:

```bash
export JWT_SECRET="$(openssl rand -base64 64)"
```

`JWT_SECRET` must decode to at least 64 bytes for HS512. The application fails to start if it is missing or too short. Generate and store a fresh secret for each environment; do not commit it. `JWT_EXPIRATION` is measured in milliseconds and defaults to 24 hours. The default CORS origin is `http://localhost:3000`; set `CORS_ALLOWED_ORIGINS` to your frontend's origin in production.

### 4. Build and run

```bash
mvn clean compile
mvn spring-boot:run
```

This assumes `SPRING_PROFILES_ACTIVE=dev` was exported in step 2. The application starts at `http://localhost:8080` by default.

### Existing MySQL databases

The Java renames did not rename the `user_details` or `shows_table` tables. An existing database may need no SQL change. Back it up, inspect its unique indexes, and run with the base configuration's `JPA_DDL_AUTO=validate` in staging before deploying. Do not use the `dev` profile's automatic schema update on important data. See the [upgrade checklist](docs/production-migration.md#upgrading-an-existing-mysql-database).

## API conventions

- Request bodies use JSON and should be sent with `Content-Type: application/json`.
- IDs and path variables are strings.
- `POST /register` and `POST /login` are the only public application endpoints.
- Swagger, OpenAPI, and related webjars documentation routes are also public.
- Every other application endpoint requires a valid JWT unless a stricter role is listed.
- Send the JWT returned by `POST /login` in the `Authorization` header.

Successful responses use:

```json
{
  "status": 200,
  "message": "Human-readable message",
  "data": {}
}
```

`POST /login` returns `200`; resource-creation endpoints return `201`.

## API endpoints

The API is served at `http://localhost:8080` by default.

### Authentication

| Method | Endpoint | Description | Access |
|---|---|---|---|
| `POST` | `/login` | Authenticate a user and return a JWT token | Public |

Request:

```json
{
  "email": "john.doe@gmail.com",
  "password": "Password123!"
}
```

### User management

| Method | Endpoint | Description | Access |
|---|---|---|---|
| `POST` | `/register` | Register a user account | Public |
| `PUT` | `/users/{userId}` | Update a user profile | Authenticated target user or `ROLE_ADMIN` |
| `DELETE` | `/users/{userId}` | Soft delete a user account | Authenticated target user or `ROLE_ADMIN` |

The `userId` is returned by `POST /register` and is included as the `userId` claim in the login JWT.

Registration request:

```json
{
  "username": "john_doe123",
  "email": "john.doe@gmail.com",
  "phoneNumber": "9876543210",
  "password": "Password123!",
  "dateOfBirth": "1990-05-15"
}
```

Registration always creates `ROLE_USER`. The response data contains `userId`, `username`, `email`, and `userRole`; it does not return the password or phone number. Privileged accounts must be provisioned through a trusted process because no public promotion endpoint exists.

Registration constraints: email must be a Gmail address; username may contain letters, digits, and underscores; phone number must be ten digits and start with `7`, `8`, or `9`; password must be 8–64 characters with upper/lowercase letters, a digit, and a special character; date of birth must be in the past.

Update profile request:

```json
{
  "username": "john_doe_updated",
  "phoneNumber": "9876543210",
  "dateOfBirth": "1990-05-15"
}
```

A user attempting to update or delete another user's account without `ROLE_ADMIN` receives `403 Forbidden`. An unknown or already-deleted `userId` returns `404 Not Found`.

### Theater management

| Method | Endpoint | Description | Access |
|---|---|---|---|
| `POST` | `/theater/register` | Register a theater for the authenticated owner | JWT with `ROLE_THEATER_OWNER` |
| `GET` | `/theater/{id}` | Get theater details by ID | JWT token |
| `PUT` | `/theater/{id}` | Update theater details | `ROLE_THEATER_OWNER` and owner of the theater |

Theater request body for registration and update:

```json
{
  "name": "PVR Cinemas",
  "address": "123 Main Street",
  "city": "Bangalore",
  "landmark": "Near Central Mall"
}
```

### Screen management

| Method | Endpoint | Description | Access |
|---|---|---|---|
| `POST` | `/screen?theaterId={theaterId}` | Add a screen to a theater | `ROLE_THEATER_OWNER` and owner of the theater |
| `GET` | `/screen/{screenId}` | Get screen details and generated seats | JWT token |

Screen request body:

```json
{
  "screenType": "THREE_D",
  "capacity": 150,
  "noOfRows": 10
}
```

Valid screen types are `TWO_D`, `THREE_D`, and `IMAX`. Capacity must be between `1` and `1000`; rows must be between `1` and `26`; capacity must be evenly divisible by the number of rows. A screen creation automatically generates its seat layout.

### Movie management

| Method | Endpoint | Description | Access |
|---|---|---|---|
| `POST` | `/movies` | Create a movie | JWT with `ROLE_ADMIN` |
| `PUT` | `/movies/{movieId}` | Update movie title | JWT with `ROLE_ADMIN` |
| `PUT` | `/movies/{movieId}/description` | Update movie description | JWT with `ROLE_ADMIN` |
| `PUT` | `/movies/{movieId}/cast` | Update movie cast | JWT with `ROLE_ADMIN` |
| `GET` | `/movies/{movieId}` | Get movie details by ID | JWT token |

Create movie request:

```json
{
  "title": "Example Movie",
  "description": "A movie description.",
  "runtime": "PT2H",
  "certificate": "UA",
  "genre": "DRAMA",
  "castList": ["Actor One", "Actor Two"]
}
```

Allowed certificates: `U`, `UA`, `A`, and `S`.

Allowed genres: `ACTION`, `ANIMATION`, `COMEDY`, `DRAMA`, `HORROR`, `ROMANCE`, `SCIENCE_FICTION`, and `THRILLER`.

Runtime must be a positive ISO-8601 duration of no more than 24 hours. Cast names must be nonblank. Movie responses include `ratings`, the average feedback rating as a number rounded to two decimals (`0` when there is no feedback). Update request bodies are:

```json
{ "title": "Updated movie title" }
```

```json
{ "description": "Updated movie description" }
```

```json
{ "castList": ["Updated Actor"] }
```

### Show management

| Method | Endpoint | Description | Access |
|---|---|---|---|
| `POST` | `/theaters/{theaterId}/screens/{screenId}/shows` | Create a show for a movie on an owned screen | `ROLE_THEATER_OWNER`; theater and screen ownership are checked |

Show request body:

```json
{
  "startTimeEpochMillis": 1735094400000,
  "movieId": "movie-uuid-string"
}
```

The movie must already exist. A start time in the past, or a screen that does not belong to the given theater, returns `400 Bad Request`. The end time is calculated from the movie runtime. A show that overlaps an existing show on the same screen returns `409 Conflict`.

### Feedback system

| Method | Endpoint | Description | Access |
|---|---|---|---|
| `POST` | `/movies/{movieId}/feedback` | Create feedback for a movie | Active `ROLE_USER` account |
| `GET` | `/movies/{movieId}/feedback` | Get feedback for a movie | JWT token |

Feedback request body:

```json
{
  "rating": 5,
  "review": "Excellent movie! Highly recommended."
}
```

The rating must be between `1` and `5`; the review must be nonblank and no longer than 500 characters. A user may submit only one feedback entry per movie.

`GET /movies/{movieId}/feedback` is paginated and returns newest first. Use `?page=0&size=20` (the defaults) to page through results.

## Complete endpoint summary

| # | Method | Endpoint | Access |
|---:|---|---|---|
| 1 | `POST` | `/register` | Public |
| 2 | `POST` | `/login` | Public |
| 3 | `PUT` | `/users/{userId}` | Authenticated target user or `ROLE_ADMIN` |
| 4 | `DELETE` | `/users/{userId}` | Authenticated target user or `ROLE_ADMIN` |
| 5 | `POST` | `/theater/register` | `ROLE_THEATER_OWNER` |
| 6 | `GET` | `/theater/{id}` | JWT token |
| 7 | `PUT` | `/theater/{id}` | `ROLE_THEATER_OWNER` and theater owner |
| 8 | `POST` | `/screen?theaterId={theaterId}` | `ROLE_THEATER_OWNER` and theater owner |
| 9 | `GET` | `/screen/{screenId}` | JWT token |
| 10 | `POST` | `/movies` | `ROLE_ADMIN` |
| 11 | `PUT` | `/movies/{movieId}` | `ROLE_ADMIN` |
| 12 | `PUT` | `/movies/{movieId}/description` | `ROLE_ADMIN` |
| 13 | `PUT` | `/movies/{movieId}/cast` | `ROLE_ADMIN` |
| 14 | `GET` | `/movies/{movieId}` | JWT token |
| 15 | `POST` | `/theaters/{theaterId}/screens/{screenId}/shows` | `ROLE_THEATER_OWNER` and owned theater/screen |
| 16 | `POST` | `/movies/{movieId}/feedback` | Active `ROLE_USER` |
| 17 | `GET` | `/movies/{movieId}/feedback` | JWT token |

For a machine-readable endpoint list, see [`api_endpoints.json`](api_endpoints.json).

## Authentication and authorization

Authentication uses stateless JWT security:

1. Register an account with `POST /register`.
2. Log in with `POST /login` using the registered email and password.
3. Save the returned JWT token.
4. Send it in the `Authorization: Bearer <token>` header for protected requests.

Public application routes:

- `POST /register`
- `POST /login`

Public documentation routes:

- `/swagger-ui/**`
- `/v3/api-docs/**`
- `/swagger-resources/**`
- `/webjars/**`

Role checks use `@PreAuthorize` on controllers. Resource ownership (which theater or account the caller may change) is checked in services through `CurrentUserService`. The additional restrictions are:

- `ROLE_THEATER_OWNER`: theater registration, theater updates, screen creation, and show creation for owned resources.
- `ROLE_ADMIN`: movie catalog management and any-user profile administration.
- Active `ROLE_USER`: feedback creation. Theater-owner and administrator accounts cannot use feedback creation.

## Database schema

The application uses MySQL and contains these main entities:

- **Users:** user accounts and roles.
- **Theaters:** cinema locations managed by theater owners.
- **Screens:** theater halls with screen type, capacity, and row information.
- **Movies:** movie catalog data.
- **Shows:** scheduled movie screenings.
- **Seats:** generated seat entities associated with screens.
- **Feedback:** user reviews and ratings for movies.

## Error handling

Validation and application errors use an error structure with these properties:

```json
{
  "statusCode": 400,
  "error_message": "Validation failed for one or more fields",
  "timestamp": "2026-09-24T06:30:00.123Z",
  "path": "/register",
  "data": [
    {
      "field": "email",
      "rejectedValue": "invalid-email",
      "errorMessage": "Enter a valid Gmail ID"
    }
  ]
}
```

Every error, including security errors raised before a controller runs, populates `statusCode`, `error_message`, a UTC `timestamp`, and the request `path`. Validation errors also include `data`. `rejectedValue` is omitted for password fields. Clients should use the HTTP status code and `error_message` for application errors.

Common HTTP status codes:

- `200` — successful read or update.
- `201` — successful resource creation.
- `400` — validation error or bad request.
- `401` — missing or invalid JWT, or failed authentication.
- `403` — authenticated user lacks permission or resource ownership.
- `404` — resource or route not found.
- `405` — HTTP method not supported.
- `409` — state conflict, duplicate resource, or overlapping show.
- `500` — internal server error.

Errors raised in controllers and services are handled by `GlobalExceptionHandler`; missing or invalid JWTs are handled by `JsonSecurityErrorHandler` before reaching a controller.

## Testing

Run the test suite with Maven:

```bash
mvn test
```

## Dependencies

Key dependencies include Spring Web, Spring Data JPA, Spring Security, JJWT, MySQL Connector/J, Lombok, Spring Validation, and springdoc-openapi.
