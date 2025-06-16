package com.example.movieticketbookingsystem.controller;

import com.example.movieticketbookingsystem.dto.response.MovieResponse;
import com.example.movieticketbookingsystem.serviceImpl.MovieServiceImpl;
import com.example.movieticketbookingsystem.utility.ResponseStructure;
import com.example.movieticketbookingsystem.utility.RestResponseBuilder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MovieController {

    private final MovieServiceImpl movieService;
    private final RestResponseBuilder restResponseBuilder;

    @GetMapping("/movies/{movieId}")
    public ResponseEntity<ResponseStructure<MovieResponse>> getMovie(@PathVariable String movieId){
        MovieResponse movieResponse = movieService.getMovie(movieId);
        return restResponseBuilder.success(HttpStatus.OK, "Movie has been fetch successfully", movieResponse);
    }
}
