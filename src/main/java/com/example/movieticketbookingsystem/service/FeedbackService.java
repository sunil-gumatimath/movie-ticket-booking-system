package com.example.movieticketbookingsystem.service;

import com.example.movieticketbookingsystem.dto.request.FeedbackRequest;
import com.example.movieticketbookingsystem.dto.response.FeedbackResponse;
import jakarta.validation.Valid;

public interface FeedbackService {
    FeedbackResponse createFeedback(String movieId, @Valid FeedbackRequest feedbackRequest);
}
