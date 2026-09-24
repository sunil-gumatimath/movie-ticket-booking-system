# Exception Handling Architecture

This document describes the current exception handling implementation in the Movie Ticket Booking System API. For setup instructions and endpoint documentation, see the [main project README](../../../../../README.md).

## Overview

The application uses seven `@RestControllerAdvice` components:

- Domain handlers for users, movies, theaters, and screens.
- A security handler.
- A validation handler.
- A general fallback handler.

The domain, security, and general handlers use `RestResponseBuilder` to create `ErrorStructure` responses. The validation handler creates `FieldErrorStructure` directly so it can return field-level details.

## Handler components

### `UserExceptionHandler`

- `UserNotFoundByEmailException` → `404 Not Found`
- `UserExistByEmailException` → `409 Conflict`
- `UserNotRegistered` → `401 Unauthorized`

### `MovieExceptionHandler`

- `MovieNotFoundByIdException` → `404 Not Found`

### `TheaterExceptionHandler`

- `TheaterOwnerIdException` → `404 Not Found`
- `TheaterScreenMismatchException` → `400 Bad Request`

### `ScreenExceptionHandler`

- `ScreenIdNotFoundException` → `404 Not Found`

### `SecurityExceptionHandler`

- `BadCredentialsException` → `401 Unauthorized`
- `AuthenticationException` → `401 Unauthorized`
- `DisabledException` and `LockedException` → `401 Unauthorized`
- `AccessDeniedException` → `403 Forbidden`

### `ValidationExceptionHandler`

- `MethodArgumentNotValidException` → `400 Bad Request`
- `ConstraintViolationException` → `400 Bad Request`
- Returns field-level or constraint-level validation details.

### `GeneralExceptionHandler`

- `ConflictException` → `409 Conflict`
- `IllegalArgumentException` → `400 Bad Request`
- `IllegalStateException` → `409 Conflict`
- Duplicate/unique `DataIntegrityViolationException` → `409 Conflict`
- Other `DataIntegrityViolationException` → `400 Bad Request`
- `NoHandlerFoundException` → `404 Not Found`
- `HttpRequestMethodNotSupportedException` → `405 Method Not Allowed`
- `MissingServletRequestParameterException` → `400 Bad Request`
- `MethodArgumentTypeMismatchException` → `400 Bad Request`
- Unhandled `RuntimeException` → `500 Internal Server Error`
- Unhandled `Exception` → `500 Internal Server Error`

## Error response formats

### Standard application error

```json
{
  "statusCode": 404,
  "error_message": "User not found with the provided email",
  "timestamp": "2026-09-24T12:00:00",
  "path": null
}
```

The `error_message` property is intentionally named that way by the JSON serialization configuration. The current response builder populates the status code, message, and timestamp. It does not currently populate the request path, so `path` is normally `null`.

### Validation error

```json
{
  "statusCode": 400,
  "error_message": "Validation failed for one or more fields",
  "timestamp": "2026-09-24T12:00:00",
  "path": null,
  "data": [
    {
      "field": "email",
      "rejectedValue": "invalid-email",
      "errorMessage": "Enter a valid Gmail ID"
    }
  ]
}
```

Constraint violations use the same outer structure, but each item in `data` contains `propertyPath`, `invalidValue`, and `message`.

## Custom exception classes

The project currently contains eight custom exception classes:

- `UserNotFoundByEmailException`
- `UserExistByEmailException`
- `UserNotRegistered`
- `MovieNotFoundByIdException`
- `TheaterOwnerIdException`
- `ScreenIdNotFoundException`
- `TheaterScreenMismatchException`
- `ConflictException`

These classes represent the current domain and request-state errors. There is no `ResourceNotFoundException` class in the source tree.

## Service-layer usage examples

```java
throw new UserNotFoundByEmailException("User not found with email: " + email);
throw new MovieNotFoundByIdException("Movie not found with ID: " + movieId);
throw new TheaterOwnerIdException("Theater not found with ID: " + theaterId);
throw new ScreenIdNotFoundException("Screen not found with ID: " + screenId);
throw new TheaterScreenMismatchException(
        "Screen does not belong to the specified theater");
throw new ConflictException(
        "The selected time slot is already occupied for this screen");
```

Use `IllegalArgumentException` for invalid request values and `IllegalStateException` for invalid state transitions when no more specific domain exception applies. The general handler maps these to `400` and `409` respectively.

## Response construction

Most handlers use this pattern:

```java
@RestControllerAdvice
@AllArgsConstructor
public class UserExceptionHandler {

    private final RestResponseBuilder responseBuilder;

    @ExceptionHandler(UserNotFoundByEmailException.class)
    public ResponseEntity<ErrorStructure> handleUserNotFound(
            UserNotFoundByEmailException exception) {
        return responseBuilder.error(HttpStatus.NOT_FOUND, exception.getMessage());
    }
}
```

`ValidationExceptionHandler` is different: it builds a `FieldErrorStructure` containing the validation error list and does not inject `RestResponseBuilder`.

## Status-code summary

- `200` — successful reads, updates, login, and deletion.
- `201` — successful resource creation.
- `400` — validation, illegal-argument, missing-parameter, or type-mismatch errors.
- `401` — authentication failures.
- `403` — insufficient permissions or resource ownership failures.
- `404` — missing resources or routes.
- `405` — unsupported HTTP method.
- `409` — duplicate resources, invalid state, or scheduling conflicts.
- `500` — unhandled application errors.

> **Return to [main project documentation](../../../../../README.md).**
