package com.example.movieticketbookingsystem.controller;

import com.example.movieticketbookingsystem.dto.request.FeedbackRequest;
import com.example.movieticketbookingsystem.dto.response.FeedbackResponse;
import com.example.movieticketbookingsystem.serviceImpl.FeedbackServiceImpl;
import com.example.movieticketbookingsystem.utility.ResponseStructure;
import com.example.movieticketbookingsystem.utility.RestResponseBuilder;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class FeedbackController {

    private final FeedbackServiceImpl feedbackService;

    @PostMapping("/movies/{movieId}/feedback")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ResponseStructure<FeedbackResponse>> createFeedback(
            @PathVariable String movieId,
            @Valid @RequestBody FeedbackRequest feedbackRequest) {
        FeedbackResponse feedbackResponse = feedbackService.createFeedback(movieId, feedbackRequest);
        return RestResponseBuilder.created("Feedback submitted successfully", feedbackResponse, HttpStatus.CREATED);
    }
}
