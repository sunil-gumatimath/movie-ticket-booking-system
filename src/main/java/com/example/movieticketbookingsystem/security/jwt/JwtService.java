package com.example.movieticketbookingsystem.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
@Slf4j
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @PostConstruct
    void validateConfiguration() {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("JWT secret must be configured");
        }
        byte[] decoded = Decoders.BASE64.decode(secret);
        if (decoded.length < 64) {
            throw new IllegalStateException("JWT secret must be at least 512 bits for HS512");
        }
    }

    public String createJwtToken(TokenPayload tokenPayload) {
        return Jwts.builder()
                .setClaims(tokenPayload.claims())
                .setSubject(tokenPayload.subject())
                .setIssuedAt(Date.from(tokenPayload.issuedAt()))
                .setExpiration(Date.from(tokenPayload.expiration()))
                .signWith(getSignatureKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private Key getSignatureKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignatureKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignatureKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}