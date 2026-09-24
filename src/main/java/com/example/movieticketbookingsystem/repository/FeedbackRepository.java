package com.example.movieticketbookingsystem.repository;

import com.example.movieticketbookingsystem.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback,String> {

    /**
     * Find all feedbacks for a movie ID
     *
     * @param movieId The movie ID
     * @return List of feedbacks for the movie
     */
    List<Feedback> findByMovieMovieId(String movieId);

    boolean existsByUserUserIdAndMovieMovieId(String userId, String movieId);

    @Query("select coalesce(avg(f.rating), 0) from Feedback f where f.movie.movieId = :movieId")
    double findAverageRatingByMovieId(String movieId);
}
