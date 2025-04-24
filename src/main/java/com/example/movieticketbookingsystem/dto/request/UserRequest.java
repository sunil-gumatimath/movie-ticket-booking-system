package com.example.movieticketbookingsystem.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record UserRequest(
        @NotNull(message = "username cannot be null")
        String username,

        @NotNull(message = "phoneNumber cannot be null")
        @Pattern(regexp = "\\d{10}", message = "phoneNumber must be a valid 10-digit number")
        String phoneNumber,

        @NotNull(message = "dateOfBirth cannot be null")
        @Past(message = "dateOfBirth must be a past date")
        LocalDate dateOfBirth
) {}