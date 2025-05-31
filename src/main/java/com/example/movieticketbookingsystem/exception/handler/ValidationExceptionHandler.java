package com.example.movieticketbookingsystem.exception.handler;

import com.example.movieticketbookingsystem.utility.FieldErrorStructure;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<FieldErrorStructure<List<CustomFieldError>>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        
        List<CustomFieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> CustomFieldError.builder()
                        .field(fieldError.getField())
                        .rejectedValue(fieldError.getRejectedValue())
                        .errorMessage(fieldError.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());

        FieldErrorStructure<List<CustomFieldError>> errorResponse = FieldErrorStructure
                .<List<CustomFieldError>>builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .errorMessage("Validation failed for one or more fields")
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .data(fieldErrors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<FieldErrorStructure<List<CustomConstraintViolation>>> handleConstraintViolationException(
            ConstraintViolationException ex) {
        
        List<CustomConstraintViolation> violations = ex.getConstraintViolations()
                .stream()
                .map(violation -> CustomConstraintViolation.builder()
                        .propertyPath(violation.getPropertyPath().toString())
                        .invalidValue(violation.getInvalidValue())
                        .message(violation.getMessage())
                        .build())
                .collect(Collectors.toList());

        FieldErrorStructure<List<CustomConstraintViolation>> errorResponse = FieldErrorStructure
                .<List<CustomConstraintViolation>>builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .errorMessage("Constraint validation failed")
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .data(violations)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @Getter
    @Builder
    public static class CustomFieldError {
        private String field;
        private Object rejectedValue;
        private String errorMessage;
    }

    @Getter
    @Builder
    public static class CustomConstraintViolation {
        private String propertyPath;
        private Object invalidValue;
        private String message;
    }
}
