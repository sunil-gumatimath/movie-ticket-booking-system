package com.example.movieticketbookingsystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record UserRequest(
        @NotBlank(message = "username cannot be blank")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain alphabets, numbers, and underscore")
        String username,

        @NotBlank(message = "phoneNumber cannot be blank")
        @Pattern(regexp = "^[7-9]\\d{9}$", message = "phoneNumber must be a valid 10-digit number")
        String phoneNumber,

        @NotNull(message = "dateOfBirth cannot be null")
        @Past(message = "dateOfBirth must be a past date")
        LocalDate dateOfBirth
) {}