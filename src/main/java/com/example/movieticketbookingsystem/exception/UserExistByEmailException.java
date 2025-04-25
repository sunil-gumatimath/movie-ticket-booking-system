package com.example.movieticketbookingsystem.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
public class UserExistByEmailException extends RuntimeException {

    public UserExistByEmailException(String message) {
        super(message);
    }
}
