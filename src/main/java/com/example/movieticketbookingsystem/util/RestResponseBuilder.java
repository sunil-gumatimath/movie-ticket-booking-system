package com.example.movieticketbookingsystem.util;

import com.example.movieticketbookingsystem.utility.ResponseStructure;
import com.example.movieticketbookingsystem.utility.ErrorStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class RestResponseBuilder{

    public <T> ResponseEntity<ResponseStructure<T>> sucess(HttpStatus statusCode, String message, T data){
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
                .build());
    }

}
