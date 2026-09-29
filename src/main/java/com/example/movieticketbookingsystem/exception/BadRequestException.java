package com.example.movieticketbookingsystem.exception;

/**
 * A request is well-formed but violates a business rule that bean validation
 * cannot express (for example, a screen that does not belong to the given
 * theater). Mapped to {@code 400 Bad Request}.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
