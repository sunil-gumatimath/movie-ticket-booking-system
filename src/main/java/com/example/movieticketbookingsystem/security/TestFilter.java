package com.example.movieticketbookingsystem.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
public class TestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .ifPresent(auth -> log.info("Current authenticated user: {}, role: {}",
                        auth.getName(),
                        auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList() ));

        filterChain.doFilter(request, response);
    }
}
