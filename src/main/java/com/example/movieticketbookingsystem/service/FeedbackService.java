package com.example.movieticketbookingsystem.service;

import com.example.movieticketbookingsystem.dto.request.FeedbackRequest;
import com.example.movieticketbookingsystem.dto.response.FeedbackResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FeedbackService {

    FeedbackResponse createFeedback(String movieId, FeedbackRequest feedbackRequest);

    List<FeedbackResponse> getFeedbacksByMovie(String movieId, Pageable pageable);
}
