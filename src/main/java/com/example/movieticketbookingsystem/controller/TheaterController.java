package com.example.movieticketbookingsystem.controller;

import com.example.movieticketbookingsystem.dto.request.TheaterRequest;
import com.example.movieticketbookingsystem.dto.response.TheaterResponse;
import com.example.movieticketbookingsystem.entity.Theater;
import com.example.movieticketbookingsystem.entity.TheaterOwner;
import com.example.movieticketbookingsystem.serviceImpl.TheaterServiceImpl;
import com.example.movieticketbookingsystem.utility.ResponseStructure;
import com.example.movieticketbookingsystem.utility.RestResponseBuilder;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class TheaterController {

    private final TheaterServiceImpl theaterService;

    @PreAuthorize("hasAuthority('ROLE_THEATER_OWNER')")
    @PostMapping("/theater/register")
    public ResponseEntity<ResponseStructure<TheaterResponse>> createTheater(
            @Valid @RequestBody TheaterRequest theaterRequest,
            @RequestParam String email) {
        TheaterResponse createTheater = theaterService.createTheater(email, theaterRequest);
        return new RestResponseBuilder().success(HttpStatus.CREATED, "Theater Created", createTheater);
    }

    @GetMapping("/theater/{id}")
    public ResponseEntity<ResponseStructure<TheaterResponse>> findTheater(@PathVariable String id){
        TheaterResponse findTheater = theaterService.findTheater(id);
        return new RestResponseBuilder().success(HttpStatus.OK, "Theater Found", findTheater);
    }

    @PreAuthorize("hasAuthority('ROLE_THEATER_OWNER')")
    @PutMapping("/theater/{id}")
    public ResponseEntity<ResponseStructure<TheaterResponse>> updateTheater(
            @PathVariable String id,
            @Valid @RequestBody TheaterRequest theaterRequest){
        TheaterResponse updateTheater = theaterService.updateTheater(id, theaterRequest);
        return new RestResponseBuilder().success(HttpStatus.OK, "Theater Updated", updateTheater);
    }
}
