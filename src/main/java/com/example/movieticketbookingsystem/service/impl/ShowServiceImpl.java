package com.example.movieticketbookingsystem.service.impl;

import com.example.movieticketbookingsystem.dto.request.ShowRequest;
import com.example.movieticketbookingsystem.dto.response.ShowResponse;
import com.example.movieticketbookingsystem.entity.Movie;
import com.example.movieticketbookingsystem.entity.Screen;
import com.example.movieticketbookingsystem.entity.Show;
import com.example.movieticketbookingsystem.entity.Theater;
import com.example.movieticketbookingsystem.exception.BadRequestException;
import com.example.movieticketbookingsystem.exception.ConflictException;
import com.example.movieticketbookingsystem.exception.ResourceNotFoundException;
import com.example.movieticketbookingsystem.mapper.ShowMapper;
import com.example.movieticketbookingsystem.repository.MovieRepository;
import com.example.movieticketbookingsystem.repository.ScreenRepository;
import com.example.movieticketbookingsystem.repository.ShowRepository;
import com.example.movieticketbookingsystem.security.CurrentUserService;
import com.example.movieticketbookingsystem.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;
    private final ScreenRepository screenRepository;
    private final MovieRepository movieRepository;
    private final ShowMapper showMapper;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public ShowResponse addShow(ShowRequest showRequest, String theaterId, String screenId) {
        // Row lock: concurrent requests for the same screen wait here, so the overlap
        // check below cannot race with another insert.
        Screen screen = screenRepository.findByIdForUpdate(screenId)
                .orElseThrow(() -> new ResourceNotFoundException("Screen", screenId));

        Theater theater = screen.getTheater();
        if (!theater.getTheaterId().equals(theaterId)) {
            throw new BadRequestException("Screen does not belong to the specified theater");
        }
        currentUserService.requireOwnerOf(theater);

        Movie movie = movieRepository.findById(showRequest.movieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie", showRequest.movieId()));

        Instant startsAt = Instant.ofEpochMilli(showRequest.startTimeEpochMillis());
        if (startsAt.isBefore(Instant.now())) {
            throw new BadRequestException("Show start time cannot be in the past");
        }
        // Movie runtime is validated (positive, at most 24h) when the movie is created.
        Instant endsAt = startsAt.plus(movie.getRuntime());

        if (showRepository.existsByScreenAndStartsAtLessThanAndEndsAtGreaterThan(screen, endsAt, startsAt)) {
            throw new ConflictException("The selected time slot is already occupied for this screen");
        }

        Show show = new Show();
        show.setScreen(screen);
        show.setTheater(theater);
        show.setMovie(movie);
        show.setStartsAt(startsAt);
        show.setEndsAt(endsAt);

        return showMapper.toShowResponse(showRepository.save(show));
    }
}
