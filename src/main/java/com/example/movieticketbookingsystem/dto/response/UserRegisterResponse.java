package com.example.movieticketbookingsystem.dto.response;

import com.example.movieticketbookingsystem.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserRegisterResponse(

        @NotNull(message = "User ID cannot be null")
        @NotBlank(message = "User ID cannot be blank")
        String userId,

        @NotNull(message = "Username cannot be null")
        @NotBlank(message = "Username cannot be blank")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain alphabets, numbers, and underscore")
        String username,

        @NotNull(message = "Email cannot be null")
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email must be valid")
        String email,

        @NotNull(message = "User role cannot be null")
        UserRole userRole
) {}