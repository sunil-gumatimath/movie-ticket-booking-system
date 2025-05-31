package com.example.movieticketbookingsystem.serviceImpl;

import com.example.movieticketbookingsystem.dto.response.MovieResponse;
import com.example.movieticketbookingsystem.entity.Feedback;
import com.example.movieticketbookingsystem.entity.Movie;
import com.example.movieticketbookingsystem.exception.MovieNotFoundByIdException;
import com.example.movieticketbookingsystem.mapper.MovieMapper;
import com.example.movieticketbookingsystem.repository.MovieRepository;
import com.example.movieticketbookingsystem.service.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    @Override
    public MovieResponse getMovie(String movieId) {

        if (movieRepository.existsById(movieId)){

            Movie movie = movieRepository.findById(movieId).get();
            List<Feedback> feedbacks =movie.getFeedbacks();

            double avgRatings = 0;

            if (!feedbacks.isEmpty()) {
                for (Feedback feedback: feedbacks){
                    avgRatings+=feedback.getRating();
                }
                avgRatings/=feedbacks.size();
            }

            return movieMapper.movieResponseMapper(movie,avgRatings);
        }
        throw new MovieNotFoundByIdException("Movie not found in Database");
    }
}
