package com.example.movieticketbookingsystem.serviceImpl;

import com.example.movieticketbookingsystem.dto.request.FeedbackRequest;
import com.example.movieticketbookingsystem.dto.response.FeedbackResponse;
import com.example.movieticketbookingsystem.entity.Feedback;
import com.example.movieticketbookingsystem.entity.Movie;
import com.example.movieticketbookingsystem.entity.User;
import com.example.movieticketbookingsystem.exception.ResourceNotFoundException;
import com.example.movieticketbookingsystem.repository.FeedbackRepository;
import com.example.movieticketbookingsystem.repository.MovieRepository;
import com.example.movieticketbookingsystem.repository.UserRepository;
import com.example.movieticketbookingsystem.service.FeedbackService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public FeedbackResponse createFeedback(String movieId, FeedbackRequest feedbackRequest) {
        // Get the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        // Find the user
        User user = (User) userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Find the movie
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with ID: " + movieId));

        // Create and save the feedback
        Feedback feedback = new Feedback();
        feedback.setRating(feedbackRequest.rating());
        feedback.setReview(feedbackRequest.review());
        feedback.setMovie(movie);
        feedback.setUser(user);

        Feedback savedFeedback = feedbackRepository.save(feedback);

        // Return the response
        return new FeedbackResponse(
                savedFeedback.getFeedbackId(),
                savedFeedback.getRating(),
                savedFeedback.getReview(),
                savedFeedback.getCreatedAt()
        );
    }
}
