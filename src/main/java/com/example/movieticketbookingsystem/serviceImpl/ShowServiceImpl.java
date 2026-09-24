package com.example.movieticketbookingsystem.serviceImpl;

import com.example.movieticketbookingsystem.dto.request.ShowRequest;
import com.example.movieticketbookingsystem.dto.response.ShowResponse;
import com.example.movieticketbookingsystem.entity.Movie;
import com.example.movieticketbookingsystem.entity.Screen;
import com.example.movieticketbookingsystem.entity.Shows;
import com.example.movieticketbookingsystem.entity.Theater;
import com.example.movieticketbookingsystem.exception.ConflictException;
import com.example.movieticketbookingsystem.exception.MovieNotFoundByIdException;
import com.example.movieticketbookingsystem.exception.ScreenIdNotFoundException;
import com.example.movieticketbookingsystem.exception.TheaterScreenMismatchException;
import com.example.movieticketbookingsystem.mapper.ShowMapper;
import com.example.movieticketbookingsystem.repository.MovieRepository;
import com.example.movieticketbookingsystem.repository.ScreenRepository;
import com.example.movieticketbookingsystem.repository.ShowRepository;
import com.example.movieticketbookingsystem.service.ShowService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;

@Service
@AllArgsConstructor
public class ShowServiceImpl implements ShowService {

    private static final Duration MAX_MOVIE_RUNTIME = Duration.ofHours(24);

    private final ShowRepository showRepository;
    private final ScreenRepository screenRepository;
    private final MovieRepository movieRepository;
    private final ShowMapper showMapper;

    @Override
    @Transactional
    public ShowResponse addShow(ShowRequest showRequest, String theaterId, String screenId) {
        if (showRequest == null || showRequest.startTimeEpochMillis() == null
                || showRequest.movieId() == null || showRequest.movieId().isBlank()) {
            throw new IllegalArgumentException("Show request is incomplete");
        }

        Screen screen = screenRepository.findByIdForUpdate(screenId)
                .orElseThrow(() -> new ScreenIdNotFoundException("Screen not found"));

        Theater theater = screen.getTheater();
        if (theater == null) {
            throw new TheaterScreenMismatchException("Screen is not associated with any theater");
        }
        if (!theater.getTheaterId().equals(theaterId)) {
            throw new TheaterScreenMismatchException("Screen does not belong to the specified theater");
        }
        requireTheaterOwner(theater);

        Movie movie = movieRepository.findById(showRequest.movieId())
                .orElseThrow(() -> new MovieNotFoundByIdException("Movie not found"));
        validateRuntime(movie.getRuntime());

        Instant now = Instant.now();
        Instant requestedStartTime = parseStartTime(showRequest.startTimeEpochMillis());
        if (requestedStartTime.isBefore(now)) {
            throw new ConflictException("Show start time cannot be in the past");
        }

        Instant endInstant;
        try {
            endInstant = requestedStartTime.plus(movie.getRuntime());
        } catch (RuntimeException exception) {
            throw new IllegalArgumentException("Show end time is invalid");
        }
        if (!endInstant.isAfter(requestedStartTime)) {
            throw new IllegalArgumentException("Show end time must be after its start time");
        }

        boolean isOccupied = showRepository.existsByScreenAndStartsAtLessThanAndEndsAtGreaterThan(
                screen, endInstant, requestedStartTime);
        if (isOccupied) {
            throw new ConflictException("The selected time slot is already occupied for this screen");
        }

        Shows shows = new Shows();
        shows.setScreen(screen);
        shows.setTheater(theater);
        shows.setMovie(movie);
        shows.setStartsAt(requestedStartTime);
        shows.setEndsAt(endInstant);

        return showMapper.toShowResponse(showRepository.save(shows));
    }

    private Instant parseStartTime(long epochMillis) {
        try {
            return Instant.ofEpochMilli(epochMillis);
        } catch (RuntimeException exception) {
            throw new IllegalArgumentException("Show start time is invalid");
        }
    }

    private void validateRuntime(Duration runtime) {
        if (runtime == null || runtime.isZero() || runtime.isNegative()
                || runtime.compareTo(MAX_MOVIE_RUNTIME) > 0) {
            throw new IllegalArgumentException("Movie runtime must be positive and no longer than 24 hours");
        }
    }

    private void requireTheaterOwner(Theater theater) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Authentication is required");
        }
        if (theater.getOwner() == null
                || !theater.getOwner().getEmail().equalsIgnoreCase(authentication.getName())) {
            throw new AccessDeniedException("You may only schedule shows for your own theaters");
        }
    }
}
