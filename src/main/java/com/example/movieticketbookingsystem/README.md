# Exception Handling Architecture

This document describes the comprehensive exception handling system implemented in the Movie Ticket Booking System API.

## Overview

The exception handling system is designed with the following principles:
- **Separation of Concerns**: Each domain has its own exception handler
- **Consistent Error Responses**: All errors follow a standard format
- **Detailed Error Information**: Includes timestamps, paths, and specific error messages
- **Proper HTTP Status Codes**: Each exception maps to appropriate HTTP status codes
- **Logging**: All exceptions are properly logged for debugging

## Exception Handler Structure

### 1. UserExceptionHandler (@Order(1))
Handles user-related exceptions:
- `UserNotFoundByEmailException` → 404 NOT_FOUND
- `UserExistByEmailException` → 409 CONFLICT  
- `UserNotRegistered` → 401 UNAUTHORIZED

### 2. MovieExceptionHandler (@Order(2))
Handles movie-related exceptions:
- `MovieNotFoundByIdException` → 404 NOT_FOUND

### 3. TheaterExceptionHandler (@Order(3))
Handles theater and screen-related exceptions:
- `TheaterOwnerIdException` → 404 NOT_FOUND
- `ScreenIdNotFoundException` → 404 NOT_FOUND

### 4. SecurityExceptionHandler (@Order(4))
Handles security and authentication exceptions:
- `BadCredentialsException` → 401 UNAUTHORIZED
- `AuthenticationException` → 401 UNAUTHORIZED
- `AccessDeniedException` → 403 FORBIDDEN
- `DisabledException` → 401 UNAUTHORIZED
- `LockedException` → 401 UNAUTHORIZED

### 5. ValidationExceptionHandler (@Order(5))
Handles validation-related exceptions:
- `MethodArgumentNotValidException` → 400 BAD_REQUEST
- `ConstraintViolationException` → 400 BAD_REQUEST

### 6. GeneralExceptionHandler (@Order(10))
Handles general application exceptions (lowest priority):
- `ResourceNotFoundException` → 404 NOT_FOUND
- `ConflictException` → 409 CONFLICT
- `DataIntegrityViolationException` → 400 BAD_REQUEST
- `NoHandlerFoundException` → 404 NOT_FOUND
- `HttpRequestMethodNotSupportedException` → 405 METHOD_NOT_ALLOWED
- `MissingServletRequestParameterException` → 400 BAD_REQUEST
- `MethodArgumentTypeMismatchException` → 400 BAD_REQUEST
- `RuntimeException` → 500 INTERNAL_SERVER_ERROR
- `Exception` → 500 INTERNAL_SERVER_ERROR

## Error Response Formats

### Standard Error Response
```json
{
  "statusCode": 404,
  "error_message": "User not found with the provided email",
  "timestamp": "2024-01-15T10:30:45",
  "path": "/api/users/123"
}
```

### Validation Error Response
```json
{
  "statusCode": 400,
  "error_message": "Validation failed for one or more fields",
  "timestamp": "2024-01-15T10:30:45",
  "path": "/api/users",
  "data": [
    {
      "field": "email",
      "rejectedValue": "invalid-email",
      "errorMessage": "Please provide a valid email address",
      "objectName": "userRegisterRequest"
    }
  ]
}
```

## Exception Classes

All custom exception classes follow these patterns:
- Extend `RuntimeException`
- Use proper constructor chaining with `super(message)` and `super(message, cause)`
- Include `@Getter` annotation for accessing message
- Provide both single-parameter and cause-parameter constructors

## Usage Examples

### Throwing Exceptions in Service Layer
```java
// User not found
throw new UserNotFoundByEmailException("User not found with email: " + email);

// Movie not found
throw new MovieNotFoundByIdException("Movie not found with ID: " + movieId);

// Theater owner not found
throw new TheaterOwnerIdException("Theater owner not found with ID: " + ownerId);

// Resource conflict
throw new ConflictException("Email already exists in the system");
```

### Handler Priority
Handlers are ordered using `@Order` annotation:
- Lower numbers = Higher priority
- Specific handlers (1-5) handle domain-specific exceptions
- General handler (10) catches remaining exceptions

## Benefits

1. **Maintainability**: Each domain's exceptions are handled separately
2. **Consistency**: All error responses follow the same format
3. **Debugging**: Comprehensive logging and error details
4. **User Experience**: Clear, meaningful error messages
5. **API Documentation**: Predictable error response structure
6. **Security**: Sensitive information is not exposed in error messages

## Migration Notes

- `FieldErrorExceptionHandler` has been deprecated in favor of `ValidationExceptionHandler`
- All exception handlers now use the updated `ErrorStructure` and `FieldErrorStructure` classes
- The `RestResponseBuilder` has been enhanced with timestamp and path support
