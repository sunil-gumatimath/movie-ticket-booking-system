package com.example.movieticketbookingsystem.exception;


import lombok.Getter;

@Getter
public class UserNotRegistered extends  RuntimeException {
    public UserNotRegistered(String message) {
        super(message);
    }
}
