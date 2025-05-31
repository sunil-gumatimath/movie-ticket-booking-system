package com.example.movieticketbookingsystem.mapper;

import com.example.movieticketbookingsystem.dto.request.UserRegisterRequest;
import com.example.movieticketbookingsystem.dto.response.UserRegisterResponse;
import com.example.movieticketbookingsystem.entity.TheaterOwner;
import com.example.movieticketbookingsystem.entity.User;
import com.example.movieticketbookingsystem.entity.UserDetails;
import com.example.movieticketbookingsystem.enums.UserRole;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {

    /**
     * Maps a UserRegisterRequest to a UserDetails entity
     *
     * @param dto The UserRegisterRequest DTO
     * @return A new UserDetails entity (either User or TheaterOwner based on role)
     */
    public UserDetails toEntity(UserRegisterRequest dto) {
        if (dto == null) {
            return null;
        }

        UserDetails userDetails = dto.userRole() == UserRole.USER ? new User() : new TheaterOwner();

        userDetails.setUsername(dto.username());
        userDetails.setEmail(dto.email());
        userDetails.setPassword(dto.password());
        userDetails.setPhoneNumber(dto.phoneNumber());
        userDetails.setDateOfBirth(dto.dateOfBirth());
        userDetails.setUserRole(dto.userRole());
        // CreatedAt and UpdatedAt will be handled by JPA auditing

        return userDetails;
    }

    /**
     * Maps a UserDetails entity to a UserRegisterResponse DTO
     *
     * @param userDetails The UserDetails entity
     * @return A new UserRegisterResponse DTO
     */
    public UserRegisterResponse toResponse(UserDetails userDetails) {
        if (userDetails == null) {
            return null;
        }

        return new UserRegisterResponse(
                userDetails.getUserId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getUserRole()
        );
    }
}
