package com.example.movieticketbookingsystem.service;

import com.example.movieticketbookingsystem.dto.request.TheaterRequest;
import com.example.movieticketbookingsystem.dto.response.TheaterResponse;
import jakarta.validation.Valid;

public interface TheaterService {

    // Method to create a theater. It requires email validation and theater request data.
    TheaterResponse createTheater(@Valid String email, @Valid TheaterRequest theaterRequest);

    // Method to find a theater by its ID.
    TheaterResponse findTheater(@Valid String id);

    // Method to update an existing theater by its ID and the updated theater data.
    TheaterResponse updateTheater(@Valid String id, @Valid TheaterRequest theaterRequest);
}
