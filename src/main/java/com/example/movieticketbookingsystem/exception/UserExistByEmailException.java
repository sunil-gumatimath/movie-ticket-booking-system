package com.example.movieticketbookingsystem.exception;

import lombok.Getter;

@Getter
public class UserExistByEmailException extends RuntimeException {

    public UserExistByEmailException(String message) {
        super(message);
    }

    public UserExistByEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
