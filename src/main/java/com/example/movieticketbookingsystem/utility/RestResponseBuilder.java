package com.example.movieticketbookingsystem.utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class RestResponseBuilder {

    public static <T> ResponseEntity<ResponseStructure<T>> success(HttpStatus status, String message, T data) {
        ResponseStructure<T> responseStructure = ResponseStructure.<T>builder()
                .status(status.value())
                .message(message)
                .data(data)
                .build();
        return ResponseEntity.status(status).body(responseStructure);
    }

    public static <T> ResponseEntity<ResponseStructure<T>> created(String message, T data, HttpStatus status) {
        ResponseStructure<T> responseStructure = ResponseStructure.<T>builder()
                .status(status.value())
                .message(message)
                .data(data)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseStructure);
    }

    public static <T> ResponseEntity<ResponseStructure<T>> ok(String message, T data, HttpStatus status) {
        ResponseStructure<T> responseStructure = ResponseStructure.<T>builder()
                .status(status.value())
                .message(message)
                .data(data)
                .build();
        return ResponseEntity.ok(responseStructure);
    }
}
