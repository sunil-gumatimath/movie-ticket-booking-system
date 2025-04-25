package com.example.movieticketbookingsystem.repository;

import com.example.movieticketbookingsystem.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRepository extends JpaRepository<Theater,String> {
}
