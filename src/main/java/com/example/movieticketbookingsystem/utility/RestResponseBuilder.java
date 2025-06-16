package com.example.movieticketbookingsystem.utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class RestResponseBuilder {

    public <T> ResponseEntity<ResponseStructure<T>> success(HttpStatus statusCode, String message, T data){
        return ResponseEntity.status(statusCode).body(ResponseStructure.<T>builder()
                .status(statusCode.value())
                .message(message)
                .data(data)
                .build());
    }

    public ResponseEntity<ErrorStructure> error(HttpStatus statusCode, String message){
        return ResponseEntity.status(statusCode).body(ErrorStructure.builder()
                .statusCode(statusCode.value())
                .message(message)
                .timestamp(java.time.LocalDateTime.now().toString())
                .build());
    }

    // Overloaded method for error with URI (used by GeneralExceptionHandler)
    public ResponseEntity<ErrorStructure> error(HttpStatus statusCode, String message, String requestUri){
        return ResponseEntity.status(statusCode).body(ErrorStructure.builder()
                .statusCode(statusCode.value())
                .message(message)
                .timestamp(java.time.LocalDateTime.now().toString())
                .path(requestUri)
                .build());
    }
}

