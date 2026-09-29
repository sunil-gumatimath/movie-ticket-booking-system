package com.example.movieticketbookingsystem.service.impl;

import com.example.movieticketbookingsystem.dto.request.MovieCastUpdateRequest;
import com.example.movieticketbookingsystem.dto.request.MovieDescriptionUpdateRequest;
import com.example.movieticketbookingsystem.dto.request.MovieRequest;
import com.example.movieticketbookingsystem.dto.request.MovieUpdateRequest;
import com.example.movieticketbookingsystem.dto.response.MovieResponse;
import com.example.movieticketbookingsystem.entity.Movie;
import com.example.movieticketbookingsystem.exception.ResourceNotFoundException;
import com.example.movieticketbookingsystem.mapper.MovieMapper;
import com.example.movieticketbookingsystem.repository.FeedbackRepository;
import com.example.movieticketbookingsystem.repository.MovieRepository;
import com.example.movieticketbookingsystem.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

/**
 * Movie operations. Admin-only access is enforced by {@code @PreAuthorize} on the
 * controller, and request fields are validated by the DTO constraints.
 */
@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final FeedbackRepository feedbackRepository;
    private final MovieMapper movieMapper;

    @Override
    @Transactional
    public MovieResponse createMovie(MovieRequest request) {
        Movie savedMovie = movieRepository.save(movieMapper.movieEntityMapper(request));
        return movieMapper.movieResponseMapper(savedMovie, 0);
    }

    @Override
    @Transactional
    public MovieResponse updateMovie(String movieId, MovieUpdateRequest request) {
        Movie movie = findMovie(movieId);
        movie.setTitle(request.title().trim());
        return toResponse(movie);
    }

    @Override
    @Transactional
    public MovieResponse updateMovieDescription(String movieId, MovieDescriptionUpdateRequest request) {
        Movie movie = findMovie(movieId);
        movie.setDescription(request.description().trim());
        return toResponse(movie);
    }

    @Override
    @Transactional
    public MovieResponse updateMovieCast(String movieId, MovieCastUpdateRequest request) {
        Movie movie = findMovie(movieId);
        movie.setCastList(new HashSet<>(request.castList()));
        return toResponse(movie);
    }

    @Override
    @Transactional(readOnly = true)
    public MovieResponse getMovie(String movieId) {
        return toResponse(findMovie(movieId));
    }

    private Movie findMovie(String movieId) {
        return movieRepository.findWithCastListByMovieId(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", movieId));
    }

    private MovieResponse toResponse(Movie movie) {
        return movieMapper.movieResponseMapper(movie, feedbackRepository.findAverageRatingByMovieId(movie.getMovieId()));
    }
}
