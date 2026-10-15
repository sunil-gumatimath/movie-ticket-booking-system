package com.example.movieticketbookingsystem.repository;

import com.example.movieticketbookingsystem.entity.Screen;
import com.example.movieticketbookingsystem.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface ShowRepository extends JpaRepository<Show, String> {

    boolean existsByScreenAndStartsAtLessThanAndEndsAtGreaterThan(
            Screen screen, Instant endTime, Instant startTime
    );

}
