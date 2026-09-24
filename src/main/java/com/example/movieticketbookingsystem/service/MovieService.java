package com.example.movieticketbookingsystem.service;

import com.example.movieticketbookingsystem.dto.request.MovieRequest;
import com.example.movieticketbookingsystem.dto.request.MovieCastUpdateRequest;
import com.example.movieticketbookingsystem.dto.request.MovieDescriptionUpdateRequest;
import com.example.movieticketbookingsystem.dto.request.MovieUpdateRequest;
import com.example.movieticketbookingsystem.dto.response.MovieResponse;

public interface MovieService {
    MovieResponse createMovie(MovieRequest movieRequest);
    MovieResponse updateMovie(String movieId, MovieUpdateRequest movieRequest);
    MovieResponse updateMovieDescription(String movieId, MovieDescriptionUpdateRequest movieRequest);
    MovieResponse updateMovieCast(String movieId, MovieCastUpdateRequest movieRequest);
    MovieResponse getMovie(String movieId);
}
