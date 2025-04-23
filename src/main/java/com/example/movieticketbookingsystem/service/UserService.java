package com.example.movieticketbookingsystem.service;

import com.example.movieticketbookingsystem.dto.request.UserRegisterRequest;
import com.example.movieticketbookingsystem.dto.response.UserRegisterResponse;

public interface UserService {
    UserRegisterResponse addUserDetails(UserRegisterRequest userDetails);
}
