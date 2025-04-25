package com.example.movieticketbookingsystem.entity;

import com.example.movieticketbookingsystem.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@Entity // VERY IMPORTANT
public abstract class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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

    @Column(name = "is_deleted")
    private boolean isDeleted = false;
    @Column(name = "deleted_at")
    private Instant deletedAt;

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt != null ? createdAt.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli() : null;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt != null ? updatedAt.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli() : null;
    }

    public void softDelete(){
        this.isDeleted = true;
        this.deletedAt = Instant.now();
    }
}
