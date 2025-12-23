package com.example.movieticketbookingsystem.entity;

import com.example.movieticketbookingsystem.enums.UserRole;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name = "user_details")
@EntityListeners(AuditingEntityListener.class)
public abstract class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    private String username;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", length = 30)
    private UserRole userRole;

    private String phoneNumber;
    private LocalDate dateOfBirth;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private Instant updatedAt;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "boolean default false")
    private boolean isDeleted = false;

    @Column(name = "deleted_at", nullable = true)
    private Instant deletedAt;

    public void softDelete() {
        this.isDeleted = true;
        this.deletedAt = Instant.now();
    }
}
