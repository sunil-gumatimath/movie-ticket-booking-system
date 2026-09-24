package com.example.movieticketbookingsystem.repository;

import com.example.movieticketbookingsystem.entity.Screen;
import com.example.movieticketbookingsystem.entity.Shows;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface ShowRepository extends JpaRepository<Shows, String> {

    long countByScreenScreenId(String screenId);

    boolean existsByScreenAndStartsAtLessThanAndEndsAtGreaterThan(
            Screen screen, Instant endTime, Instant startTime
    );

}
