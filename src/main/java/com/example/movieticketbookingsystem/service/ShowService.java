package com.example.movieticketbookingsystem.service;

import com.example.movieticketbookingsystem.dto.request.ShowRequest;
import com.example.movieticketbookingsystem.dto.response.ShowResponse;
import jakarta.validation.Valid;

public interface ShowService {
    ShowResponse addShow(@Valid ShowRequest showRequest, String screenId);
}
