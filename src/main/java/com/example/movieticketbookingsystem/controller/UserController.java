package com.example.movieticketbookingsystem.controller;

import com.example.movieticketbookingsystem.dto.request.UserRegisterRequest;
import com.example.movieticketbookingsystem.dto.request.UserRequest;
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
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseStructure<UserRegisterResponse>> addUserDetails(@Valid @RequestBody UserRegisterRequest userDetails) {
        UserRegisterResponse saveDetails = userService.addUserDetails(userDetails);
        return RestResponseBuilder.created("UserDetail Created", saveDetails, 201);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseStructure<UserRegisterResponse>> updateUser(@Valid @RequestParam String email, @RequestBody UserRequest userRequest){
        UserRegisterResponse updateUser = userService.updateUser(email,userRequest);
        return RestResponseBuilder.ok("User profile updated successfully",updateUser,200);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseStructure<String>> deleteUser(@Valid @RequestParam String email) {
        userService.softDelete(email);
        return RestResponseBuilder.ok("User account deleted successfully (soft delete).", null, 200);
    }
}
