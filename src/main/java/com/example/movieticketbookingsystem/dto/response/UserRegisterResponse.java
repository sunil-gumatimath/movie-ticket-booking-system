package com.example.movieticketbookingsystem.dto.response;

import com.example.movieticketbookingsystem.enums.UserRole;

public record UserRegisterResponse(
        String userId,
        String username,
        String email,
        UserRole userRole)
        {}