package com.example.movieticketbookingsystem.controller;

import com.example.movieticketbookingsystem.dto.request.MovieCastUpdateRequest;
import com.example.movieticketbookingsystem.dto.request.MovieDescriptionUpdateRequest;
import com.example.movieticketbookingsystem.dto.request.MovieRequest;
import com.example.movieticketbookingsystem.dto.request.MovieUpdateRequest;
import com.example.movieticketbookingsystem.dto.response.MovieResponse;
import com.example.movieticketbookingsystem.service.MovieService;
import com.example.movieticketbookingsystem.utility.ResponseStructure;
import com.example.movieticketbookingsystem.utility.RestResponseBuilder;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
public class MovieController {

    private final MovieService movieService;
    private final RestResponseBuilder restResponseBuilder;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/movies")
    public ResponseEntity<ResponseStructure<MovieResponse>> createMovie(
            @Valid @RequestBody MovieRequest movieRequest) {
        MovieResponse createdMovie = movieService.createMovie(movieRequest);
        return restResponseBuilder.success(HttpStatus.CREATED, "Movie Created", createdMovie);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/movies/{movieId}")
    public ResponseEntity<ResponseStructure<MovieResponse>> updateMovie(
            @PathVariable String movieId,
            @Valid @RequestBody MovieUpdateRequest movieRequest) {
        MovieResponse updatedMovie = movieService.updateMovie(movieId, movieRequest);
        return restResponseBuilder.success(HttpStatus.OK, "Movie Updated", updatedMovie);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/movies/{movieId}/description")
    public ResponseEntity<ResponseStructure<MovieResponse>> updateMovieDescription(
            @PathVariable String movieId,
            @Valid @RequestBody MovieDescriptionUpdateRequest movieRequest) {
        MovieResponse updatedMovie = movieService.updateMovieDescription(movieId, movieRequest);
        return restResponseBuilder.success(HttpStatus.OK, "Movie Description Updated", updatedMovie);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/movies/{movieId}/cast")
    public ResponseEntity<ResponseStructure<MovieResponse>> updateMovieCast(
            @PathVariable String movieId,
            @Valid @RequestBody MovieCastUpdateRequest movieRequest) {
        MovieResponse updatedMovie = movieService.updateMovieCast(movieId, movieRequest);
        return restResponseBuilder.success(HttpStatus.OK, "Movie Cast Updated", updatedMovie);
    }

    @GetMapping("/movies/{movieId}")
    public ResponseEntity<ResponseStructure<MovieResponse>> getMovie(@PathVariable String movieId) {
        MovieResponse movieResponse = movieService.getMovie(movieId);
        return restResponseBuilder.success(HttpStatus.OK, "Movie has been fetched successfully", movieResponse);
    }
}
