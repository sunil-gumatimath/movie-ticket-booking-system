package com.example.movieticketbookingsystem.exception;

import lombok.Getter;

@Getter
public class UserNotFoundByEmailException extends RuntimeException {

    public UserNotFoundByEmailException(String message) {
        super(message);
    }

    public UserNotFoundByEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
