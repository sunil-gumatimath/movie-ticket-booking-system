package com.example.movieticketbookingsystem.utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class RestResponseBuilder {

    public <T>ResponseEntity<ResponseStructure<T>> success(HttpStatus status,String message, T data){
        ResponseStructure<T> responseStructure = ResponseStructure.<T>builder()
                .status(status.value())
                .message(message)
                .data(data)
                .build();
        return ResponseEntity.status(status).body(responseStructure);
    }
}
