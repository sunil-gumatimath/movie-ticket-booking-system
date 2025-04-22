package com.example.movieticketbookingsystem.mapper;

import com.example.movieticketbookingsystem.dto.request.UserRegisterRequest;
import com.example.movieticketbookingsystem.entity.TheaterOwner;
import com.example.movieticketbookingsystem.entity.User;
import com.example.movieticketbookingsystem.entity.UserDetails;
import com.example.movieticketbookingsystem.enums.UserRole;

import java.time.LocalDateTime;

public class UserMapper {

    public static UserDetails toEntity(UserRegisterRequest dto) {
        UserDetails userDetails = dto.userRole() == UserRole.USER ? new User() : new TheaterOwner();

        userDetails.setUsername(dto.username());
        userDetails.setEmail(dto.email());
        userDetails.setPassword(dto.password());
        userDetails.setPhoneNumber(dto.phoneNumber());
        userDetails.setDateOfBirth(dto.dateOfBirth());
        userDetails.setUserRole(dto.userRole());
//        userDetails.setCreatedAt(LocalDateTime.now());
//        userDetails.setUpdatedAt(LocalDateTime.now());

        return userDetails;
    }
}
