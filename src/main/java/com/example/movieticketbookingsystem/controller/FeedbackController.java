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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class FeedbackController {

    private final FeedbackServiceImpl feedbackService;

//    @PostMapping("/create/feedback")
//    public ResponseEntity<ResponseStructure<FeedbackResponse>> createFeedback(@Valid String movieId, @RequestBody FeedbackRequest feedbackRequest){
//        FeedbackResponse feedbackResponse = feedbackService.createFeedback(movieId,feedbackRequest);
//        return RestResponseBuilder.created("Feedback Created",feedbackResponse, HttpStatus.CREATED);
//    }

}
