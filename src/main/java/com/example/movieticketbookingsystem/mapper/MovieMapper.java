package com.example.movieticketbookingsystem.mapper;

import com.example.movieticketbookingsystem.dto.request.MovieRequest;
import com.example.movieticketbookingsystem.dto.response.MovieResponse;
import com.example.movieticketbookingsystem.entity.Movie;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {

    public Movie movieEntityMapper(MovieRequest request) {
        if (request == null) {
            return null;
        }
        Movie movie = new Movie();
        movie.setTitle(request.title().trim());
        movie.setDescription(request.description().trim());
        movie.setRuntime(request.runtime());
        movie.setCertificate(request.certificate());
        movie.setGenre(request.genre());
        movie.setCastList(request.castList().stream().map(String::trim).collect(java.util.stream.Collectors.toSet()));
        return movie;
    }

    public MovieResponse movieResponseMapper(Movie movie, double averageRating) {
        if (movie == null) {
            return null;
        }

        return new MovieResponse(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDescription(),
                Math.round(averageRating * 100) / 100.0,
                movie.getRuntime(),
                movie.getCertificate(),
                movie.getGenre(),
                movie.getCastList()
        );
    }
}

