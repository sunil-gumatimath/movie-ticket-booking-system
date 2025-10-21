# Movie Ticket Booking System API

A Spring Boot REST API for managing movie ticket bookings with JWT authentication and role-based access control.

## Features

- User registration and authentication
- Theater and screen management
- Movie and show scheduling
- Seat booking system
- Rating and feedback system
- JWT-based security with USER and THEATER_OWNER roles

## Tech Stack

- **Java**: 17
- **Spring Boot**: 3.4.4
- **Database**: MySQL
- **Security**: Spring Security + JWT
- **Build**: Maven
- **ORM**: JPA/Hibernate

## Prerequisites

- Java 17+
- Maven 3.6+
- MySQL 8.0+

## Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/sunil-gumatimath/movie-ticket-booking-system.git
   cd movie-ticket-booking-system-api
   ```

2. **Database Setup**
   - Create MySQL database: `movie-ticket-booking-app-db`
   - Configure connection via environment variables:
   ```bash
   export DB_USERNAME=your_username
   export DB_PASSWORD=your_password
   export DB_HOST=localhost
   export DB_PORT=3306
   export DB_NAME=movie-ticket-booking-app-db
   ```

3. **Build and Run**
   ```bash
   mvn clean compile
   mvn spring-boot:run
   ```

The API will be available at `http://localhost:8080`

## Configuration

### Environment Variables
```bash
DB_HOST=localhost          # Database host (default: localhost)
DB_PORT=3306              # Database port (default: 3306)
DB_NAME=movie-ticket-booking-app-db  # Database name
DB_USERNAME=your_username  # Database username
DB_PASSWORD=your_password  # Database password
JWT_SECRET=your-secret     # JWT secret key
JWT_EXPIRATION=86400000    # JWT expiration in ms (default: 24h)
```

## API Endpoints

### Authentication
- `POST /register` - Register new user
- `POST /login` - User login

### Movies
- `GET /movies/{movieId}` - Get movie details

### Theater Management
- `POST /theater/register` - Create theater (THEATER_OWNER)
- `GET /theater/{id}` - Get theater details
- `PUT /theater/{id}` - Update theater (THEATER_OWNER)

### Screen Management
- `POST /screen` - Add screen to theater
- `GET /screen/{screenId}` - Get screen details

### Show Management
- `POST /theaters/{theaterId}/screens/{screenId}/shows` - Create show (THEATER_OWNER)

### Feedback
- `POST /movies/{movieId}/feedback` - Add movie feedback (USER)
- `GET /movies/{movieId}/feedback` - Get movie feedback

## Project Structure

```
src/main/java/com/example/movieticketbookingsystem/
├── controller/     # REST controllers
├── entity/        # JPA entities
├── service/       # Service interfaces
├── serviceImpl/   # Service implementations
├── repository/    # Data repositories
├── security/      # Security configuration
├── dto/          # Data transfer objects
├── exception/    # Custom exceptions
└── utility/      # Utility classes (4 utilities)
```

## Testing

```bash
# Run all tests
mvn test

# Run with coverage
mvn test jacoco:report
```

## Deployment

### Docker
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Build
```bash
mvn clean package -DskipTests
java -jar target/movie-ticket-booking-system-api-*.jar
```

---

**License**: MIT
