package com.example.movieticketbookingsystem.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record UserRegisterRequest(
        @NotBlank(message = "User name cannot be blank")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain alphabets, numbers, and underscore")
        String username,

        @NotBlank(message = "Email cannot be blank")
        @Email(regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$", message = "Enter a valid Gmail ID")
        String email,

        @NotBlank(message = "Phone number cannot be blank")
        @Pattern(regexp = "^[7-9]\\d{9}$", message = "Invalid phone number")
        String phoneNumber,

        // 64 stays well under BCrypt's 72-byte input limit.
        @NotBlank(message = "Password cannot be blank")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,64}$",
                message = "Password must be 8–64 characters, include upper & lowercase letters, a number, and a special character"
        )
        String password,

        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth
) {}
