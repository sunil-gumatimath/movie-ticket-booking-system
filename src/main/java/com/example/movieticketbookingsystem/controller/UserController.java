package com.example.movieticketbookingsystem.controller;

import com.example.movieticketbookingsystem.dto.request.UserRegisterRequest;
import com.example.movieticketbookingsystem.dto.response.UserRegisterResponse;
import com.example.movieticketbookingsystem.entity.UserDetails;
import com.example.movieticketbookingsystem.mapper.UserMapper;
import com.example.movieticketbookingsystem.serviceImpl.UserServiceImpl;
import com.example.movieticketbookingsystem.utility.ResponseStructure;
import com.example.movieticketbookingsystem.utility.RestResponseBuilder;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseStructure<UserRegisterResponse>> addUserDetails(@Valid @RequestBody UserRegisterRequest userDetails) {
        UserRegisterResponse saveDetails = userService.addUserDetails(userDetails);
        return RestResponseBuilder.created("UserDetail Created", saveDetails, HttpStatus.CREATED.value());
    }
}
