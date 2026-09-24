package com.example.movieticketbookingsystem.serviceImpl;

import com.example.movieticketbookingsystem.dto.request.MovieCastUpdateRequest;
import com.example.movieticketbookingsystem.dto.request.MovieDescriptionUpdateRequest;
import com.example.movieticketbookingsystem.dto.request.MovieRequest;
import com.example.movieticketbookingsystem.dto.request.MovieUpdateRequest;
import com.example.movieticketbookingsystem.dto.response.MovieResponse;
import com.example.movieticketbookingsystem.entity.Movie;
import com.example.movieticketbookingsystem.enums.UserRole;
import com.example.movieticketbookingsystem.exception.MovieNotFoundByIdException;
import com.example.movieticketbookingsystem.mapper.MovieMapper;
import com.example.movieticketbookingsystem.repository.FeedbackRepository;
import com.example.movieticketbookingsystem.repository.MovieRepository;
import com.example.movieticketbookingsystem.service.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@AllArgsConstructor
public class MovieServiceImpl implements MovieService {

    private static final Duration MAX_RUNTIME = Duration.ofHours(24);

    private final MovieRepository movieRepository;
    private final FeedbackRepository feedbackRepository;
    private final MovieMapper movieMapper;

    @Override
    @Transactional
    public MovieResponse createMovie(MovieRequest request) {
        requireAdmin();
        validateRuntime(request == null ? null : request.runtime());

        Movie movie = movieMapper.movieEntityMapper(request);
        Movie savedMovie = movieRepository.saveAndFlush(movie);
        return movieMapper.movieResponseMapper(savedMovie, 0);
    }

    @Override
    @Transactional
    public MovieResponse updateMovie(String movieId, MovieUpdateRequest request) {
        requireAdmin();
        if (request == null || request.title() == null || request.title().isBlank()) {
            throw new IllegalArgumentException("Movie title is required");
        }
        Movie movie = findMovie(movieId);
        movie.setTitle(request.title().trim());
        return responseForSavedMovie(movieRepository.save(movie));
    }

    @Override
    @Transactional
    public MovieResponse updateMovieDescription(String movieId, MovieDescriptionUpdateRequest request) {
        requireAdmin();
        if (request == null || request.description() == null || request.description().isBlank()) {
            throw new IllegalArgumentException("Movie description is required");
        }
        Movie movie = findMovie(movieId);
        movie.setDescription(request.description().trim());
        return responseForSavedMovie(movieRepository.save(movie));
    }

    @Override
    @Transactional
    public MovieResponse updateMovieCast(String movieId, MovieCastUpdateRequest request) {
        requireAdmin();
        if (request == null || request.castList() == null || request.castList().isEmpty()
                || request.castList().stream().anyMatch(name -> name == null || name.isBlank())) {
            throw new IllegalArgumentException("Movie cast list must contain at least one nonblank name");
        }
        Movie movie = findMovie(movieId);
        movie.setCastList(request.castList());
        return responseForSavedMovie(movieRepository.save(movie));
    }

    @Override
    @Transactional(readOnly = true)
    public MovieResponse getMovie(String movieId) {
        Movie movie = findMovie(movieId);
        if (movie.getCastList() != null) {
            movie.getCastList().size();
        }
        return movieMapper.movieResponseMapper(
                movie,
                feedbackRepository.findAverageRatingByMovieId(movieId)
        );
    }

    private Movie findMovie(String movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundByIdException("Movie not found in Database"));
    }

    private MovieResponse responseForSavedMovie(Movie movie) {
        return movieMapper.movieResponseMapper(
                movie,
                feedbackRepository.findAverageRatingByMovieId(movie.getMovieId())
        );
    }

    private void validateRuntime(Duration runtime) {
        if (runtime == null || runtime.isZero() || runtime.isNegative()
                || runtime.compareTo(MAX_RUNTIME) > 0) {
            throw new IllegalArgumentException("Movie runtime must be positive and no longer than 24 hours");
        }
    }

    private void requireAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || !authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(UserRole.ROLE_ADMIN.name()))) {
            throw new AccessDeniedException("Administrator authority is required");
        }
    }
}
