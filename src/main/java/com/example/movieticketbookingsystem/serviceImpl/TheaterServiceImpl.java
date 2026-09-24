package com.example.movieticketbookingsystem.serviceImpl;

import com.example.movieticketbookingsystem.dto.request.TheaterRequest;
import com.example.movieticketbookingsystem.dto.response.TheaterResponse;
import com.example.movieticketbookingsystem.entity.Theater;
import com.example.movieticketbookingsystem.entity.TheaterOwner;
import com.example.movieticketbookingsystem.entity.UserDetails;
import com.example.movieticketbookingsystem.enums.UserRole;
import com.example.movieticketbookingsystem.exception.TheaterOwnerIdException;
import com.example.movieticketbookingsystem.exception.UserNotFoundByEmailException;
import com.example.movieticketbookingsystem.mapper.TheaterMapper;
import com.example.movieticketbookingsystem.repository.TheaterRepository;
import com.example.movieticketbookingsystem.repository.UserRepository;
import com.example.movieticketbookingsystem.service.TheaterService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class TheaterServiceImpl implements TheaterService {

    private final TheaterRepository theaterRepository;
    private final UserRepository userRepository;
    private final TheaterMapper theaterMapper;

    @Override
    public TheaterResponse createTheater(TheaterRequest theaterRequest) {
        validateTheaterRequest(theaterRequest);
        TheaterOwner theaterOwner = currentTheaterOwner();
        if (theaterOwner.isDeleted()) {
            throw new IllegalStateException("Cannot create theater for deleted user");
        }

        Theater newTheater = new Theater();
        newTheater.setName(theaterRequest.name().trim());
        newTheater.setAddress(theaterRequest.address().trim());
        newTheater.setCity(theaterRequest.city().trim());
        newTheater.setLandmark(theaterRequest.landmark().trim());
        newTheater.setOwner(theaterOwner);

        if (theaterOwner.getTheaters() == null) {
            theaterOwner.setTheaters(new java.util.ArrayList<>());
        }
        theaterOwner.getTheaters().add(newTheater);

        return theaterMapper.toTheaterResponse(theaterRepository.save(newTheater));
    }

    @Override
    @Transactional(readOnly = true)
    public TheaterResponse findTheater(String id) {
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new TheaterOwnerIdException("Theater not found with id: " + id));
        return theaterMapper.toTheaterResponse(theater);
    }

    @Override
    public TheaterResponse updateTheater(String id, TheaterRequest theaterRequest) {
        validateTheaterRequest(theaterRequest);
        Theater existingTheater = theaterRepository.findById(id)
                .orElseThrow(() -> new TheaterOwnerIdException("Theater not found with ID: " + id));

        requireTheaterOwner(existingTheater);

        existingTheater.setName(theaterRequest.name().trim());
        existingTheater.setAddress(theaterRequest.address().trim());
        existingTheater.setCity(theaterRequest.city().trim());
        existingTheater.setLandmark(theaterRequest.landmark().trim());

        return theaterMapper.toTheaterResponse(theaterRepository.save(existingTheater));
    }

    private void validateTheaterRequest(TheaterRequest request) {
        if (request == null || request.name() == null || request.name().isBlank()
                || request.address() == null || request.address().isBlank()
                || request.city() == null || request.city().isBlank()
                || request.landmark() == null || request.landmark().isBlank()) {
            throw new IllegalArgumentException("Theater name, address, city, and landmark are required");
        }
    }

    private TheaterOwner currentTheaterOwner() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Authentication is required");
        }

        UserDetails userDetails = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UserNotFoundByEmailException(
                        "User not found with email: " + authentication.getName()));
        if (userDetails.isDeleted()) {
            throw new AccessDeniedException("Deleted users cannot manage theaters");
        }
        if (userDetails.getUserRole() != UserRole.ROLE_THEATER_OWNER) {
            throw new AccessDeniedException("Theater owner authority is required");
        }
        if (!(userDetails instanceof TheaterOwner theaterOwner)) {
            throw new AccessDeniedException("User account is not a theater owner");
        }
        return theaterOwner;
    }

    private void requireTheaterOwner(Theater theater) {
        TheaterOwner currentOwner = currentTheaterOwner();
        if (theater.getOwner() == null
                || !currentOwner.getUserId().equals(theater.getOwner().getUserId())) {
            throw new AccessDeniedException("You may only modify your own theaters");
        }
    }
}
