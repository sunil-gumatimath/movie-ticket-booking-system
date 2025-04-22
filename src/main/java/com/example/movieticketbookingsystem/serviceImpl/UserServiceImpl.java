package com.example.movieticketbookingsystem.serviceImpl;

import com.example.movieticketbookingsystem.dto.request.UserRegisterRequest;
import com.example.movieticketbookingsystem.entity.TheaterOwner;
import com.example.movieticketbookingsystem.entity.User;
import com.example.movieticketbookingsystem.entity.UserDetails;
import com.example.movieticketbookingsystem.enums.UserRole;
import com.example.movieticketbookingsystem.exception.UserExistByEmailException;
import com.example.movieticketbookingsystem.repository.UserRepository;
import com.example.movieticketbookingsystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetails addUserDetails(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserExistByEmailException("User with Email already exists");
        }

        return switch (request.userRole()) {
            case USER -> saveUser(new User(), request);
            case THEATER_OWNER -> saveUser(new TheaterOwner(), request);
        };
    }

    private UserDetails saveUser(UserDetails target, UserRegisterRequest source) {
        target.setUserRole(source.userRole());
        target.setUsername(source.username());
        target.setEmail(source.email());
        target.setPassword(source.password());
        target.setPhoneNumber(source.phoneNumber());
        target.setDateOfBirth(source.dateOfBirth());
//        target.setCreatedAt(LocalDateTime.now());
//        target.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(target);
    }
}
