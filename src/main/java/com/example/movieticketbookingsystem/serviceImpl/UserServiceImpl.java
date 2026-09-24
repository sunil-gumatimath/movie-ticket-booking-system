package com.example.movieticketbookingsystem.serviceImpl;

import com.example.movieticketbookingsystem.dto.request.UserRegisterRequest;
import com.example.movieticketbookingsystem.dto.request.UserRequest;
import com.example.movieticketbookingsystem.dto.response.UserRegisterResponse;
import com.example.movieticketbookingsystem.entity.UserDetails;
import com.example.movieticketbookingsystem.enums.UserRole;
import com.example.movieticketbookingsystem.exception.UserExistByEmailException;
import com.example.movieticketbookingsystem.exception.UserNotRegistered;
import com.example.movieticketbookingsystem.mapper.UserMapper;
import com.example.movieticketbookingsystem.repository.UserRepository;
import com.example.movieticketbookingsystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserRegisterResponse addUserDetails(UserRegisterRequest request) {
        if (request == null || request.email() == null || request.email().isBlank()) {
            throw new IllegalArgumentException("Registration email is required");
        }
        String normalizedEmail = normalizeEmail(request.email());
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new UserExistByEmailException("User with Email already exists");
        }

        UserDetails userDetails = userMapper.toEntity(request, normalizedEmail);
        userDetails.setPassword(passwordEncoder.encode(userDetails.getPassword()));

        try {
            UserDetails savedUser = userRepository.saveAndFlush(userDetails);
            return userMapper.toResponse(savedUser);
        } catch (DataIntegrityViolationException exception) {
            if (isEmailUniqueViolation(exception)) {
                throw new UserExistByEmailException("User with Email already exists");
            }
            throw exception;
        }
    }

    @Override
    @Transactional
    public UserRegisterResponse updateUser(String email, UserRequest userRequest) {
        if (userRequest == null || userRequest.username() == null || userRequest.username().isBlank()
                || userRequest.phoneNumber() == null || !userRequest.phoneNumber().matches("^[7-9]\\d{9}$")
                || userRequest.dateOfBirth() == null) {
            throw new IllegalArgumentException("Valid username, phone number, and date of birth are required");
        }
        requireSelfOrAdmin(email);
        UserDetails user = findUserByEmail(email);

        if (user.isDeleted()) {
            throw new IllegalStateException("Deleted users cannot be updated");
        }

        user.setUsername(userRequest.username().trim());
        user.setPhoneNumber(userRequest.phoneNumber());
        user.setDateOfBirth(userRequest.dateOfBirth());

        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public void softDelete(String email) {
        requireSelfOrAdmin(email);
        UserDetails user = findUserByEmail(email);

        if (user.isDeleted()) {
            throw new IllegalStateException("User already deleted.");
        }

        user.softDelete();
        userRepository.save(user);
    }

    private boolean isEmailUniqueViolation(DataIntegrityViolationException exception) {
        String message = exceptionMessage(exception);
        return message.contains("uk_user_details_email")
                || ((message.contains("duplicate") || message.contains("unique"))
                && message.contains("email"));
    }

    private String exceptionMessage(Throwable exception) {
        StringBuilder message = new StringBuilder();
        Throwable current = exception;
        while (current != null) {
            if (current.getMessage() != null) {
                message.append(' ').append(current.getMessage().toLowerCase());
            }
            current = current.getCause();
        }
        return message.toString();
    }

    private UserDetails findUserByEmail(String email) {
        return userRepository.findByEmail(normalizeEmail(email))
                .orElseThrow(() -> new UserNotRegistered("User not found with email: " + email));
    }

    private void requireSelfOrAdmin(String targetEmail) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Authentication is required");
        }

        String currentEmail = normalizeEmail(authentication.getName());
        if (currentEmail.equals(normalizeEmail(targetEmail))) {
            return;
        }

        UserDetails currentUser = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new AccessDeniedException("Current user is not registered"));
        if (currentUser.getUserRole() != UserRole.ROLE_ADMIN) {
            throw new AccessDeniedException("You may only modify your own account");
        }
    }

    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
