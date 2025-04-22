package com.example.movieticketbookingsystem.service;

import com.example.movieticketbookingsystem.dto.request.UserRegisterRequest;
import com.example.movieticketbookingsystem.entity.UserDetails;

public interface UserService {
    UserDetails addUserDetails(UserRegisterRequest userDetails);
}
