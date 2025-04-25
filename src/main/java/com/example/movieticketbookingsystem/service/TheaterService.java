package com.example.movieticketbookingsystem.service;

import com.example.movieticketbookingsystem.dto.request.TheaterRequest;
import com.example.movieticketbookingsystem.dto.response.TheaterResponse;
import com.example.movieticketbookingsystem.entity.Theater;

public interface TheaterService {

    public TheaterResponse createTheater(String email, TheaterRequest theaterRequest);
}
