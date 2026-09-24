package com.example.movieticketbookingsystem.mapper;

import com.example.movieticketbookingsystem.dto.request.UserRegisterRequest;
import com.example.movieticketbookingsystem.dto.response.UserRegisterResponse;
import com.example.movieticketbookingsystem.entity.User;
import com.example.movieticketbookingsystem.entity.UserDetails;
import com.example.movieticketbookingsystem.enums.UserRole;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    /**
     * Public registration always creates a normal user. Privileged roles are
     * provisioned separately and must never be selected from a public request.
     */
    public UserDetails toEntity(UserRegisterRequest dto, String normalizedEmail) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setUsername(dto.username().trim());
        user.setEmail(normalizedEmail);
        user.setPassword(dto.password());
        user.setPhoneNumber(dto.phoneNumber());
        user.setDateOfBirth(dto.dateOfBirth());
        user.setUserRole(UserRole.ROLE_USER);
        return user;
    }

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
