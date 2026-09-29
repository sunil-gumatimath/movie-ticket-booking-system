package com.example.movieticketbookingsystem.service;

import com.example.movieticketbookingsystem.dto.request.ShowRequest;
import com.example.movieticketbookingsystem.dto.response.ShowResponse;

public interface ShowService {

    ShowResponse addShow(ShowRequest showRequest, String theaterId, String screenId);
}
