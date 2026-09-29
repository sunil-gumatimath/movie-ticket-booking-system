package com.example.movieticketbookingsystem.repository;

import com.example.movieticketbookingsystem.entity.Feedback;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, String> {

    List<Feedback> findByMovieMovieId(String movieId, Pageable pageable);

    boolean existsByUserUserIdAndMovieMovieId(String userId, String movieId);

    @Query("select coalesce(avg(f.rating), 0) from Feedback f where f.movie.movieId = :movieId")
    double findAverageRatingByMovieId(String movieId);
}
