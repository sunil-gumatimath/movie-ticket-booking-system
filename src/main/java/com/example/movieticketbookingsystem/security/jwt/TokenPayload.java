package com.example.movieticketbookingsystem.security.jwt;

import lombok.Builder;

import java.time.Instant;
import java.util.Map;

@Builder
public record TokenPayload(
        Map<String, Object> claims,
        String subject,
        Instant issuedAt,
        Instant expiration
) {}
