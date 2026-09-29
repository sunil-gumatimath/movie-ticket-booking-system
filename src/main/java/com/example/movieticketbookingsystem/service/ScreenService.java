package com.example.movieticketbookingsystem.service;

import com.example.movieticketbookingsystem.dto.request.ScreenRequest;
import com.example.movieticketbookingsystem.dto.response.ScreenDetailResponse;
import com.example.movieticketbookingsystem.dto.response.ScreenResponse;

public interface ScreenService {

    ScreenResponse addScreen(String theaterId, ScreenRequest screenRequest);

    ScreenDetailResponse findScreen(String screenId);
}
