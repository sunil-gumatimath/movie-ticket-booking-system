package com.example.movieticketbookingsystem.repository;

import com.example.movieticketbookingsystem.entity.Movie;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, String> {

    /** Loads the movie with its cast list, which every movie response includes. */
    @EntityGraph(attributePaths = "castList")
    Optional<Movie> findWithCastListByMovieId(String movieId);
}
