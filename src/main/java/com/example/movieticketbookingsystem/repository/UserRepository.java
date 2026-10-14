package com.example.movieticketbookingsystem.repository;

import com.example.movieticketbookingsystem.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, String> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIsDeletedFalse(String email);

    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findByEmailAndIsDeletedFalse(String email);

    long deleteByEmailAndIsDeletedTrue(String email);
}
