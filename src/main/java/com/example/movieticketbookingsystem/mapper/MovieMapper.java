package com.example.movieticketbookingsystem.mapper;

import com.example.movieticketbookingsystem.dto.request.MovieRequest;
import com.example.movieticketbookingsystem.dto.response.MovieResponse;
import com.example.movieticketbookingsystem.entity.Movie;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;

@Component
public class MovieMapper {

    public Movie movieEntityMapper(MovieRequest request) {
        Movie movie = new Movie();
        movie.setTitle(request.title());
        movie.setDescription(request.description());
        movie.setRuntime(request.runtime());
        movie.setCertificate(request.certificate());
        movie.setGenre(request.genre());
        movie.setCastList(request.castList());
        return movie;
    }

    public MovieResponse movieResponseMapper(Movie movie,double avgRatings){
        if (movie==null)
            return null;
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedRatings = df.format(avgRatings);

        return new MovieResponse(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDescription(),
                formattedRatings,
                movie.getRuntime(),
                movie.getCertificate(),
                movie.getGenre(),
                movie.getCastList()
        );
    }
}
