package com.example.movieticketbookingsystem.serviceImpl;

import com.example.movieticketbookingsystem.dto.request.ShowRequest;
import com.example.movieticketbookingsystem.dto.response.ShowResponse;
import com.example.movieticketbookingsystem.entity.Movie;
import com.example.movieticketbookingsystem.entity.Screen;
import com.example.movieticketbookingsystem.entity.Show;
import com.example.movieticketbookingsystem.entity.Theater;
import com.example.movieticketbookingsystem.exception.ResourceNotFoundException;
import com.example.movieticketbookingsystem.repository.MovieRepository;
import com.example.movieticketbookingsystem.repository.ScreenRepository;
import com.example.movieticketbookingsystem.repository.ShowRepository;
import com.example.movieticketbookingsystem.repository.TheaterRepository;
import com.example.movieticketbookingsystem.service.ShowService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@AllArgsConstructor
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;
    private final ScreenRepository screenRepository;
    private final TheaterRepository theaterRepository;
    private final MovieRepository movieRepository;


    @Override
    public ShowResponse addShow(ShowRequest showRequest, String screenId) {
        Screen screen = screenRepository.findById(screenId).orElseThrow(() -> new ResourceNotFoundException("Screen not found"));

        Theater theater = screen.getTheater();
        if (theater == null){
            throw new IllegalStateException("Screen is not associated with a theater");
        }

        Movie movie = movieRepository.findById(showRequest.movieId()).orElseThrow(()->new ResourceNotFoundException("Movie not found"));

        LocalDateTime startTime = Instant.ofEpochMilli(showRequest.startTimeEpochMillis())
                .atZone(ZoneId.of("UTC")).toLocalDateTime();

        LocalDateTime endTime = startTime.plus(movie.getRuntime());

        // 6. Check if time slot is available
        boolean isOccupied = showRepository.existsByScreenAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                screen, endTime, startTime
        );

        if (isOccupied){
            throw new IllegalStateException("Time slot is already occupied");
        }

        Show show = new Show();
        show.setScreen(screen);
        show.setTheater(theater);
        show.setMovie(movie);
        show.setStartsAt(startTime.toInstant(ZoneId.of("UTC").getRules().getOffset(startTime)));
        show.setEndsAt(endTime.toInstant(ZoneId.of("UTC").getRules().getOffset(endTime)));

        showRepository.save(show);

        return new ShowResponse(
                show.getShowId(),
                movie.getTitle(),
                theater.getName(),
                screen.getScreenId(),
                show.getStartsAt().toEpochMilli(),
                show.getEndsAt().toEpochMilli()
        );
    }
}
