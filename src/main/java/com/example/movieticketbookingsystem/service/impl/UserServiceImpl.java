package com.example.movieticketbookingsystem.service.impl;

import com.example.movieticketbookingsystem.dto.request.UserRegisterRequest;
import com.example.movieticketbookingsystem.dto.request.UserRequest;
import com.example.movieticketbookingsystem.dto.response.UserRegisterResponse;
import com.example.movieticketbookingsystem.entity.AppUser;
import com.example.movieticketbookingsystem.exception.ConflictException;
import com.example.movieticketbookingsystem.exception.ConstraintViolations;
import com.example.movieticketbookingsystem.exception.ResourceNotFoundException;
import com.example.movieticketbookingsystem.mapper.UserMapper;
import com.example.movieticketbookingsystem.repository.UserRepository;
import com.example.movieticketbookingsystem.security.CurrentUserService;
import com.example.movieticketbookingsystem.service.UserService;
import com.example.movieticketbookingsystem.utility.Emails;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String EMAIL_TAKEN = "User with Email already exists";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public UserRegisterResponse registerUser(UserRegisterRequest request) {
        String email = Emails.normalize(request.email());
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException(EMAIL_TAKEN);
        }

        AppUser user = userMapper.toEntity(request, email);
        user.setPassword(passwordEncoder.encode(request.password()));

        try {
            return userMapper.toResponse(userRepository.saveAndFlush(user));
        } catch (DataIntegrityViolationException exception) {
            // A concurrent registration took the email after the check above.
            if (ConstraintViolations.violates(exception, AppUser.EMAIL_UNIQUE_CONSTRAINT)) {
                throw new ConflictException(EMAIL_TAKEN, exception);
            }
            throw exception;
        }
    }

    @Override
    @Transactional
    public UserRegisterResponse updateUser(String userId, UserRequest userRequest) {
        AppUser user = findActiveUser(userId);
        user.setUsername(userRequest.username().trim());
        user.setPhoneNumber(userRequest.phoneNumber());
        user.setDateOfBirth(userRequest.dateOfBirth());
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public void softDelete(String userId) {
        findActiveUser(userId).softDelete();
    }

    /** Loads the target account after checking the caller may act on it. Deleted accounts are treated as absent. */
    private AppUser findActiveUser(String userId) {
        currentUserService.requireSelfOrAdmin(userId);
        return userRepository.findById(userId)
                .filter(user -> !user.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }
}
