package com.example.movieticketbookingsystem.dto.request;

public record LoginRequest(
        String email,
        String password
) {
}
