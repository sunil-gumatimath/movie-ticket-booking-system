package com.example.movieticketbookingsystem.controller;

import com.example.movieticketbookingsystem.entity.UserDetails;
import com.example.movieticketbookingsystem.serviceImpl.UserServiceImpl;
import com.example.movieticketbookingsystem.utility.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/user-details")
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseStructure<Object>> addUserDetails(@RequestBody UserDetails userDetails){

    UserDetails userDetails1 = userService.addUserDetails(userDetails);

    ResponseStructure<Object> responseStructure = ResponseStructure.builder().status(HttpStatus.CREATED.value())
            .message("User_Details Object Created").data(userDetails1).build();

    return ResponseEntity.status(HttpStatus.CREATED).body(responseStructure);
    }
}
