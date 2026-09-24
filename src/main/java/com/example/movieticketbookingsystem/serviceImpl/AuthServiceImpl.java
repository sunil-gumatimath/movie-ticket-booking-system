package com.example.movieticketbookingsystem.serviceImpl;

import com.example.movieticketbookingsystem.dto.request.LoginRequest;
import com.example.movieticketbookingsystem.entity.UserDetails;
import com.example.movieticketbookingsystem.exception.UserNotFoundByEmailException;
import com.example.movieticketbookingsystem.repository.UserRepository;
import com.example.movieticketbookingsystem.security.jwt.JwtService;
import com.example.movieticketbookingsystem.security.jwt.TokenPayload;
import com.example.movieticketbookingsystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Value("${jwt.expiration:86400000}")
    private long expirationMillis;

    @Override
    public String userLogin(LoginRequest loginRequest) {
        if (expirationMillis <= 0) {
            throw new IllegalStateException("JWT expiration must be positive");
        }
        String normalizedEmail = loginRequest.email().trim().toLowerCase(Locale.ROOT);
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            normalizedEmail,
                            loginRequest.password()
                    )
            );

            // If authentication is successful, generate JWT token
            if (authentication.isAuthenticated()) {
                // Fetch user details
                UserDetails user = userRepository.findByEmail(normalizedEmail)
                        .orElseThrow(() -> new UserNotFoundByEmailException("User not found"));

                // Create claims for the token
                Map<String, Object> claims = new HashMap<>();
                claims.put("userId", user.getUserId());
                claims.put("role", user.getUserRole().name());

                // Create token payload
                Instant now = Instant.now();
                TokenPayload tokenPayload = TokenPayload.builder()
                        .subject(user.getEmail())
                        .claims(claims)
                        .issuedAt(now)
                        .expiration(now.plusMillis(expirationMillis))
                        .build();

                // Generate and return token
                return jwtService.createJwtToken(tokenPayload);
            } else {
                throw new AuthenticationException("Authentication failed") {};
            }
        } catch (AuthenticationException e) {
            throw new AuthenticationException("Invalid email or password") {};
        }
    }
}