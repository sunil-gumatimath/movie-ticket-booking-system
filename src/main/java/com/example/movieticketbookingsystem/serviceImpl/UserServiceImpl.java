package com.example.movieticketbookingsystem.serviceImpl;

import com.example.movieticketbookingsystem.dto.request.UserRegisterRequest;
import com.example.movieticketbookingsystem.dto.request.UserRequest;
import com.example.movieticketbookingsystem.dto.response.UserRegisterResponse;
import com.example.movieticketbookingsystem.entity.TheaterOwner;
import com.example.movieticketbookingsystem.entity.User;
import com.example.movieticketbookingsystem.entity.UserDetails;
import com.example.movieticketbookingsystem.exception.UserExistByEmailException;
import com.example.movieticketbookingsystem.exception.UserNotRegistered;
import com.example.movieticketbookingsystem.mapper.UserMapper;
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
    private final UserMapper userMapper;

    @Override
    public UserRegisterResponse addUserDetails(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserExistByEmailException("User with Email already exists");
        }

        // Create user entity using mapper
        UserDetails userDetails = userMapper.toEntity(request);

        // Encode password
        userDetails.setPassword(passwordEncoder.encode(userDetails.getPassword()));

        // Save user
        UserDetails savedUser = userRepository.save(userDetails);

        // Return response using mapper
        return userMapper.toResponse(savedUser);
    }
    // This method is no longer needed as we're using the mapper

    @Override
    public UserRegisterResponse updateUser(String email, UserRequest userRequest) {
        UserDetails user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotRegistered("User not found with email "+email));

        user.setUsername(userRequest.username());
        user.setPhoneNumber(userRequest.phoneNumber());
        user.setDateOfBirth(userRequest.dateOfBirth());
        // UpdatedAt will be handled by JPA auditing

        userRepository.save(user);

        return userMapper.toResponse(user);
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
