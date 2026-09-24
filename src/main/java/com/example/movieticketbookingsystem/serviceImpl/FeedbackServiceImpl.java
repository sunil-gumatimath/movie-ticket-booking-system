package com.example.movieticketbookingsystem.serviceImpl;

import com.example.movieticketbookingsystem.dto.request.FeedbackRequest;
import com.example.movieticketbookingsystem.dto.response.FeedbackResponse;
import com.example.movieticketbookingsystem.entity.Feedback;
import com.example.movieticketbookingsystem.entity.Movie;
import com.example.movieticketbookingsystem.entity.User;
import com.example.movieticketbookingsystem.enums.UserRole;
import com.example.movieticketbookingsystem.exception.ConflictException;
import com.example.movieticketbookingsystem.exception.MovieNotFoundByIdException;
import com.example.movieticketbookingsystem.mapper.FeedbackMapper;
import com.example.movieticketbookingsystem.repository.FeedbackRepository;
import com.example.movieticketbookingsystem.repository.MovieRepository;
import com.example.movieticketbookingsystem.repository.UserRepository;
import com.example.movieticketbookingsystem.service.FeedbackService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        if (feedbackRequest == null || feedbackRequest.rating() < 1 || feedbackRequest.rating() > 5
                || feedbackRequest.review() == null || feedbackRequest.review().isBlank()
                || feedbackRequest.review().length() > 500) {
            throw new IllegalArgumentException("Feedback must contain a rating from 1 to 5 and a review up to 500 characters");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Authentication is required");
        }

        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail)
                .filter(candidate -> !candidate.isDeleted())
                .filter(candidate -> candidate.getUserRole() == UserRole.ROLE_USER)
                .filter(User.class::isInstance)
                .map(User.class::cast)
                .orElseThrow(() -> new AccessDeniedException("Only active normal users can submit feedback"));

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundByIdException("Movie not found with ID: " + movieId));

        if (feedbackRepository.existsByUserUserIdAndMovieMovieId(user.getUserId(), movieId)) {
            throw new ConflictException("User has already submitted feedback for this movie");
        }

        Feedback feedback = feedbackMapper.toEntity(feedbackRequest, movie, user);
        try {
            Feedback savedFeedback = feedbackRepository.saveAndFlush(feedback);
            return feedbackMapper.toResponse(savedFeedback);
        } catch (DataIntegrityViolationException exception) {
            if (isFeedbackUniqueViolation(exception)) {
                throw new ConflictException("User has already submitted feedback for this movie");
            }
            throw exception;
        }
    }

    private boolean isFeedbackUniqueViolation(DataIntegrityViolationException exception) {
        StringBuilder message = new StringBuilder();
        Throwable current = exception;
        while (current != null) {
            if (current.getMessage() != null) {
                message.append(' ').append(current.getMessage().toLowerCase());
            }
            current = current.getCause();
        }
        String text = message.toString();
        return text.contains("uk_feedback_user_movie")
                || ((text.contains("duplicate") || text.contains("unique"))
                && (text.contains("user_id") || text.contains("movie_id")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackResponse> getFeedbacksByMovie(String movieId) {
        if (!movieRepository.existsById(movieId)) {
            throw new MovieNotFoundByIdException("Movie not found with ID: " + movieId);
        }

        return feedbackRepository.findByMovieMovieId(movieId).stream()
                .map(feedbackMapper::toResponse)
                .toList();
    }
}
