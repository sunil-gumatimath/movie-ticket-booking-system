package com.example.movieticketbookingsystem.utility;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Single place that builds the API's success and error bodies, so controllers,
 * exception handlers, and security handlers all return the same shapes.
 */
@Component
public class RestResponseBuilder {

    public <T> ResponseEntity<ResponseStructure<T>> success(HttpStatus statusCode, String message, T data){
        return ResponseEntity.status(statusCode).body(ResponseStructure.<T>builder()
                .status(statusCode.value())
                .message(message)
                .data(data)
                .build());
    }

    public ResponseEntity<ErrorStructure> error(HttpStatus statusCode, String message, HttpServletRequest request) {
        return ResponseEntity.status(statusCode).body(errorBody(statusCode, message, request.getRequestURI()));
    }

    public ErrorStructure errorBody(HttpStatus statusCode, String message, String path) {
        return ErrorStructure.builder()
                .statusCode(statusCode.value())
                .message(message)
                .timestamp(Instant.now().toString())
                .path(path)
                .build();
    }

    public <T> ResponseEntity<FieldErrorStructure<T>> validationError(String message, T details, HttpServletRequest request) {
        return ResponseEntity.badRequest().body(FieldErrorStructure.<T>builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .errorMessage(message)
                .timestamp(Instant.now().toString())
                .path(request.getRequestURI())
                .data(details)
                .build());
    }
}

