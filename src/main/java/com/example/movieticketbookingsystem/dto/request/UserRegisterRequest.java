package com.example.movieticketbookingsystem.dto.request;

import com.example.movieticketbookingsystem.enums.UserRole;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UserRegisterRequest(
//        @NotNull(message = "User name cannot be null")
//        @NotBlank(message = "User name cannot be blank")
//        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain alphabets, numbers, and underscore")
        String username,

//        @NotNull(message = "Email cannot be null")
//        @NotBlank(message = "Email cannot be blank")
//        @Email(regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$", message = "Enter a valid Gmail ID")
        String email,

//        @NotNull(message = "Phone number cannot be null")
//        @NotBlank(message = "Phone number cannot be blank")
//        @Pattern(regexp = "^[7-9]\\d{9}$", message = "Invalid phone number")
        String phoneNumber,

//        @NotNull(message = "Password cannot be null")
//        @NotBlank(message = "Password cannot be blank")
//        @Pattern(
//                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])[A-Za-z\\d@#$%^&+=!]{8,12}$",
//                message = "Password must be 8–12 characters, include upper & lowercase letters, a number, and a special character"
//        )
        String password,

//        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,

//        @NotNull(message = "User role is required")
        UserRole userRole
) {}
