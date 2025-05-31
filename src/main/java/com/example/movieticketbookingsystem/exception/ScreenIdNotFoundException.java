package com.example.movieticketbookingsystem.exception;

import lombok.Getter;

@Getter
public class ScreenIdNotFoundException extends RuntimeException {

    public ScreenIdNotFoundException(String message) {
        super(message);
    }

    public ScreenIdNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
