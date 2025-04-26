package com.example.movieticketbookingsystem.service;

import com.example.movieticketbookingsystem.dto.request.ScreenRequest;
import com.example.movieticketbookingsystem.dto.response.ScreenResponse;
import com.example.movieticketbookingsystem.dto.response.ScreenResponseList;
import jakarta.validation.Valid;

public interface ScreenService {
    ScreenResponse addScreen(@Valid String theaterId, ScreenRequest screenRequest);

    ScreenResponseList findScreen(@Valid String screenId);
}
