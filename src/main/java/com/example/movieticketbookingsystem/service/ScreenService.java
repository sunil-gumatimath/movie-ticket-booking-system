package com.example.movieticketbookingsystem.service;

import com.example.movieticketbookingsystem.dto.request.ScreenRequest;
import com.example.movieticketbookingsystem.dto.response.ScreenResponse;
import jakarta.validation.Valid;

public interface ScreenService {
    ScreenResponse addScreen(@Valid String theaterId, ScreenRequest screenRequest);

    ScreenResponse findScreen(@Valid String screenId, ScreenRequest screenRequest);
}
