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
import com.example.movieticketbookingsystem.repository.TheaterOwnerRepository;
import com.example.movieticketbookingsystem.repository.TheaterRepository;
import com.example.movieticketbookingsystem.repository.UserRepository;
import com.example.movieticketbookingsystem.service.TheaterService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class TheaterServiceImpl implements TheaterService {

    private final TheaterRepository theaterRepository;
    private final UserRepository userRepository;
    private final TheaterOwnerRepository theaterOwnerRepository;
    private final TheaterMapper theaterMapper;

    @Override
    public TheaterResponse createTheater(String email, TheaterRequest theaterRequest) {
        UserDetails userDetails = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundByEmailException("User not found with email: " + email));

        if (userDetails.getUserRole() != UserRole.ROLE_THEATER_OWNER) {
            throw new IllegalArgumentException("User is not a theater owner");
        }

        if (userDetails.isDeleted()) {
            throw new IllegalStateException("Cannot create theater for deleted user");
        }

        TheaterOwner theaterOwner = (TheaterOwner) userDetails;

        Theater newTheater = new Theater();
        newTheater.setName(theaterRequest.name());
        newTheater.setAddress(theaterRequest.address());
        newTheater.setCity(theaterRequest.city());
        newTheater.setLandmark(theaterRequest.landmark());
        newTheater.setOwner(theaterOwner);
        // CreatedAt and CreatedBy will be handled by JPA auditing

        // Add the new theater to the theater owner's list of theaters
        if (theaterOwner.getTheaters() == null) {
            theaterOwner.setTheaters(new ArrayList<>());
        }
        theaterOwner.getTheaters().add(newTheater);

        Theater savedTheater = theaterRepository.save(newTheater);
        return theaterMapper.toTheaterResponse(savedTheater);
    }

    @Override
    public TheaterResponse findTheater(String id) {
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new TheaterOwnerIdException("Theater not found with id: " + id));

        return theaterMapper.toTheaterResponse(theater);
    }


    @Override
    public TheaterResponse updateTheater(String id, TheaterRequest theaterRequest) {
        Theater existingTheater = theaterRepository.findById(id)
                .orElseThrow(() -> new TheaterOwnerIdException("Theater not found with ID: " + id));

        // Update theater fields with the provided data
        existingTheater.setName(theaterRequest.name());
        existingTheater.setAddress(theaterRequest.address());
        existingTheater.setCity(theaterRequest.city());
        existingTheater.setLandmark(theaterRequest.landmark());
        // UpdatedAt will be handled by JPA auditing

        // Save the updated theater
        Theater savedTheater = theaterRepository.save(existingTheater);

        return theaterMapper.toTheaterResponse(savedTheater);
    }


}