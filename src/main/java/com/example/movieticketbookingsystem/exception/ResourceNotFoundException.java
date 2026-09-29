package com.example.movieticketbookingsystem.exception;

/**
 * A requested resource does not exist. Mapped to {@code 404 Not Found}.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, String id) {
        super(resource + " not found with ID: " + id);
    }
}
