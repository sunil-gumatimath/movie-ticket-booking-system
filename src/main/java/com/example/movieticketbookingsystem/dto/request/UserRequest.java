package com.example.movieticketbookingsystem.dto.request;

import java.time.LocalDate;

public record UserRequest(
        String username,
        String phoneNumber,
        LocalDate dateOfBirth
) {}
