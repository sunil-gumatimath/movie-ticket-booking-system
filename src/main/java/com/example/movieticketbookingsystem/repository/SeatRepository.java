package com.example.movieticketbookingsystem.repository;

import com.example.movieticketbookingsystem.entity.Screen;
import com.example.movieticketbookingsystem.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat,String> {
    List<Seat> findByScreen(Screen screen);
}
