package com.example.movieticketbookingsystem.repository;

import com.example.movieticketbookingsystem.entity.Screen;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ScreenRepository extends JpaRepository<Screen, String> {

    /** Locks the screen row so concurrent show scheduling on the same screen is serialized. */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Screen s where s.screenId = :screenId")
    Optional<Screen> findByIdForUpdate(@Param("screenId") String screenId);
}
