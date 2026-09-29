package com.example.movieticketbookingsystem.mapper;

import com.example.movieticketbookingsystem.dto.request.UserRegisterRequest;
import com.example.movieticketbookingsystem.dto.response.UserRegisterResponse;
import com.example.movieticketbookingsystem.entity.User;
import com.example.movieticketbookingsystem.entity.AppUser;
import com.example.movieticketbookingsystem.enums.UserRole;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    /**
     * Public registration always creates a normal user. Privileged roles are
     * provisioned separately and must never be selected from a public request.
     * The password is left unset; the caller stores the encoded hash.
     */
    public AppUser toEntity(UserRegisterRequest dto, String normalizedEmail) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setUsername(dto.username().trim());
        user.setEmail(normalizedEmail);
        user.setPhoneNumber(dto.phoneNumber());
        user.setDateOfBirth(dto.dateOfBirth());
        user.setUserRole(UserRole.ROLE_USER);
        return user;
    }

    public UserRegisterResponse toResponse(AppUser user) {
        if (user == null) {
            return null;
        }

        return new UserRegisterResponse(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getUserRole()
        );
    }
}
