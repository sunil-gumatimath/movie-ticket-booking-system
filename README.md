# Movie Ticket Booking System API

A secure and robust REST API for managing movie ticket bookings, built with Spring Boot. This system allows users to register, browse theaters, view movies, and book tickets through a comprehensive digital interface.

## 📋 Description

The Movie Ticket Booking System API is a software application that enables users to browse movies, select showtimes, choose seats, and book tickets online. Theater owners can manage their cinemas, screens, and show schedules efficiently.

## 🛠️ Tech Stack

- **Framework:** Spring Boot 3.4.4
- **Language:** Java 17
- **Database:** MySQL 8.0+
- **Security:** JSON Web Tokens (JWT)
- **ORM:** JPA/Hibernate
- **Validation:** Bean Validation (Jakarta Validation)
- **Build Tool:** Maven

## ✨ Features

### User Management
- User registration and authentication
- JWT-based authorization
- Profile management and soft delete
- Role-based access (USER, THEATER_OWNER)

### Theater Management
- Create and manage theaters
- Add multiple screens to theaters
- Update theater information

### Movie Management
- Fetch movie details by ID
- Support for movie feedback system

### Show Management
- Create shows for movies in specific screens
- Automatic overlap detection to prevent conflicts
- Time-based scheduling with start/end times

### Booking System
- Screen and seat management (foundation established)
- Show creation with capacity checking

### Feedback System
- Users can provide movie feedback
- Retrieve feedback by movie

## 🔧 Prerequisites

Before running the application, ensure you have the following installed:

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6+
- Git

## 🚀 Setup and Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/sunil-gumatimath/movie-ticket-booking-system.git
   cd movie-ticket-booking-system-api
   ```

2. **Set up MySQL Database:**
   - Create a new MySQL database
   - Update the following environment variables (or use default values):
     ```bash
     export DB_HOST=localhost
     export DB_PORT=3306
     export DB_NAME=movie-ticket-booking-app-db
     export DB_USERNAME=root
     export DB_PASSWORD=root
     ```

3. **Configure JWT Secret (Optional):**
   ```bash
   export JWT_SECRET=pguQuMEr0QorbK6ZrCPITXw5/NRf7zRW2yjh4/WpN4c=
   export JWT_EXPIRATION=86400000  # 24 hours in milliseconds
   ```

4. **Build and run the application:**
   ```bash
   # Build the project
   mvn clean compile

   # Run the application
   mvn spring-boot:run

   # Or run with Maven wrapper
   ./mvnw spring-boot:run
   ```

The application will start on `http://localhost:8080`

## 📖 API Endpoints

### Authentication
- `POST /login` - User login (returns JWT token)

### User Management
- `POST /register` - Register new user
- `PUT /update?email={email}` - Update user profile
- `DELETE /delete?email={email}` - Soft delete user account

### Theater Management
- `POST /theater` - Create new theater
- `GET /theater/{id}` - Get theater by ID
- `PUT /theater/{id}` - Update theater

### Screen Management
- `POST /screen?theaterId={theaterId}` - Add screen to theater
- `GET /screen/{screenId}` - Get screen details

### Show Management
- `POST /show` - Create new show for a movie

### Movie Management
- `GET /movies/{movieId}` - Get movie details

### Feedback System
- `POST /feedback` - Create movie feedback
- `GET /feedback/movie/{movieId}` - Get all feedback for a movie

## 🔐 Authentication

This API uses JWT (JSON Web Tokens) for authentication:

1. **Registration:** Use `POST /register` to create a new user account
2. **Login:** Use `POST /login` with valid credentials to receive a JWT token
3. **Authorization:** Include the token in the `Authorization` header for protected endpoints:
   ```
   Authorization: Bearer <your-jwt-token>
   ```

## 📊 Database Schema

The application uses MySQL with the following main entities:

- **Users:** User accounts with roles (USER, THEATER_OWNER)
- **Theaters:** Cinema locations managed by theater owners
- **Screens:** Theater halls with capacity and screen type
- **Movies:** Movie information
- **Shows:** Scheduled movie screenings
- **Seats:** Individual seats in screens
- **Feedback:** User reviews for movies

## 🧪 Testing

Run the test suite with Maven:
```bash
mvn test
```

## 📝 Sample Usage

### Create a Show
See `sample-show-creation-output.md` for detailed example of creating a movie show with overlap prevention.

### Example API Request (Login)
```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "password": "Password123!"
  }'
```

### Example API Response
```json
{
  "statusCode": 200,
  "message": "Login successful",
  "data": "eyJhbGciOiJIUzI1NiJ9..."
}
```

## 🚦 Error Handling

The API uses consistent error response structure:

```json
{
  "statusCode": 400,
  "message": "Validation failed",
  "data": {
    "field": "email",
    "reason": "Email format is invalid"
  }
}
```

Common HTTP status codes:
- `200/201` - Success
- `400` - Bad Request (validation errors)
- `401` - Unauthorized
- `403` - Forbidden
- `404` - Not Found
- `409` - Conflict (e.g., overlapping shows)
- `500` - Internal Server Error

## 🔄 Dependencies

Key dependencies include:
- Spring Web
- Spring Data JPA
- Spring Security
- JWT (JJWT)
- MySQL Connector
- Lombok
- Spring Validation

Built with ❤️ using Spring Boot
