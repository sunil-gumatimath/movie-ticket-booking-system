package com.example.movieticketbookingsystem.exception;

/**
 * The request conflicts with the current state of a resource (duplicate data,
 * overlapping schedule, invalid state transition). Mapped to {@code 409 Conflict}.
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
