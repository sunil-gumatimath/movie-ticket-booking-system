package com.example.movieticketbookingsystem.serviceImpl;

import com.example.movieticketbookingsystem.dto.request.ShowRequest;
import com.example.movieticketbookingsystem.dto.response.ShowResponse;
import com.example.movieticketbookingsystem.entity.Movie;
import com.example.movieticketbookingsystem.entity.Screen;
import com.example.movieticketbookingsystem.entity.Shows;
import com.example.movieticketbookingsystem.entity.Theater;
import com.example.movieticketbookingsystem.exception.ScreenIdNotFoundException;
import com.example.movieticketbookingsystem.exception.MovieNotFoundByIdException;
import com.example.movieticketbookingsystem.exception.TheaterOwnerIdException;
import com.example.movieticketbookingsystem.exception.TheaterScreenMismatchException;
import com.example.movieticketbookingsystem.exception.ConflictException;
import com.example.movieticketbookingsystem.mapper.ShowMapper;
import com.example.movieticketbookingsystem.repository.MovieRepository;
import com.example.movieticketbookingsystem.repository.ScreenRepository;
import com.example.movieticketbookingsystem.repository.ShowRepository;
import com.example.movieticketbookingsystem.service.ShowService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@AllArgsConstructor
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;
    private final ScreenRepository screenRepository;
    private final MovieRepository movieRepository;
    private final ShowMapper showMapper;

    @Override
    @Transactional
    public ShowResponse addShow(ShowRequest showRequest, String theaterId, String screenId) {
        // 1. Validate Screen
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new ScreenIdNotFoundException("Screen not found"));

        // 2. Get associated Theater
        Theater theater = screen.getTheater();
        if (theater == null) {
            throw new TheaterScreenMismatchException("Screen is not associated with any theater");
        }

        // 3. Validate Theater ID
        if (!theater.getTheaterId().equals(theaterId)) {
            throw new TheaterScreenMismatchException("Screen does not belong to the specified theater");
        }

        // 4. Validate Movie
        Movie movie = movieRepository.findById(showRequest.movieId())
                .orElseThrow(() -> new MovieNotFoundByIdException("Movie not found"));

        // 5. Convert start time from epoch to LocalDateTime
        Instant now = Instant.now();
        Instant requestedStartTime = Instant.ofEpochMilli(showRequest.startTimeEpochMillis());

        // Validate that start time is not in the past
        if (requestedStartTime.isBefore(now)) {
            throw new ConflictException("Show start time cannot be in the past");
        }

        // 6. Calculate end time using movie duration
        Instant endInstant = requestedStartTime.plus(movie.getRuntime());

        // 7. Check for overlapping shows
        // This checks if there's any show where:
        // - The show is for the same screen
        // - The show's start time is before our end time (startsAt < endInstant)
        // - The show's end time is after our start time (endsAt > startInstant)
        boolean isOccupied = showRepository.existsByScreenAndStartsAtLessThanAndEndsAtGreaterThan(
                screen, endInstant, requestedStartTime
        );

        if (isOccupied) {
            throw new ConflictException("The selected time slot is already occupied for this screen");
        }

        // 8. Create and save the Show
        Shows shows = new Shows();
        shows.setScreen(screen);
        shows.setTheater(theater);
        shows.setMovie(movie);
        shows.setStartsAt(requestedStartTime);
        shows.setEndsAt(endInstant);

        Shows savedShow = showRepository.save(shows);

        // 9. Return response using mapper
        return showMapper.toShowResponse(savedShow);
    }
}
