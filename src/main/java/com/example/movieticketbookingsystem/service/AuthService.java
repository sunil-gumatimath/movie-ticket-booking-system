package com.example.movieticketbookingsystem.service;

import com.example.movieticketbookingsystem.dto.request.LoginRequest;

public interface AuthService {
    String userLogin(LoginRequest loginRequest);
}
