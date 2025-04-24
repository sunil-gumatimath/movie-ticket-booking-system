package com.example.movieticketbookingsystem.utility;

import com.example.movieticketbookingsystem.exception.UserExistByEmailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorStructure<String>> handleUserExistByEmail(UserExistByEmailException e){
        ErrorStructure<String> errorStructure = ErrorStructure.<String>builder()
                .type("User Exit")
                .status(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(errorStructure,HttpStatus.NOT_FOUND);
    }

}
