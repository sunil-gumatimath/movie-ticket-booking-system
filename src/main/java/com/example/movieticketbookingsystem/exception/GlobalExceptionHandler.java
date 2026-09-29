package com.example.movieticketbookingsystem.exception;

import com.example.movieticketbookingsystem.utility.ErrorStructure;
import com.example.movieticketbookingsystem.utility.FieldErrorStructure;
import com.example.movieticketbookingsystem.utility.RestResponseBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.Locale;

/**
 * The application's only {@code @RestControllerAdvice}. Keeping every handler in one
 * class guarantees Spring picks the most specific handler for an exception; with several
 * advice classes, the first advice that has <em>any</em> matching handler wins, which let
 * the catch-all {@code Exception} handler shadow domain handlers.
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final RestResponseBuilder responseBuilder;

    // --- Domain errors ---

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorStructure> handleNotFound(ResourceNotFoundException exception, HttpServletRequest request) {
        return responseBuilder.error(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorStructure> handleBadRequest(BadRequestException exception, HttpServletRequest request) {
        return responseBuilder.error(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorStructure> handleConflict(ConflictException exception, HttpServletRequest request) {
        return responseBuilder.error(HttpStatus.CONFLICT, exception.getMessage(), request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorStructure> handleDataIntegrityViolation(DataIntegrityViolationException exception,
                                                                       HttpServletRequest request) {
        log.warn("Data integrity violation on {}", request.getRequestURI(), exception);
        return responseBuilder.error(HttpStatus.CONFLICT, "The request conflicts with existing data", request);
    }

    // --- Request validation ---

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FieldErrorStructure<List<FieldValidationError>>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception, HttpServletRequest request) {
        List<FieldValidationError> fieldErrors = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldValidationError(
                        error.getField(), safeRejectedValue(error.getField(), error.getRejectedValue()), error.getDefaultMessage()))
                .toList();
        return responseBuilder.validationError("Validation failed for one or more fields", fieldErrors, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<FieldErrorStructure<List<ConstraintValidationError>>> handleConstraintViolation(
            ConstraintViolationException exception, HttpServletRequest request) {
        List<ConstraintValidationError> violations = exception.getConstraintViolations().stream()
                .map(violation -> new ConstraintValidationError(
                        violation.getPropertyPath().toString(), violation.getInvalidValue(), violation.getMessage()))
                .toList();
        return responseBuilder.validationError("Constraint validation failed", violations, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorStructure> handleUnreadableBody(HttpMessageNotReadableException exception,
                                                               HttpServletRequest request) {
        return responseBuilder.error(HttpStatus.BAD_REQUEST, "Request body is missing or malformed", request);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorStructure> handleMissingParameter(MissingServletRequestParameterException exception,
                                                                 HttpServletRequest request) {
        return responseBuilder.error(HttpStatus.BAD_REQUEST,
                String.format("Required parameter '%s' of type '%s' is missing",
                        exception.getParameterName(), exception.getParameterType()), request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorStructure> handleTypeMismatch(MethodArgumentTypeMismatchException exception,
                                                             HttpServletRequest request) {
        String expectedType = exception.getRequiredType() == null ? "unknown" : exception.getRequiredType().getSimpleName();
        return responseBuilder.error(HttpStatus.BAD_REQUEST,
                String.format("Invalid value '%s' for parameter '%s'. Expected type: %s",
                        exception.getValue(), exception.getName(), expectedType), request);
    }

    // --- Security (errors raised inside controllers/services, e.g. by @PreAuthorize or login) ---

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorStructure> handleAuthentication(AuthenticationException exception, HttpServletRequest request) {
        String message;
        if (exception instanceof BadCredentialsException) {
            message = "Invalid email or password";
        } else if (exception instanceof DisabledException) {
            message = "Your account has been disabled";
        } else if (exception instanceof LockedException) {
            message = "Your account has been locked";
        } else {
            message = "Authentication failed";
        }
        return responseBuilder.error(HttpStatus.UNAUTHORIZED, message, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorStructure> handleAccessDenied(AccessDeniedException exception, HttpServletRequest request) {
        return responseBuilder.error(HttpStatus.FORBIDDEN,
                "Access denied. You don't have permission to access this resource", request);
    }

    // --- Routing ---

    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    public ResponseEntity<ErrorStructure> handleNoRoute(Exception exception, HttpServletRequest request) {
        return responseBuilder.error(HttpStatus.NOT_FOUND,
                String.format("No endpoint found for %s %s", request.getMethod(), request.getRequestURI()), request);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorStructure> handleMethodNotSupported(HttpRequestMethodNotSupportedException exception,
                                                                   HttpServletRequest request) {
        return responseBuilder.error(HttpStatus.METHOD_NOT_ALLOWED,
                String.format("HTTP method '%s' is not supported for this endpoint", exception.getMethod()), request);
    }

    // --- Fallback ---

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorStructure> handleUnexpected(Exception exception, HttpServletRequest request) {
        log.error("Unhandled exception on {}", request.getRequestURI(), exception);
        return responseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", request);
    }

    /** Never echo secrets back to the client (or into logs that capture responses). */
    private static Object safeRejectedValue(String field, Object rejectedValue) {
        return field.toLowerCase(Locale.ROOT).contains("password") ? null : rejectedValue;
    }

    public record FieldValidationError(String field, Object rejectedValue, String errorMessage) {
    }

    public record ConstraintValidationError(String propertyPath, Object invalidValue, String message) {
    }
}
