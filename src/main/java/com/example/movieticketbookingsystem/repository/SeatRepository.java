package com.example.movieticketbookingsystem.repository;

import com.example.movieticketbookingsystem.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat,String> {
}
