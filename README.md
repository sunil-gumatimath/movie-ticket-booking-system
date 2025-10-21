# Movie Ticket Booking System API

A comprehensive Spring Boot REST API for managing movie ticket bookings with clean architecture and robust exception handling.

## 🚀 Features

- **User Management**: Registration, authentication, and profile management
- **Movie Management**: CRUD operations for movies with ratings and feedback
- **Theater Management**: Theater and screen management for theater owners
- **Show Management**: Schedule and manage movie shows
- **Seat Management**: Dynamic seat layout generation and management
- **Feedback System**: User reviews and ratings for movies
- **JWT Authentication**: Secure token-based authentication
- **Role-based Access Control**: Different access levels for users and theater owners
- **Clean Exception Handling**: Domain-specific exception handlers with consistent error responses

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.x
- **Security**: Spring Security with JWT
- **Database**: MySQL with JPA/Hibernate
- **Build Tool**: Maven
- **Java Version**: 17
- **Additional Libraries**:
  - Lombok for boilerplate reduction
  - JJWT for JWT token handling
  - Validation API for input validation

## 📁 Project Structure

```
src/main/java/com/example/movieticketbookingsystem/
├── auditor/               # JPA auditing configuration
├── config/                # Configuration classes
├── controller/            # REST controllers (7 controllers)
├── dto/                   # Data Transfer Objects
│   ├── request/          # Request DTOs (7 DTOs)
│   └── response/         # Response DTOs (7 DTOs)
├── entity/               # JPA entities (9 entities)
├── enums/                # Enumerations (4 enums)
├── exception/            # Custom exceptions (9 exceptions)
│   └── handler/         # Exception handlers (7 handlers)
├── mapper/               # Object mappers (7 mappers)
├── repository/           # JPA repositories (8 repositories)
├── security/             # Security configuration and JWT
│   └── jwt/             # JWT implementation
├── service/              # Service interfaces (8 services)
├── serviceImpl/          # Service implementations (8 implementations)
└── utility/              # Utility classes (4 utilities)
```

## 🔧 Setup and Installation

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd movie-ticket-booking-system-api
   ```

2. **Configure Database**
   - Create a MySQL database named `movie-ticket-booking-app-db`
   - Configure database credentials via environment variables (or edit `application.yml`):
   ```bash
   # Environment variables (recommended)
   export DB_USERNAME=your_username
   export DB_PASSWORD=your_password
   export DB_HOST=localhost
   export DB_PORT=3306
   export DB_NAME=movie-ticket-booking-app-db

   # Or directly in application.yml (for development only):
   spring:
     datasource:
       url: jdbc:mysql://${DB_HOST:localhost}:${DB_PORT:3306}/${DB_NAME:movie-ticket-booking-app-db}?createDatabaseIfNotExist=true
       username: ${DB_USERNAME:root}
       password: ${DB_PASSWORD:root}
   ```

3. **Build the project**
   ```bash
   mvn clean compile
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The API will be available at `http://localhost:8080`

5. **Configure JWT (Optional)**
   ```yaml
   jwt:
     secret: your-secret-key-here
     expiration: 86400000  # 24 hours
   ```

## 📚 API Endpoints

### Authentication
- `POST /register` - User registration
- `POST /login` - User login

### User Management
- `PUT /update?email={email}` - Update user profile
- `DELETE /delete?email={email}` - Soft delete user account

### Movies
- `GET /movies/{movieId}` - Get movie details

### Theater Management
- `POST /theater/register?email={email}` - Create theater (THEATER_OWNER only)
- `GET /theater/{id}` - Get theater details
- `PUT /theater/{id}` - Update theater (THEATER_OWNER only)

### Screen Management
- `POST /screen?theaterId={id}` - Add screen to theater
- `GET /screen/{screenId}` - Get screen details with seat layout

### Show Management
- `POST /theaters/{theaterId}/screens/{screenId}/shows` - Create new show (THEATER_OWNER only)

### Feedback System
- `POST /movies/{movieId}/feedback` - Add movie feedback (USER only)
- `GET /movies/{movieId}/feedback` - Get movie feedback

## 🛡️ Exception Handling

The application features a clean and optimized exception handling system with domain-specific handlers and consistent error responses.

### Key Features
- **Domain-Specific Handlers**: Separate handlers for each business domain
- **Consistent Error Format**: Standardized error responses using RestResponseBuilder
- **Specific Exception Classes**: No generic exceptions, all domain-specific
- **Clean Architecture**: Removed redundant and duplicate handlers

### Exception Handlers Overview
- **UserExceptionHandler**: User-related exceptions (registration, authentication)
- **MovieExceptionHandler**: Movie-related exceptions (not found, etc.)
- **TheaterExceptionHandler**: Theater-related exceptions (owner validation)
- **ScreenExceptionHandler**: Screen-related exceptions (screen not found)
- **SecurityExceptionHandler**: Authentication/authorization exceptions
- **ValidationExceptionHandler**: Input validation exceptions with field details
- **GeneralExceptionHandler**: General application exceptions (lowest priority)

### Quick Exception Reference
- `UserNotFoundByEmailException` → 404 NOT_FOUND
- `UserExistByEmailException` → 409 CONFLICT
- `UserNotRegistered` → 401 UNAUTHORIZED
- `MovieNotFoundByIdException` → 404 NOT_FOUND
- `TheaterOwnerIdException` → 404 NOT_FOUND
- `TheaterScreenMismatchException` → 400 BAD_REQUEST
- `ScreenIdNotFoundException` → 404 NOT_FOUND
- `ConflictException` → 409 CONFLICT
- `ResourceNotFoundException` → 404 NOT_FOUND

📖 **For detailed exception handling documentation, see**: [`src/main/java/com/example/movieticketbookingsystem/README.md`](src/main/java/com/example/movieticketbookingsystem/README.md)

## 🔒 Security

- **JWT-based Authentication**: Secure token-based authentication with configurable expiration
- **Role-based Access Control**: Two user roles (USER, THEATER_OWNER) with different permissions
- **Password Encryption**: BCrypt hashing for secure password storage
- **Stateless Session Management**: No server-side session storage
- **Security Filter Chain**: Custom JWT authentication filter with proper error handling
- **Protected Endpoints**: Role-specific access control using `@PreAuthorize` annotations

## 📊 Recent Updates (2025)

### Code Maintenance & Clean Up
- ✅ **Fixed** minor issues in UserServiceImpl: removed unused imports (`TheaterOwner`, `User`, `SecurityConfig`, `LocalDateTime`) and leftover comments
- ✅ **Optimized** logging configuration: Changed Spring Security logging from DEBUG to INFO for production-ready security logs
- ✅ **Updated** project statistics to reflect accurate file count (93 Java files) and descriptive component roles
- ✅ **Enhanced** documentation with detailed monitoring features and test structure

## 📊 Recent Updates (2024)

### Exception System Enhancement
- ✅ **Added** missing exception classes (`ConflictException`, `ResourceNotFoundException`, `TheaterScreenMismatchException`)
- ✅ **Removed** duplicate and unused code (TestFilter, duplicate RestResponseBuilder)
- ✅ **Fixed** all compilation errors and logical inconsistencies
- ✅ **Implemented** domain-specific exception handling with 7 specialized handlers
- ✅ **Standardized** RestResponseBuilder usage across all controllers and handlers

### Code Quality Improvements
- ✅ **Enhanced** service implementations with semantically correct exception usage
- ✅ **Implemented** clean architecture with proper separation of concerns
- ✅ **Standardized** error response format with timestamps and request paths
- ✅ **Added** comprehensive bean configuration and health checks
- ✅ **Fixed** dependency injection across all controllers
- ✅ **Updated** documentation to reflect current system state

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📚 Documentation

### Main Documentation
- **Project Overview**: This README.md (you are here)
- **Exception Handling**: [`src/main/java/com/example/movieticketbookingsystem/README.md`](src/main/java/com/example/movieticketbookingsystem/README.md)
- **API Documentation**: Available endpoints listed above
- **Setup Guide**: Installation and configuration instructions above

### Quick Links
- [🚀 Setup Instructions](#setup-and-installation)
- [📚 API Endpoints](#api-endpoints)
- [🛡️ Exception Handling](#exception-handling)
- [🔒 Security Features](#security)
- [📊 Recent Updates](#recent-updates-2025)

## 🔧 **Troubleshooting**

### Common Issues

#### Database Connection Issues
```bash
# Check if MySQL is running
sudo systemctl status mysql

# Verify database exists
mysql -u root -p -e "SHOW DATABASES;"
```

#### JWT Token Issues
- Ensure JWT secret is properly configured
- Check token expiration settings
- Verify token format in Authorization header: `Bearer <token>`

#### Build Issues
```bash
# Clean and rebuild
mvn clean install

# Skip tests if needed
mvn clean install -DskipTests
```

#### Port Already in Use
```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>
```

## 📞 Support

For support and questions:
- **Issues**: Open an issue in the repository
- **Documentation**: Check the detailed README files
- **Logs**: Enable debug logging for troubleshooting

---

**Status**: ✅ Active Development | 🚀 Production Ready | 🛡️ Secure | 🧹 Clean Architecture

---

## 🎯 **Project Statistics**

- **Total Java Files**: 67
- **Controllers**: 7 (all with proper dependency injection)
- **Service Implementations**: 8 (with comprehensive business logic)
- **JPA Entities**: 9 (with proper relationships and auditing)
- **Exception Handlers**: 7 (domain-specific with consistent patterns)
- **Custom Exceptions**: 9 (semantically correct and specific)
- **DTOs**: 14 (request and response objects)
- **Repositories**: 8 (JPA-based data access)
- **Mappers**: 7 (object mapping utilities)
- **Utility Classes**: 4 (response building and error handling)

## 🧪 **Testing**

### Running Tests
```bash
# Run all tests
mvn test

# Run tests with coverage
mvn test jacoco:report
```

### Test Structure
- **Context Loading Test**: Verifies Spring Boot application context loads correctly
- **Integration Tests**: Controller and repository testing (extensible)
- **Security Tests**: Authentication and authorization flow testing (expandable)

## 🚀 **Deployment**

### Docker Deployment (Recommended)
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/movie-ticket-booking-system-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Environment Variables
```bash
# Database Configuration
DB_HOST=localhost
DB_PORT=3306
DB_NAME=movie-ticket-booking-app-db
DB_USERNAME=your_username
DB_PASSWORD=your_password

# JWT Configuration
JWT_SECRET=your-secret-key-here
JWT_EXPIRATION=86400000
```

## 🔍 **Monitoring & Health Checks**

The application includes built-in health monitoring:
- **Startup Health Check**: Validates all critical beans on application start with detailed component status
- **Bean Validation**: Ensures all services, repositories, and handlers are properly configured
- **Comprehensive Logging**: Tiered logging with DEBUG for app, INFO for security (production-ready)

## 🔧 **Troubleshooting**

### Common Issues

#### Database Connection Issues
```bash
# Check if MySQL is running
sudo systemctl status mysql

# Verify database exists
mysql -u root -p -e "SHOW DATABASES;"
```

#### JWT Token Issues
- Ensure JWT secret is properly configured
- Check token expiration settings
- Verify token format in Authorization header: `Bearer <token>`

#### Build Issues
```bash
# Clean and rebuild
mvn clean install

# Skip tests if needed
mvn clean install -DskipTests
```

#### Port Already in Use
```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📚 Documentation

### Main Documentation
- **Project Overview**: This README.md (you are here)
- **Exception Handling**: [`src/main/java/com/example/movieticketbookingsystem/README.md`](src/main/java/com/example/movieticketbookingsystem/README.md)
- **API Documentation**: Available endpoints listed above
- **Setup Guide**: Installation and configuration instructions above

### Quick Links
- [🚀 Setup Instructions](#setup-and-installation)
- [📚 API Endpoints](#api-endpoints)
- [🛡️ Exception Handling](#exception-handling)
- [🔒 Security Features](#security)
- [📊 Recent Updates](#recent-updates-2025)
- [🔧 Troubleshooting](#troubleshooting)

## 📞 Support

For support and questions:
- **Issues**: Open an issue in the repository
- **Documentation**: Check the detailed README files
- **Logs**: Enable debug logging for troubleshooting

---

**Status**: ✅ Active Development | 🚀 Production Ready | 🛡️ Secure | 🧹 Clean Architecture

---

## 🎯 **Project Statistics**

- **Total Java Files**: 93 (including main application and test)
- **Controllers**: 7 (REST endpoints with complete CRUD operations)
- **Service Implementations**: 8 (comprehensive business logic with transaction management)
- **JPA Entities**: 9 (with proper inheritance, relationships, and auditing)
- **Exception Handlers**: 7 (domain-specific with consistent error formatting)
- **Custom Exceptions**: 9 (granular exception handling for all edge cases)
- **DTOs**: 14 (7 request, 7 response objects with validation)
- **Repositories**: 8 (JPA data access with query methods)
- **Mappers**: 7 (object mapping for clean data transformation)
- **Utility Classes**: 4 (response building and error handling utilities)
- **Security Components**: 4 (JWT auth, user details, filters, and config)
- **Configuration Classes**: 2 (health checks and auditor)
