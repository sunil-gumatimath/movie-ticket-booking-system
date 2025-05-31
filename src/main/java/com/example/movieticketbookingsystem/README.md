# Exception Handling Architecture

This document provides detailed technical documentation for the exception handling system in the Movie Ticket Booking System API.

> 📖 **For general project information, setup instructions, and API documentation, see the main**: [`README.md`](../../../../../README.md)

## Table of Contents
- [Overview](#overview)
- [Exception Handler Structure](#exception-handler-structure)
- [Error Response Formats](#error-response-formats)
- [Exception Classes](#exception-classes)
- [Usage Examples](#usage-examples)
- [Handler Architecture](#handler-architecture)
- [Benefits](#benefits)
- [Recent Cleanup](#recent-cleanup-2024)

## Overview

The exception handling system is designed with the following principles:
- **Separation of Concerns**: Each domain has its own exception handler
- **Consistent Error Responses**: All errors follow a standard format using RestResponseBuilder
- **Specific Exception Classes**: Domain-specific exceptions for better error handling
- **Proper HTTP Status Codes**: Each exception maps to appropriate HTTP status codes
- **Clean Architecture**: Removed redundant and generic exception classes
- **RestResponseBuilder Pattern**: All handlers use consistent response building

## Exception Handler Structure

### 1. UserExceptionHandler
Handles user-related exceptions:
- `UserNotFoundByEmailException` → 404 NOT_FOUND
- `UserExistByEmailException` → 409 CONFLICT
- `UserNotRegistered` → 401 UNAUTHORIZED

### 2. MovieExceptionHandler
Handles movie-related exceptions:
- `MovieNotFoundByIdException` → 404 NOT_FOUND

### 3. TheaterExceptionHandler
Handles theater-related exceptions:
- `TheaterOwnerIdException` → 404 NOT_FOUND

### 4. ScreenExceptionHandler
Handles screen-related exceptions:
- `ScreenIdNotFoundException` → 404 NOT_FOUND

### 5. SecurityExceptionHandler
Handles security and authentication exceptions:
- `BadCredentialsException` → 401 UNAUTHORIZED
- `AuthenticationException` → 401 UNAUTHORIZED
- `AccessDeniedException` → 403 FORBIDDEN
- `DisabledException` → 401 UNAUTHORIZED
- `LockedException` → 401 UNAUTHORIZED

### 6. ValidationExceptionHandler
Handles validation-related exceptions:
- `MethodArgumentNotValidException` → 400 BAD_REQUEST
- Returns detailed field-level validation errors

### 7. GeneralExceptionHandler
Handles general application exceptions (lowest priority):
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
  "timestamp": "2024-01-15T10:30:45"
}
```

### Validation Error Response
```json
{
  "statusCode": 400,
  "error_message": "Validation failed for one or more fields",
  "timestamp": "2024-01-15T10:30:45",
  "data": [
    {
      "field": "email",
      "rejectedValue": "invalid-email",
      "errorMessage": "Please provide a valid email address"
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

### Current Exception Classes:
- `UserNotFoundByEmailException`
- `UserExistByEmailException`
- `UserNotRegistered`
- `MovieNotFoundByIdException`
- `TheaterOwnerIdException`
- `ScreenIdNotFoundException`

## Usage Examples

### Throwing Exceptions in Service Layer
```java
// User not found
throw new UserNotFoundByEmailException("User not found with email: " + email);

// Movie not found
throw new MovieNotFoundByIdException("Movie not found with ID: " + movieId);

// Theater owner not found
throw new TheaterOwnerIdException("Theater owner not found with ID: " + ownerId);

// Screen not found
throw new ScreenIdNotFoundException("Screen not found with ID: " + screenId);
```

### Handler Architecture
All exception handlers follow consistent patterns:
- Use `@RestControllerAdvice` annotation
- Use `@AllArgsConstructor` for dependency injection
- Use `RestResponseBuilder` field named `responseBuilder`
- Use `@ExceptionHandler` methods without explicit exception class parameters

## Benefits

1. **Clean Architecture**: Removed redundant and generic exception classes
2. **Maintainability**: Each domain's exceptions are handled separately
3. **Consistency**: All error responses follow the same format using RestResponseBuilder
4. **Specificity**: Domain-specific exceptions provide better error context
5. **User Experience**: Clear, meaningful error messages
6. **API Documentation**: Predictable error response structure
7. **Security**: Sensitive information is not exposed in error messages

## Recent Cleanup (2024)

### Removed Components:
- ❌ `ConflictException` (generic, replaced with specific exceptions)
- ❌ `ResourceNotFoundException` (generic, replaced with specific exceptions)
- ❌ `FieldErrorExceptionHandler` (duplicate validation handler)
- ❌ `GlobalExceptionHandler` (redundant with specific handlers)
- ❌ `LoginExceptionhandler` (broken implementation)

### Current Clean Architecture:
- ✅ Domain-specific exception handlers only
- ✅ Specific exception classes for better error context
- ✅ Consistent RestResponseBuilder usage
- ✅ No redundant or generic exception classes
- ✅ All compilation errors resolved

## Summary

This exception handling system provides:
- **7 Domain-Specific Handlers** for different business areas
- **6 Custom Exception Classes** for specific error scenarios
- **Consistent Error Responses** using RestResponseBuilder pattern
- **Clean Architecture** with no redundant or generic exceptions
- **Comprehensive Coverage** from validation to general application errors

> 🔙 **Return to main project documentation**: [`README.md`](../../../../../README.md)
