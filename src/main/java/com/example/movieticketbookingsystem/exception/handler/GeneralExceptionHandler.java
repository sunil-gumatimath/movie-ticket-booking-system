package com.example.movieticketbookingsystem.exception.handler;

import com.example.movieticketbookingsystem.exception.ConflictException;
import com.example.movieticketbookingsystem.utility.ErrorStructure;
import com.example.movieticketbookingsystem.utility.RestResponseBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
@AllArgsConstructor
public class GeneralExceptionHandler {

    private final RestResponseBuilder responseBuilder;

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorStructure> handleConflictException(ConflictException exception) {
        return responseBuilder.error(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorStructure> handleIllegalArgumentException(IllegalArgumentException exception) {
        return responseBuilder.error(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorStructure> handleIllegalStateException(IllegalStateException exception) {
        return responseBuilder.error(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorStructure> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        String message = exception.getMessage() == null ? "" : exception.getMessage().toLowerCase();
        if (message.contains("duplicate") || message.contains("unique")) {
            return responseBuilder.error(HttpStatus.CONFLICT, "The resource already exists");
        }
        log.warn("Data integrity violation", exception);
        return responseBuilder.error(HttpStatus.BAD_REQUEST, "The request violates a data constraint");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorStructure> handleNoHandlerFoundException(NoHandlerFoundException exception) {
        return responseBuilder.error(HttpStatus.NOT_FOUND,
                String.format("No handler found for %s %s", exception.getHttpMethod(), exception.getRequestURL()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorStructure> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception) {
        return responseBuilder.error(HttpStatus.METHOD_NOT_ALLOWED,
                String.format("HTTP method '%s' is not supported for this endpoint", exception.getMethod()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorStructure> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException exception) {
        return responseBuilder.error(HttpStatus.BAD_REQUEST,
                String.format("Required parameter '%s' of type '%s' is missing",
                        exception.getParameterName(), exception.getParameterType()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorStructure> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException exception) {
        return responseBuilder.error(HttpStatus.BAD_REQUEST,
                String.format("Invalid value '%s' for parameter '%s'. Expected type: %s",
                        exception.getValue(), exception.getName(), exception.getRequiredType().getSimpleName()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorStructure> handleRuntimeException(RuntimeException exception) {
        log.error("Unhandled runtime exception", exception);
        return responseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorStructure> handleException(Exception exception) {
        log.error("Unhandled exception", exception);
        return responseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }
}
