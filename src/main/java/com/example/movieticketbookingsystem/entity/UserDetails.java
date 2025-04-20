package com.example.movieticketbookingsystem.entity;

import com.example.movieticketbookingsystem.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public class UserDetails {

    @Id
    private String userId;
    private String username;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private String phoneNumber;
    private LocalDate dateOfBirth;
    private Long createdAt;
    private Long updatedAt;

}
