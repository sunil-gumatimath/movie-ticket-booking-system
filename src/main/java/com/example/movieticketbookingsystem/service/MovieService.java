package com.example.movieticketbookingsystem.service;

import com.example.movieticketbookingsystem.dto.response.MovieResponse;

public interface MovieService {
    MovieResponse getMovie(String movieId);
}
