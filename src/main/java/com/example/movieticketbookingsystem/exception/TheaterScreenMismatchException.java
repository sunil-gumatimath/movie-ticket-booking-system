package com.example.movieticketbookingsystem.exception;

import lombok.Getter;

@Getter
public class TheaterScreenMismatchException extends RuntimeException {

    public TheaterScreenMismatchException(String message) {
        super(message);
    }

    public TheaterScreenMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
