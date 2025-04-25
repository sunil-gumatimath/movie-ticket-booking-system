package com.example.movieticketbookingsystem.service;

import com.example.movieticketbookingsystem.dto.request.TheaterRequest;
import com.example.movieticketbookingsystem.dto.response.TheaterResponse;
import com.example.movieticketbookingsystem.entity.Theater;
import jakarta.validation.Valid;

public interface TheaterService {

    public TheaterResponse createTheater(@Valid String email, TheaterRequest theaterRequest);

    TheaterResponse findTheater(@Valid String id, TheaterRequest theaterRequest);

    TheaterResponse updateTheater(@Valid String id, TheaterRequest theaterRequest);
}
