package com.example.movieticketbookingsystem.serviceImpl;

import com.example.movieticketbookingsystem.repository.FeedbackRepository;
import com.example.movieticketbookingsystem.service.FeedbackService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
}
