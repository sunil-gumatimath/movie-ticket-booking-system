package com.example.movieticketbookingsystem.serviceImpl;

import com.example.movieticketbookingsystem.dto.request.ScreenRequest;
import com.example.movieticketbookingsystem.dto.response.ScreenResponse;
import com.example.movieticketbookingsystem.dto.response.ScreenResponseList;
import com.example.movieticketbookingsystem.dto.response.SeatResponse;
import com.example.movieticketbookingsystem.entity.Screen;
import com.example.movieticketbookingsystem.entity.Seat;
import com.example.movieticketbookingsystem.entity.Theater;
import com.example.movieticketbookingsystem.exception.ScreenIdNotFoundException;
import com.example.movieticketbookingsystem.exception.TheaterOwnerIdException;
import com.example.movieticketbookingsystem.mapper.ScreenMapper;
import com.example.movieticketbookingsystem.repository.ScreenRepository;
import com.example.movieticketbookingsystem.repository.TheaterRepository;
import com.example.movieticketbookingsystem.service.ScreenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ScreenServiceImpl implements ScreenService {

    private final ScreenRepository screenRepository;
    private final TheaterRepository theaterRepository;
    private final SeatServiceImpl seatService;
    private final ScreenMapper screenMapper;

    @Override
    public ScreenResponse addScreen(String theaterId, ScreenRequest screenRequest) {
        Optional<Theater> theaterOptional = theaterRepository.findById(theaterId);

        Theater theater = theaterOptional.orElseThrow(() ->
            new TheaterOwnerIdException("Theater not found with ID: " + theaterId));

        // Validate capacity and rows
        if (screenRequest.capacity() % screenRequest.noOfRows() != 0) {
            throw new IllegalArgumentException("Capacity must be evenly divisible by number of rows");
        }

        Screen screen = new Screen();
        screen.setScreenType(screenRequest.screenType());
        screen.setCapacity(screenRequest.capacity());
        screen.setNoOfRows(screenRequest.noOfRows());
        screen.setTheater(theater);

        // Add screen to theater's screen list
        if (theater.getScreen() == null) {
            theater.setScreen(new ArrayList<>());
        }
        theater.getScreen().add(screen);

        // Save screen first to get ID
        Screen savedScreen = screenRepository.save(screen);

        // Generate seat layout
        seatService.generateSeatLayout(savedScreen);

        return screenMapper.toScreenResponse(savedScreen);
    }

    @Override
    public ScreenResponseList findScreen(String screenId) {
        Screen screen = screenRepository.findById(screenId)
            .orElseThrow(() -> new ScreenIdNotFoundException("Screen not found with ID: " + screenId));

        List<Seat> seatList = screen.getSeats();
        if (seatList == null) {
            seatList = new ArrayList<>();
        }

        return screenMapper.toScreenResponseList(screen, seatList);
    }
}
