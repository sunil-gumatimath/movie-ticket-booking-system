package com.example.movieticketbookingsystem.repository;

import com.example.movieticketbookingsystem.entity.Screen;
import com.example.movieticketbookingsystem.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ShowRepository extends JpaRepository<Show,String> {

    boolean existsByScreenAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(Screen screen, LocalDateTime endTime, LocalDateTime startTime);
}
