package com.example.movieticketbookingsystem.controller;

import com.example.movieticketbookingsystem.entity.UserDetails;
import com.example.movieticketbookingsystem.serviceImpl.UserServiceImpl;
import com.example.movieticketbookingsystem.utility.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/UserDetails")
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseStructure<UserDetails>> addUserDetails(@RequestBody UserDetails userDetails){

        UserDetails userDetails1 = userService.addUserDetails(userDetails);




        return new ResponseEntity<ResponseStructure<UserDetails>>()
    }
}
