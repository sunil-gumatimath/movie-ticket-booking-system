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
  - MapStruct for object mapping
  - Validation API for input validation

## 📁 Project Structure

```
src/main/java/com/example/movieticketbookingsystem/
├── config/                 # Configuration classes
├── controller/             # REST controllers
├── dto/                    # Data Transfer Objects
│   ├── request/           # Request DTOs
│   └── response/          # Response DTOs
├── entity/                # JPA entities
├── exception/             # Custom exceptions and handlers
│   └── handler/          # Exception handlers
├── mapper/                # Object mappers
├── repository/            # JPA repositories
├── security/              # Security configuration and JWT
├── service/               # Service interfaces
├── serviceImpl/           # Service implementations
└── utility/               # Utility classes
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
   - Create a MySQL database
   - Update `application.properties` with your database credentials:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/movie_booking_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
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

## 📚 API Endpoints

### Authentication
- `POST /register` - User registration
- `POST /login` - User login

### Movies
- `GET /movies/{movieId}` - Get movie details

### Theaters & Screens
- `POST /screen?theaterId={id}` - Add screen to theater
- `GET /screen/{screenId}` - Get screen details

### Shows
- `POST /shows` - Create new show

### Feedback
- `POST /movies/{movieId}/feedback` - Add movie feedback
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
- `MovieNotFoundByIdException` → 404 NOT_FOUND
- `TheaterOwnerIdException` → 404 NOT_FOUND
- `ScreenIdNotFoundException` → 404 NOT_FOUND

📖 **For detailed exception handling documentation, see**: [`src/main/java/com/example/movieticketbookingsystem/README.md`](src/main/java/com/example/movieticketbookingsystem/README.md)

## 🔒 Security

- JWT-based authentication
- Role-based access control (USER, THEATER_OWNER)
- Password encryption using BCrypt
- Stateless session management

## 📊 Recent Updates (2024)

### Exception System Cleanup
- ✅ Removed redundant exception classes (`ConflictException`, `ResourceNotFoundException`)
- ✅ Removed duplicate exception handlers
- ✅ Fixed all compilation errors
- ✅ Implemented domain-specific exception handling
- ✅ Consistent RestResponseBuilder usage across all handlers

### Code Quality Improvements
- ✅ All service implementations use appropriate specific exceptions
- ✅ Clean architecture with separation of concerns
- ✅ Consistent error response format
- ✅ Proper bean configuration and health checks

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
- [📊 Recent Updates](#recent-updates-2024)

## 📞 Support

For support and questions, please open an issue in the repository.

---

**Status**: ✅ Active Development | 🚀 Production Ready | 🛡️ Secure | 🧪 Well Tested
