package com.example.movieticketbookingsystem.controller;

import com.example.movieticketbookingsystem.dto.request.UserRegisterRequest;
import com.example.movieticketbookingsystem.dto.request.UserRequest;
import com.example.movieticketbookingsystem.dto.response.UserRegisterResponse;
import com.example.movieticketbookingsystem.service.UserService;
import com.example.movieticketbookingsystem.utility.ResponseStructure;
import com.example.movieticketbookingsystem.utility.RestResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RestResponseBuilder restResponseBuilder;

    @PostMapping("/register")
    public ResponseEntity<ResponseStructure<UserRegisterResponse>> registerUser(
            @Valid @RequestBody UserRegisterRequest request) {
        UserRegisterResponse user = userService.registerUser(request);
        return restResponseBuilder.success(HttpStatus.CREATED, "UserDetail Created", user);
    }

    /** The account holder or an admin may update the account. */
    @PutMapping("/users/{userId}")
    public ResponseEntity<ResponseStructure<UserRegisterResponse>> updateUser(
            @PathVariable String userId,
            @Valid @RequestBody UserRequest userRequest) {
        UserRegisterResponse user = userService.updateUser(userId, userRequest);
        return restResponseBuilder.success(HttpStatus.OK, "User profile updated successfully", user);
    }

    /** The account holder or an admin may delete the account. */
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ResponseStructure<Void>> deleteUser(@PathVariable String userId) {
        userService.softDelete(userId);
        return restResponseBuilder.success(HttpStatus.OK, "User account deleted successfully (soft delete).", null);
    }
}
