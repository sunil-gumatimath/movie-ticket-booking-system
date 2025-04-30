package com.example.movieticketbookingsystem.repository;

import com.example.movieticketbookingsystem.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie,String> {
}
