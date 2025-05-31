package com.example.movieticketbookingsystem.exception;

import lombok.Getter;

@Getter
public class MovieNotFoundByIdException extends RuntimeException {

    public MovieNotFoundByIdException(String message) {
        super(message);
    }

    public MovieNotFoundByIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
