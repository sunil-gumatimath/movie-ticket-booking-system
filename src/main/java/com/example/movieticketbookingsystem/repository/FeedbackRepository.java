package com.example.movieticketbookingsystem.repository;

import com.example.movieticketbookingsystem.entity.Feedback;
import com.example.movieticketbookingsystem.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback,String> {

    /**
     * Find all feedbacks for a movie
     *
     * @param movie The movie entity
     * @return List of feedbacks for the movie
     */
    List<Feedback> findByMovie(Movie movie);

    /**
     * Find all feedbacks for a movie ID
     *
     * @param movieId The movie ID
     * @return List of feedbacks for the movie
     */
    List<Feedback> findByMovieMovieId(String movieId);
}
