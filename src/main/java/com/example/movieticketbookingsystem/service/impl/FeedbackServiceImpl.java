package com.example.movieticketbookingsystem.service.impl;

import com.example.movieticketbookingsystem.dto.request.FeedbackRequest;
import com.example.movieticketbookingsystem.dto.response.FeedbackResponse;
import com.example.movieticketbookingsystem.entity.Feedback;
import com.example.movieticketbookingsystem.entity.Movie;
import com.example.movieticketbookingsystem.entity.User;
import com.example.movieticketbookingsystem.exception.ConflictException;
import com.example.movieticketbookingsystem.exception.ConstraintViolations;
import com.example.movieticketbookingsystem.exception.ResourceNotFoundException;
import com.example.movieticketbookingsystem.mapper.FeedbackMapper;
import com.example.movieticketbookingsystem.repository.FeedbackRepository;
import com.example.movieticketbookingsystem.repository.MovieRepository;
import com.example.movieticketbookingsystem.security.CurrentUserService;
import com.example.movieticketbookingsystem.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private static final String ALREADY_SUBMITTED = "User has already submitted feedback for this movie";

    private final FeedbackRepository feedbackRepository;
    private final MovieRepository movieRepository;
    private final FeedbackMapper feedbackMapper;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public FeedbackResponse createFeedback(String movieId, FeedbackRequest feedbackRequest) {
        User user = currentUserService.currentCustomer();
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", movieId));

        if (feedbackRepository.existsByUserUserIdAndMovieMovieId(user.getUserId(), movieId)) {
            throw new ConflictException(ALREADY_SUBMITTED);
        }

        Feedback feedback = feedbackMapper.toEntity(feedbackRequest, movie, user);
        try {
            return feedbackMapper.toResponse(feedbackRepository.saveAndFlush(feedback));
        } catch (DataIntegrityViolationException exception) {
            // A concurrent request inserted the same (user, movie) pair after the check above.
            if (ConstraintViolations.violates(exception, Feedback.USER_MOVIE_UNIQUE_CONSTRAINT)) {
                throw new ConflictException(ALREADY_SUBMITTED, exception);
            }
            throw exception;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackResponse> getFeedbacksByMovie(String movieId, Pageable pageable) {
        if (!movieRepository.existsById(movieId)) {
            throw new ResourceNotFoundException("Movie", movieId);
        }

        return feedbackRepository.findByMovieMovieId(movieId, pageable).stream()
                .map(feedbackMapper::toResponse)
                .toList();
    }
}
