package com.example.movieticketbookingsystem.dto.response;

import com.example.movieticketbookingsystem.enums.UserRole;

public class UserRegisterResponse {
    Long userId;
    String username;
    String email;
    UserRole userRole;
}