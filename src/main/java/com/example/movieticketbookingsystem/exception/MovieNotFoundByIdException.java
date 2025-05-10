package com.example.movieticketbookingsystem.exception;

public class MovieNotFoundByIdException extends RuntimeException {
    public MovieNotFoundByIdException(String message) {
        super(message);
    }
}
