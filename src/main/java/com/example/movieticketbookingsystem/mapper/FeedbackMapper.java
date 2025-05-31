package com.example.movieticketbookingsystem.mapper;

import com.example.movieticketbookingsystem.dto.request.FeedbackRequest;
import com.example.movieticketbookingsystem.dto.response.FeedbackResponse;
import com.example.movieticketbookingsystem.entity.Feedback;
import com.example.movieticketbookingsystem.entity.Movie;
import com.example.movieticketbookingsystem.entity.User;
import org.springframework.stereotype.Component;

@Component
public class FeedbackMapper {

    /**
     * Maps a FeedbackRequest to a Feedback entity
     *
     * @param request The FeedbackRequest DTO
     * @param movie The Movie entity
     * @param user The User entity
     * @return A new Feedback entity
     */
    public Feedback toEntity(FeedbackRequest request, Movie movie, User user) {
        if (request == null) {
            return null;
        }

        Feedback feedback = new Feedback();
        feedback.setRating(request.rating());
        feedback.setReview(request.review());
        feedback.setMovie(movie);
        feedback.setUser(user);

        return feedback;
    }

    /**
     * Maps a Feedback entity to a FeedbackResponse DTO
     *
     * @param feedback The Feedback entity
     * @return A new FeedbackResponse DTO
     */
    public FeedbackResponse toResponse(Feedback feedback) {
        if (feedback == null) {
            return null;
        }

        return new FeedbackResponse(
                feedback.getFeedbackId(),
                feedback.getRating(),
                feedback.getReview(),
                feedback.getCreatedAt(),
                feedback.getMovie().getMovieId(),
                feedback.getMovie().getTitle(),
                feedback.getUser().getUserId(),
                feedback.getUser().getUsername()
        );
    }
}
