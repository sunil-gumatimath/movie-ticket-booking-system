package com.example.movieticketbookingsystem.dto.response;

import java.time.Instant;

public record FeedbackResponse(
        String feedbackId,
        int rating,
        String review,
        Instant createdAt,
        String movieId,
        String movieTitle,
        String userId,
        String username
) {
}
