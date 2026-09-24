package com.example.movieticketbookingsystem;

import com.example.movieticketbookingsystem.dto.request.UserRegisterRequest;
import com.example.movieticketbookingsystem.dto.request.UserRequest;
import com.example.movieticketbookingsystem.entity.User;
import com.example.movieticketbookingsystem.entity.UserDetails;
import com.example.movieticketbookingsystem.enums.UserRole;
import com.example.movieticketbookingsystem.mapper.UserMapper;
import com.example.movieticketbookingsystem.repository.UserRepository;
import com.example.movieticketbookingsystem.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final UserServiceImpl service = new UserServiceImpl(userRepository, passwordEncoder, new UserMapper());

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void publicRegistrationAlwaysCreatesNormalUser() {
        UserRegisterRequest request = new UserRegisterRequest(
                "user", "USER@GMAIL.COM", "9876543210", "Password123!", LocalDate.of(1990, 1, 1));

        UserDetails user = new UserMapper().toEntity(request, "user@gmail.com");

        assertThat(user.getUserRole()).isEqualTo(UserRole.ROLE_USER);
    }

    @Test
    void userCannotUpdateAnotherUsersProfile() {
        User target = user("target@gmail.com");
        when(userRepository.findByEmail("target@gmail.com")).thenReturn(Optional.of(target));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "attacker@gmail.com", "n/a", java.util.List.of(new SimpleGrantedAuthority("ROLE_USER"))));

        assertThatThrownBy(() -> service.updateUser(
                "target@gmail.com",
                new UserRequest("new_name", "9876543210", LocalDate.of(1990, 1, 1))))
                .isInstanceOf(AccessDeniedException.class);
    }

    private User user(String email) {
        User user = new User();
        user.setUserId(email);
        user.setEmail(email);
        user.setUserRole(UserRole.ROLE_USER);
        return user;
    }
}
