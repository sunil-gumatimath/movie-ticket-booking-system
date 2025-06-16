package com.example.movieticketbookingsystem.controller;

import com.example.movieticketbookingsystem.dto.request.LoginRequest;
import com.example.movieticketbookingsystem.service.AuthService;
import com.example.movieticketbookingsystem.utility.ResponseStructure;
import com.example.movieticketbookingsystem.utility.RestResponseBuilder;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RestResponseBuilder restResponseBuilder;

    @PostMapping("/login")
    public ResponseEntity<ResponseStructure<String>> login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = authService.userLogin(loginRequest);
        return restResponseBuilder.success(HttpStatus.OK, "Login successful", token);
    }
}
