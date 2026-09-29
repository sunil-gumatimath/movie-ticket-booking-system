package com.example.movieticketbookingsystem.service.impl;

import com.example.movieticketbookingsystem.dto.request.LoginRequest;
import com.example.movieticketbookingsystem.entity.AppUser;
import com.example.movieticketbookingsystem.repository.UserRepository;
import com.example.movieticketbookingsystem.security.jwt.JwtService;
import com.example.movieticketbookingsystem.security.jwt.TokenPayload;
import com.example.movieticketbookingsystem.service.AuthService;
import com.example.movieticketbookingsystem.utility.Emails;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Value("${jwt.expiration:86400000}")
    private long expirationMillis;

    @PostConstruct
    void validateConfiguration() {
        if (expirationMillis <= 0) {
            throw new IllegalStateException("JWT expiration must be positive");
        }
    }

    @Override
    public String userLogin(LoginRequest loginRequest) {
        String email = Emails.normalize(loginRequest.email());

        // Throws an AuthenticationException (401) for unknown, deleted, or wrong-password accounts.
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, loginRequest.password()));

        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        Instant now = Instant.now();
        return jwtService.createJwtToken(TokenPayload.builder()
                .subject(user.getEmail())
                .claims(Map.of("userId", user.getUserId(), "role", user.getUserRole().name()))
                .issuedAt(now)
                .expiration(now.plusMillis(expirationMillis))
                .build());
    }
}
