package com.example.movieticketbookingsystem.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class JwtService {

    private static final int MIN_HS512_KEY_BYTES = 64;

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey signingKey;

    @PostConstruct
    void initSigningKey() {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("JWT secret must be configured");
        }
        byte[] decoded = Decoders.BASE64.decode(secret);
        if (decoded.length < MIN_HS512_KEY_BYTES) {
            throw new IllegalStateException("JWT secret must be at least 512 bits for HS512");
        }
        signingKey = Keys.hmacShaKeyFor(decoded);
    }

    public String createJwtToken(TokenPayload tokenPayload) {
        return Jwts.builder()
                .claims(tokenPayload.claims())
                .subject(tokenPayload.subject())
                .issuedAt(Date.from(tokenPayload.issuedAt()))
                .expiration(Date.from(tokenPayload.expiration()))
                .signWith(signingKey, Jwts.SIG.HS512)
                .compact();
    }

    /**
     * Verifies the token's signature and expiry and returns its claims, or empty if the
     * token is invalid. Invalid tokens are an expected client condition, so they are
     * logged at debug level rather than as errors.
     */
    public Optional<Claims> parseClaims(String token) {
        try {
            return Optional.of(Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload());
        } catch (JwtException | IllegalArgumentException e) {
            log.debug("Rejected JWT: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
