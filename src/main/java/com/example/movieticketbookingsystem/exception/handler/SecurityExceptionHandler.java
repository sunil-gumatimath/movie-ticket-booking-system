package com.example.movieticketbookingsystem.exception.handler;

import com.example.movieticketbookingsystem.utility.ErrorStructure;
import com.example.movieticketbookingsystem.utility.RestResponseBuilder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@AllArgsConstructor
public class SecurityExceptionHandler {

    private final RestResponseBuilder responseBuilder;

    @ExceptionHandler
    public ResponseEntity<ErrorStructure> handleBadCredentialsException(BadCredentialsException ex){
        return responseBuilder.error(HttpStatus.UNAUTHORIZED, "Invalid username or password");
    }

    @ExceptionHandler
    public ResponseEntity<ErrorStructure> handleAuthenticationException(AuthenticationException ex){
        String errorMessage = "Authentication failed";
        if (ex instanceof DisabledException) {
            errorMessage = "Your account has been disabled";
        } else if (ex instanceof LockedException) {
            errorMessage = "Your account has been locked";
        }
        return responseBuilder.error(HttpStatus.UNAUTHORIZED, errorMessage);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorStructure> handleAccessDeniedException(AccessDeniedException ex){
        return responseBuilder.error(HttpStatus.FORBIDDEN, "Access denied. You don't have permission to access this resource");
    }
}
