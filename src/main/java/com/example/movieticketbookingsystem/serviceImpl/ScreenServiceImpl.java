package com.example.movieticketbookingsystem.serviceImpl;

import com.example.movieticketbookingsystem.dto.request.ScreenRequest;
import com.example.movieticketbookingsystem.dto.response.ScreenResponse;
import com.example.movieticketbookingsystem.dto.response.ScreenResponseList;
import com.example.movieticketbookingsystem.entity.Screen;
import com.example.movieticketbookingsystem.entity.Seat;
import com.example.movieticketbookingsystem.entity.Theater;
import com.example.movieticketbookingsystem.exception.ScreenIdNotFoundException;
import com.example.movieticketbookingsystem.exception.TheaterOwnerIdException;
import com.example.movieticketbookingsystem.mapper.ScreenMapper;
import com.example.movieticketbookingsystem.repository.ScreenRepository;
import com.example.movieticketbookingsystem.repository.TheaterRepository;
import com.example.movieticketbookingsystem.service.ScreenService;
import com.example.movieticketbookingsystem.service.SeatService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ScreenServiceImpl implements ScreenService {

    private final ScreenRepository screenRepository;
    private final TheaterRepository theaterRepository;
    private final SeatService seatService;
    private final ScreenMapper screenMapper;

    @Override
    @Transactional
    public ScreenResponse addScreen(String theaterId, ScreenRequest screenRequest) {
        validateScreenRequest(screenRequest);

        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new TheaterOwnerIdException("Theater not found with ID: " + theaterId));
        requireTheaterOwner(theater);

        Screen screen = new Screen();
        screen.setScreenType(screenRequest.screenType());
        screen.setCapacity(screenRequest.capacity());
        screen.setNoOfRows(screenRequest.noOfRows());
        screen.setTheater(theater);

        if (theater.getScreen() == null) {
            theater.setScreen(new ArrayList<>());
        }
        theater.getScreen().add(screen);

        Screen savedScreen = screenRepository.save(screen);
        seatService.generateSeatLayout(savedScreen);
        return screenMapper.toScreenResponse(savedScreen);
    }

    @Override
    @Transactional(readOnly = true)
    public ScreenResponseList findScreen(String screenId) {
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new ScreenIdNotFoundException("Screen not found with ID: " + screenId));

        List<Seat> seatList = screen.getSeats() == null ? List.of() : screen.getSeats();
        return screenMapper.toScreenResponseList(screen, seatList);
    }

    private void validateScreenRequest(ScreenRequest request) {
        if (request == null || request.capacity() == null || request.noOfRows() == null
                || request.capacity() <= 0 || request.noOfRows() <= 0
                || request.capacity() > 1000 || request.noOfRows() > 26) {
            throw new IllegalArgumentException("Screen capacity and rows must be within the supported range");
        }
        if (request.capacity() % request.noOfRows() != 0) {
            throw new IllegalArgumentException("Capacity must be evenly divisible by number of rows");
        }
    }

    private void requireTheaterOwner(Theater theater) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Authentication is required");
        }

        if (theater.getOwner() == null
                || !theater.getOwner().getEmail().equalsIgnoreCase(authentication.getName())) {
            throw new AccessDeniedException("You may only manage your own theater screens");
        }
    }
}
