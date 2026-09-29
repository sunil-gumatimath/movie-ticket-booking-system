package com.example.movieticketbookingsystem.service;

import com.example.movieticketbookingsystem.dto.request.UserRegisterRequest;
import com.example.movieticketbookingsystem.dto.request.UserRequest;
import com.example.movieticketbookingsystem.dto.response.UserRegisterResponse;

public interface UserService {

    UserRegisterResponse registerUser(UserRegisterRequest request);

    UserRegisterResponse updateUser(String userId, UserRequest userRequest);

    void softDelete(String userId);
}
