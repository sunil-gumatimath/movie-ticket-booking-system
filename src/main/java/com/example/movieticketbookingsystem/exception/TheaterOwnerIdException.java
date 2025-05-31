package com.example.movieticketbookingsystem.exception;

import lombok.Getter;

@Getter
public class TheaterOwnerIdException extends RuntimeException {

    public TheaterOwnerIdException(String message) {
        super(message);
    }

    public TheaterOwnerIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
