package com.example.movieticketbookingsystem.exception.handler;

import com.example.movieticketbookingsystem.exception.TheaterOwnerIdException;
import com.example.movieticketbookingsystem.exception.TheaterScreenMismatchException;
import com.example.movieticketbookingsystem.utility.ErrorStructure;
import com.example.movieticketbookingsystem.utility.RestResponseBuilder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@AllArgsConstructor
public class TheaterExceptionHandler {

    private final RestResponseBuilder responseBuilder;

    @ExceptionHandler
    public ResponseEntity<ErrorStructure> handleTheaterOwnerIdException(TheaterOwnerIdException ex){
        return responseBuilder.error(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorStructure> handleTheaterScreenMismatchException(TheaterScreenMismatchException ex){
        return responseBuilder.error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
