package com.example.movieticketbookingsystem.service;

import com.example.movieticketbookingsystem.dto.request.TheaterRequest;
import com.example.movieticketbookingsystem.dto.response.TheaterResponse;

public interface TheaterService {

    TheaterResponse createTheater(TheaterRequest theaterRequest);

    TheaterResponse findTheater(String id);

    TheaterResponse updateTheater(String id, TheaterRequest theaterRequest);
}
