package com.example.movieticketbookingsystem.serviceImpl;

import com.example.movieticketbookingsystem.dto.request.FeedbackRequest;
import com.example.movieticketbookingsystem.dto.response.FeedbackResponse;
import com.example.movieticketbookingsystem.entity.Feedback;
import com.example.movieticketbookingsystem.entity.Movie;
import com.example.movieticketbookingsystem.entity.User;
import com.example.movieticketbookingsystem.exception.UserNotFoundByEmailException;
import com.example.movieticketbookingsystem.exception.MovieNotFoundByIdException;
import com.example.movieticketbookingsystem.mapper.FeedbackMapper;
import com.example.movieticketbookingsystem.repository.FeedbackRepository;
import com.example.movieticketbookingsystem.repository.MovieRepository;
import com.example.movieticketbookingsystem.repository.UserRepository;
import com.example.movieticketbookingsystem.service.FeedbackService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final FeedbackMapper feedbackMapper;

    @Override
    @Transactional
    public FeedbackResponse createFeedback(String movieId, FeedbackRequest feedbackRequest) {
        // Get the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        // Find the user
        User user = (User) userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundByEmailException("User not found"));

        // Find the movie
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundByIdException("Movie not found with ID: " + movieId));

        // Create feedback entity using mapper
        Feedback feedback = feedbackMapper.toEntity(feedbackRequest, movie, user);

        // Save the feedback
        Feedback savedFeedback = feedbackRepository.save(feedback);

        // Return the response using mapper
        return feedbackMapper.toResponse(savedFeedback);
    }

    @Override
    public List<FeedbackResponse> getFeedbacksByMovie(String movieId) {
        // Validate that the movie exists
        if (!movieRepository.existsById(movieId)) {
            throw new MovieNotFoundByIdException("Movie not found with ID: " + movieId);
        }

        // Get all feedbacks for the movie
        List<Feedback> feedbacks = feedbackRepository.findByMovieMovieId(movieId);

        // Map to response DTOs
        return feedbacks.stream()
                .map(feedbackMapper::toResponse)
                .collect(Collectors.toList());
    }
}
