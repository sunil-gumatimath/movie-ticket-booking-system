package com.example.movieticketbookingsystem.service;

import com.example.movieticketbookingsystem.dto.request.FeedbackRequest;
import com.example.movieticketbookingsystem.dto.response.FeedbackResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface FeedbackService {
    /**
     * Create a new feedback for a movie
     *
     * @param movieId The ID of the movie
     * @param feedbackRequest The feedback request
     * @return The created feedback response
     */
    FeedbackResponse createFeedback(String movieId, @Valid FeedbackRequest feedbackRequest);

    /**
     * Get all feedbacks for a movie
     *
     * @param movieId The ID of the movie
     * @return List of feedback responses
     */
    List<FeedbackResponse> getFeedbacksByMovie(String movieId);
}
