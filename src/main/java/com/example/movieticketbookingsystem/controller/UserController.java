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
    private final RestResponseBuilder restResponseBuilder;

    @PostMapping("/register")
    public ResponseEntity<ResponseStructure<UserRegisterResponse>> addUserDetails(@Valid @RequestBody UserRegisterRequest userDetails) {
        UserRegisterResponse saveDetails = userService.addUserDetails(userDetails);
        return restResponseBuilder.success(HttpStatus.CREATED, "UserDetail Created", saveDetails);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseStructure<UserRegisterResponse>> updateUser(
            @RequestParam String email,
            @Valid @RequestBody UserRequest userRequest){
        UserRegisterResponse updateUser = userService.updateUser(email, userRequest);
        return restResponseBuilder.success(HttpStatus.OK, "User profile updated successfully", updateUser);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseStructure<String>> deleteUser(@RequestParam String email) {
        userService.softDelete(email);
        return restResponseBuilder.success(HttpStatus.OK, "User account deleted successfully (soft delete).", null);
    }
}
