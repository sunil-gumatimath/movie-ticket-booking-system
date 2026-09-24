# Movie Ticket Booking System API

A Spring Boot REST API for managing users, theaters, screens, movies, shows, and movie feedback. The API uses JWT authentication and role-based authorization.

> **Documentation scope:** This is the main project README. The additional README at `src/main/java/com/example/movieticketbookingsystem/README.md` documents the exception-handling architecture and is not a second project setup guide.

## Features

### User and Authentication Management

- User registration with `ROLE_USER` or `ROLE_THEATER_OWNER`
- JWT login and stateless authentication
- User profile updates
- Soft deletion of user accounts
- Role-based authorization for privileged operations

### Theater and Show Management

- Theater registration by theater owners
- Theater lookup and updates
- Screen creation and lookup
- Movie show scheduling for a theater screen
- Show overlap detection and scheduling conflict handling

### Movie and Feedback Management

- Movie lookup by movie ID
- User feedback creation for movies
- Feedback retrieval for a movie

The current controller layer provides the operations listed below. Ticket booking and seat reservation endpoints are not currently exposed by the controllers.

## Tech Stack

- **Framework:** Spring Boot 3.4.4
- **Language:** Java 17
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

## Setup and Installation

### 1. Clone the repository

```bash
git clone https://github.com/sunil-gumatimath/movie-ticket-booking-system.git
cd movie-ticket-booking-system-api
```

### 2. Configure the database

The application reads database settings from environment variables. Defaults are defined in `src/main/resources/application.yml`:

```bash
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=movie-ticket-booking-app-db
export DB_USERNAME=root
export DB_PASSWORD=root
```

MySQL must be running and accessible using these credentials. The application uses `ddl-auto: update`, so Hibernate updates the schema on startup.

### 3. Configure JWT

The defaults in `application.yml` are suitable for local development. Set your own values for other environments:

```bash
export JWT_SECRET='<base64-encoded-secret>'
export JWT_EXPIRATION=86400000
```

`JWT_EXPIRATION` is measured in milliseconds and defaults to 24 hours.

### 4. Build and run

```bash
# Compile the project
mvn clean compile

# Run the application
mvn spring-boot:run
```

The application starts at:

```text
http://localhost:8080
```

## API Conventions

- All request bodies use JSON and must include `Content-Type: application/json`.
- Validation failures return `400 Bad Request`.
- Successful responses use the common response structure with `statusCode`, `message`, and `data` fields.
- IDs and path variables are strings.
- `POST /register` and `POST /login` are public.
- Every other application endpoint requires a valid JWT token unless a stricter role is listed.
- Swagger and OpenAPI documentation routes are public.

### JWT Authorization Header

Send the token returned by `POST /login` for protected endpoints:

```http
Authorization: Bearer <jwt-token>
```

## API Endpoints

The API is served at `http://localhost:8080` by default.

### Authentication

| Method | Endpoint | Description | Access |
|---|---|---|---|
| `POST` | `/login` | Authenticate a user and return a JWT token | Public |

Login request:

```json
{
  "email": "john.doe@gmail.com",
  "password": "Password123!"
}
```

### User Management

| Method | Endpoint | Description | Access |
|---|---|---|---|
| `POST` | `/register` | Register a user account | Public |
| `PUT` | `/update?email={email}` | Update a user profile | JWT Token |
| `DELETE` | `/delete?email={email}` | Soft delete a user account | JWT Token |

Registration request:

```json
{
  "username": "john_doe123",
  "email": "john.doe@gmail.com",
  "phoneNumber": "9876543210",
  "password": "Password123!",
  "dateOfBirth": "1990-05-15",
  "userRole": "ROLE_USER"
}
```

Valid user roles are `ROLE_USER` and `ROLE_THEATER_OWNER`.

Update profile request:

```json
{
  "username": "john_doe_updated",
  "phoneNumber": "9876543210",
  "dateOfBirth": "1990-05-15"
}
```

### Theater Management

| Method | Endpoint | Description | Access |
|---|---|---|---|
| `POST` | `/theater/register?email={email}` | Register a theater for an owner | JWT with `ROLE_THEATER_OWNER` |
| `GET` | `/theater/{id}` | Get theater details by ID | JWT Token |
| `PUT` | `/theater/{id}` | Update theater details | JWT with `ROLE_THEATER_OWNER` |

Theater request body for registration and update:

```json
{
  "name": "PVR Cinemas",
  "address": "123 Main Street",
  "city": "Bangalore",
  "landmark": "Near Central Mall"
}
```

### Screen Management

| Method | Endpoint | Description | Access |
|---|---|---|---|
| `POST` | `/screen?theaterId={theaterId}` | Add a screen to a theater | JWT Token |
| `GET` | `/screen/{screenId}` | Get screen details | JWT Token |

Screen request body:

```json
{
  "screenType": "THREE_D",
  "capacity": 150,
  "noOfRows": 10
}
```

Valid screen types are `TWO_D`, `THREE_D`, and `IMAX`.

### Show Management

| Method | Endpoint | Description | Access |
|---|---|---|---|
| `POST` | `/theaters/{theaterId}/screens/{screenId}/shows` | Create a show for a movie on a screen | JWT with `ROLE_THEATER_OWNER` |

Show request body:

```json
{
  "startTimeEpochMillis": 1735094400000,
  "movieId": "movie-789"
}
```

### Movie Management

| Method | Endpoint | Description | Access |
|---|---|---|---|
| `GET` | `/movies/{movieId}` | Get movie details by ID | JWT Token |

### Feedback System

| Method | Endpoint | Description | Access |
|---|---|---|---|
| `POST` | `/movies/{movieId}/feedback` | Create feedback for a movie | JWT with `ROLE_USER` |
| `GET` | `/movies/{movieId}/feedback` | Get feedback for a movie | JWT Token |

Feedback request body:

```json
{
  "rating": 5,
  "review": "Excellent movie! Highly recommended."
}
```

The rating must be between `1` and `5`, and the review cannot be blank.

## Complete Endpoint Summary

| # | Method | Endpoint | Access |
|---:|---|---|---|
| 1 | `POST` | `/login` | Public |
| 2 | `POST` | `/register` | Public |
| 3 | `PUT` | `/update?email={email}` | JWT Token |
| 4 | `DELETE` | `/delete?email={email}` | JWT Token |
| 5 | `POST` | `/theater/register?email={email}` | JWT with `ROLE_THEATER_OWNER` |
| 6 | `GET` | `/theater/{id}` | JWT Token |
| 7 | `PUT` | `/theater/{id}` | JWT with `ROLE_THEATER_OWNER` |
| 8 | `POST` | `/screen?theaterId={theaterId}` | JWT Token |
| 9 | `GET` | `/screen/{screenId}` | JWT Token |
| 10 | `POST` | `/theaters/{theaterId}/screens/{screenId}/shows` | JWT with `ROLE_THEATER_OWNER` |
| 11 | `GET` | `/movies/{movieId}` | JWT Token |
| 12 | `POST` | `/movies/{movieId}/feedback` | JWT with `ROLE_USER` |
| 13 | `GET` | `/movies/{movieId}/feedback` | JWT Token |

For a machine-readable endpoint list, see [`api_endpoints.json`](api_endpoints.json). For a priority-based short reference, see [`apiendpointpriority.md`](apiendpointpriority.md). For step-by-step examples, see [`steps.md`](steps.md).

## Authentication and Authorization

Authentication uses stateless JWT security:

1. Register an account with `POST /register`.
2. Log in with `POST /login` using the registered `email` and `password`.
3. Save the returned JWT token.
4. Send the token in the `Authorization` header for protected requests.

The application permits only these application routes without authentication:

- `POST /register`
- `POST /login`

The following documentation routes are also public:

- `/swagger-ui/**`
- `/v3/api-docs/**`
- `/swagger-resources/**`
- `/webjars/**`

All other application routes require authentication. Method-level authorization further restricts the following operations:

- `ROLE_THEATER_OWNER`: theater registration, theater updates, and show creation
- `ROLE_USER`: feedback creation

## Database Schema

The application uses MySQL and contains the following main entities:

- **Users:** User accounts and roles
- **Theaters:** Cinema locations managed by theater owners
- **Screens:** Theater halls with screen type, capacity, and row information
- **Movies:** Movie information
- **Shows:** Scheduled movie screenings
- **Seats:** Seat entities associated with screens
- **Feedback:** User reviews and ratings for movies

## Error Handling

Validation and application errors use a consistent response structure. The exact error payload can vary by exception type, but clients should use the HTTP status code and `message` fields.

Common HTTP status codes:

- `200` - Successful read or update
- `201` - Resource created
- `400` - Validation error or bad request
- `401` - Missing or invalid JWT
- `403` - Authenticated user lacks the required role
- `404` - Resource not found
- `409` - Resource conflict, such as an overlapping show
- `500` - Internal server error

For exception-handling details, see [`src/main/java/com/example/movieticketbookingsystem/README.md`](src/main/java/com/example/movieticketbookingsystem/README.md).

## Testing

Run the test suite with Maven:

```bash
mvn test
```

## Dependencies

Key dependencies include:

- Spring Web
- Spring Data JPA
- Spring Security
- JJWT
- MySQL Connector/J
- Lombok
- Spring Validation
- springdoc-openapi

Developed with Spring Boot.
