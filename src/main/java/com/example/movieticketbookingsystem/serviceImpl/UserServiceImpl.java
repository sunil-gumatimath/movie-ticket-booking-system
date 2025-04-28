package com.example.movieticketbookingsystem.serviceImpl;

import com.example.movieticketbookingsystem.dto.request.UserRegisterRequest;
import com.example.movieticketbookingsystem.dto.request.UserRequest;
import com.example.movieticketbookingsystem.dto.response.UserRegisterResponse;
import com.example.movieticketbookingsystem.entity.TheaterOwner;
import com.example.movieticketbookingsystem.entity.User;
import com.example.movieticketbookingsystem.entity.UserDetails;
import com.example.movieticketbookingsystem.exception.UserExistByEmailException;
import com.example.movieticketbookingsystem.exception.UserNotRegistered;
import com.example.movieticketbookingsystem.repository.UserRepository;
import com.example.movieticketbookingsystem.security.SecurityConfig;
import com.example.movieticketbookingsystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserRegisterResponse addUserDetails(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserExistByEmailException("User with Email already exists");
        }

        UserDetails savedUser = switch (request.userRole()) {
            case USER -> saveUser(new User(), request);
            case THEATER_OWNER -> saveUser(new TheaterOwner(), request);
        };

        return new UserRegisterResponse(
                savedUser.getUserId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getUserRole()
        );
    }
    private UserDetails saveUser(UserDetails target, UserRegisterRequest source) {
        target.setUserRole(source.userRole());
        target.setUsername(source.username());
        target.setEmail(source.email());
        target.setPassword(passwordEncoder.encode(source.password()));
        target.setPhoneNumber(source.phoneNumber());
        target.setDateOfBirth(source.dateOfBirth());
        return userRepository.save(target);
    }

    @Override
    public UserRegisterResponse updateUser(String email, UserRequest userRequest) {
        UserDetails user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotRegistered("User not found with email "+email));

        user.setUsername(userRequest.username());
        user.setPhoneNumber(userRequest.phoneNumber());
        user.setDateOfBirth(userRequest.dateOfBirth());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        return new UserRegisterResponse(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getUserRole()
        );
    }

    @Override
    public void softDelete(String email) {
        UserDetails user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotRegistered("User not found with email: " + email));

        if (user.isDeleted()) {
            throw new IllegalStateException("User already deleted.");
        }
        user.setDeleted(true);
        user.setDeletedAt(Instant.now());
        userRepository.save(user);
    }
}
