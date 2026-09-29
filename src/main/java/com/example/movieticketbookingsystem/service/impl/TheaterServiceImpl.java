package com.example.movieticketbookingsystem.service.impl;

import com.example.movieticketbookingsystem.dto.request.TheaterRequest;
import com.example.movieticketbookingsystem.dto.response.TheaterResponse;
import com.example.movieticketbookingsystem.entity.Theater;
import com.example.movieticketbookingsystem.entity.TheaterOwner;
import com.example.movieticketbookingsystem.exception.ResourceNotFoundException;
import com.example.movieticketbookingsystem.mapper.TheaterMapper;
import com.example.movieticketbookingsystem.repository.TheaterRepository;
import com.example.movieticketbookingsystem.security.CurrentUserService;
import com.example.movieticketbookingsystem.service.TheaterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TheaterServiceImpl implements TheaterService {

    private final TheaterRepository theaterRepository;
    private final TheaterMapper theaterMapper;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public TheaterResponse createTheater(TheaterRequest theaterRequest) {
        TheaterOwner owner = currentUserService.currentTheaterOwner();

        Theater theater = new Theater();
        applyDetails(theater, theaterRequest);
        theater.setOwner(owner);
        owner.getTheaters().add(theater);

        return theaterMapper.toTheaterResponse(theaterRepository.save(theater));
    }

    @Override
    @Transactional(readOnly = true)
    public TheaterResponse findTheater(String id) {
        return theaterMapper.toTheaterResponse(findById(id));
    }

    @Override
    @Transactional
    public TheaterResponse updateTheater(String id, TheaterRequest theaterRequest) {
        Theater theater = findById(id);
        currentUserService.requireOwnerOf(theater);
        applyDetails(theater, theaterRequest);
        return theaterMapper.toTheaterResponse(theater);
    }

    private Theater findById(String id) {
        return theaterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theater", id));
    }

    private void applyDetails(Theater theater, TheaterRequest request) {
        theater.setName(request.name().trim());
        theater.setAddress(request.address().trim());
        theater.setCity(request.city().trim());
        theater.setLandmark(request.landmark().trim());
    }
}
