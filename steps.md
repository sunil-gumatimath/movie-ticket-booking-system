# Movie Ticket Booking System API - Complete Usage Guide

This guide provides step-by-step instructions for using the Movie Ticket Booking System API, organized by priority and typical user workflows.

## Prerequisites

1. **Application Setup**: Ensure the Spring Boot application is running on `http://localhost:8080`
2. **Database**: MySQL database is configured and running
3. **Authentication**: JWT tokens are required for protected endpoints

## API Endpoints with Step-by-Step Usage

### Step 1: User Registration
**Endpoint**: `POST /register`  
**Authentication**: None  
**Description**: Register a new user account (ROLE_USER) or theater owner account (ROLE_THEATER_OWNER).

**Request**:
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

**Response**:
```json
{
  "statusCode": 201,
  "message": "UserDetail Created",
  "data": {
    "userId": "uuid-string",
    "username": "john_doe123",
    "email": "john.doe@gmail.com",
    "phoneNumber": "9876543210",
    "dateOfBirth": "1990-05-15",
    "userRole": "ROLE_USER"
  }
}
```

### Step 2: User Login
**Endpoint**: `POST /login`  
**Authentication**: None  
**Description**: Authenticate user and receive JWT token.

**Request**:
```json
{
  "email": "john.doe@gmail.com",
  "password": "Password123!"
}
```

**Response** (Save the token for subsequent requests):
```json
{
  "statusCode": 200,
  "message": "Login successful",
  "data": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### Step 3: Update User Profile
**Endpoint**: `PUT /update?email=john.doe@gmail.com`  
**Authentication**: JWT Token  
**Description**: Update user profile information.

**Headers**:
```
Authorization: Bearer <your-jwt-token>
```

**Request**:
```json
{
  "username": "john_doe_updated",
  "phoneNumber": "9876543210",
  "dateOfBirth": "1990-05-15"
}
```

### Step 4: Soft Delete User Account
**Endpoint**: `DELETE /delete?email=john.doe@gmail.com`  
**Authentication**: JWT Token  
**Description**: Soft delete user account.

**Headers**:
```
Authorization: Bearer <your-jwt-token>
```

**Response**:
```json
{
  "statusCode": 200,
  "message": "User account deleted successfully (soft delete).",
  "data": null
}
```

### Step 5: Theater Registration (Theater Owner Only)
**Endpoint**: `POST /theater/register?email=theater.owner@gmail.com`  
**Authentication**: JWT Token (**ROLE_THEATER_OWNER**)  
**Description**: Register a new theater.

**Headers**:
```
Authorization: Bearer <theater-owner-jwt-token>
```

**Request**:
```json
{
  "name": "PVR Cinemas",
  "address": "123 Main Street",
  "city": "Bangalore",
  "landmark": "Near Central Mall"
}
```

### Step 6: Get Theater Details
**Endpoint**: `GET /theater/{id}`  
**Authentication**: JWT Token  
**Description**: Get theater details by ID.

### Step 7: Update Theater (Theater Owner Only)
**Endpoint**: `PUT /theater/{id}`  
**Authentication**: JWT Token (**ROLE_THEATER_OWNER**)  
**Description**: Update theater details.

**Request**:
```json
{
  "name": "PVR Cinemas - Updated",
  "address": "456 New Street",
  "city": "Bangalore",
  "landmark": "Near Airport"
}
```

### Step 8: Add Screen to Theater
**Endpoint**: `POST /screen?theaterId={theaterId}`  
**Authentication**: JWT Token  
**Description**: Add a screen to a theater.

**Request**:
```json
{
  "screenType": "THREE_D",
  "capacity": 150,
  "noOfRows": 10
}
```

**Note**: `screenType` options: `TWO_D`, `THREE_D`, `IMAX`.

### Step 9: Get Screen Details
**Endpoint**: `GET /screen/{screenId}`  
**Authentication**: JWT Token  
**Description**: Get screen details by ID.

### Step 10: Add Show to Screen (Theater Owner Only)
**Endpoint**: `POST /theaters/{theaterId}/screens/{screenId}/shows`  
**Authentication**: JWT Token (**ROLE_THEATER_OWNER**)  
**Description**: Add a show to a screen.

**Request**:
```json
{
  "startTimeEpochMillis": 1735094400000,
  "movieId": "movie-uuid-string"
}
```

### Step 11: Get Movie Details
**Endpoint**: `GET /movies/{movieId}`  
**Authentication**: JWT Token  
**Description**: Get movie details by ID.

### Step 12: Create Feedback for Movie (User Only)
**Endpoint**: `POST /movies/{movieId}/feedback`  
**Authentication**: JWT Token (**ROLE_USER**)  
**Description**: Create feedback for a movie.

**Request**:
```json
{
  "rating": 5,
  "review": "Excellent movie! Highly recommended."
}
```

### Step 13: Get Feedbacks for Movie
**Endpoint**: `GET /movies/{movieId}/feedback`  
**Authentication**: JWT Token  
**Description**: Get all feedbacks for a specific movie.

## Authentication Headers

For protected endpoints, include the JWT token in the Authorization header:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

## Error Response Format

All endpoints return consistent error responses:

```json
{
  "statusCode": 400,
  "message": "Validation failed",
  "data": {
    "field": "email",
    "reason": "Enter a valid Gmail ID"
  }
}
```

## Common Status Codes

- `200` - Success (GET/PUT requests)
- `201` - Created (POST requests)
- `400` - Bad Request (validation errors)
- `401` - Unauthorized (invalid/missing JWT)
- `403` - Forbidden (insufficient permissions)
- `404` - Not Found
- `409` - Conflict

## Workflow Summary

1. **Register & Login**: Register as `ROLE_USER` or `ROLE_THEATER_OWNER`. Login to get JWT.
2. **Theater Management**: Owners register theaters, add screens, and then add shows.
3. **Discovery**: Users browse movies and theaters.
4. **Engagement**: Users leave feedback on movies they've watched.