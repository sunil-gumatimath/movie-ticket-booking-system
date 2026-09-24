package com.example.movieticketbookingsystem.controller;

import com.example.movieticketbookingsystem.dto.request.TheaterRequest;
import com.example.movieticketbookingsystem.dto.response.TheaterResponse;
import com.example.movieticketbookingsystem.service.TheaterService;
import com.example.movieticketbookingsystem.utility.ResponseStructure;
import com.example.movieticketbookingsystem.utility.RestResponseBuilder;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
public class TheaterController {

    private final TheaterService theaterService;
    private final RestResponseBuilder restResponseBuilder;

    @PreAuthorize("hasAuthority('ROLE_THEATER_OWNER')")
    @PostMapping("/theater/register")
    public ResponseEntity<ResponseStructure<TheaterResponse>> createTheater(
            @Valid @RequestBody TheaterRequest theaterRequest) {
        TheaterResponse createdTheater = theaterService.createTheater(theaterRequest);
        return restResponseBuilder.success(HttpStatus.CREATED, "Theater Created", createdTheater);
    }

    @GetMapping("/theater/{id}")
    public ResponseEntity<ResponseStructure<TheaterResponse>> findTheater(@PathVariable String id) {
        TheaterResponse theater = theaterService.findTheater(id);
        return restResponseBuilder.success(HttpStatus.OK, "Theater Found", theater);
    }

    @PreAuthorize("hasAuthority('ROLE_THEATER_OWNER')")
    @PutMapping("/theater/{id}")
    public ResponseEntity<ResponseStructure<TheaterResponse>> updateTheater(
            @PathVariable String id,
            @Valid @RequestBody TheaterRequest theaterRequest) {
        TheaterResponse updatedTheater = theaterService.updateTheater(id, theaterRequest);
        return restResponseBuilder.success(HttpStatus.OK, "Theater Updated", updatedTheater);
    }
}
