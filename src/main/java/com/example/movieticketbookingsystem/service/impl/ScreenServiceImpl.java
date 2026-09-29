package com.example.movieticketbookingsystem.service.impl;

import com.example.movieticketbookingsystem.dto.request.ScreenRequest;
import com.example.movieticketbookingsystem.dto.response.ScreenDetailResponse;
import com.example.movieticketbookingsystem.dto.response.ScreenResponse;
import com.example.movieticketbookingsystem.entity.Screen;
import com.example.movieticketbookingsystem.entity.Theater;
import com.example.movieticketbookingsystem.exception.ResourceNotFoundException;
import com.example.movieticketbookingsystem.mapper.ScreenMapper;
import com.example.movieticketbookingsystem.repository.ScreenRepository;
import com.example.movieticketbookingsystem.repository.TheaterRepository;
import com.example.movieticketbookingsystem.security.CurrentUserService;
import com.example.movieticketbookingsystem.service.ScreenService;
import com.example.movieticketbookingsystem.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScreenServiceImpl implements ScreenService {

    private final ScreenRepository screenRepository;
    private final TheaterRepository theaterRepository;
    private final SeatService seatService;
    private final ScreenMapper screenMapper;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public ScreenResponse addScreen(String theaterId, ScreenRequest screenRequest) {
        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new ResourceNotFoundException("Theater", theaterId));
        currentUserService.requireOwnerOf(theater);

        Screen screen = new Screen();
        screen.setScreenType(screenRequest.screenType());
        screen.setCapacity(screenRequest.capacity());
        screen.setNoOfRows(screenRequest.noOfRows());
        screen.setTheater(theater);
        theater.getScreens().add(screen);

        Screen savedScreen = screenRepository.save(screen);
        seatService.generateSeatLayout(savedScreen);
        return screenMapper.toScreenResponse(savedScreen);
    }

    @Override
    @Transactional(readOnly = true)
    public ScreenDetailResponse findScreen(String screenId) {
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new ResourceNotFoundException("Screen", screenId));
        return screenMapper.toScreenDetailResponse(screen, screen.getSeats());
    }
}
