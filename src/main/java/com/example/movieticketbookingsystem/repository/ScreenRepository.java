package com.example.movieticketbookingsystem.repository;

import com.example.movieticketbookingsystem.entity.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

public interface ScreenRepository extends JpaRepository<Screen,String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Screen s where s.screenId = :screenId")
    java.util.Optional<Screen> findByIdForUpdate(@Param("screenId") String screenId);
}
