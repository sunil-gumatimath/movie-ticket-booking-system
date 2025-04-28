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
    @PostMapping("/create")
    public ResponseEntity<ResponseStructure<TheaterResponse>> createTheater(@RequestBody TheaterRequest theaterRequest, @Valid @RequestParam String email) {
        TheaterResponse createTheater = theaterService.createTheater(email, theaterRequest);
        return RestResponseBuilder.created("Theater Created", createTheater, HttpStatus.CREATED);
    }

    @GetMapping("/theater/id")
    public ResponseEntity<ResponseStructure<TheaterResponse>> findTheater(@Valid @RequestParam String id){
        TheaterResponse findTheater = theaterService.findTheater(id);
        return RestResponseBuilder.ok("Theater Found",findTheater,HttpStatus.OK);
    }

    @PutMapping("/theater/update")
    public ResponseEntity<ResponseStructure<TheaterResponse>> updateTheater(@Valid @RequestParam String id, @RequestBody TheaterRequest theaterRequest){
        TheaterResponse updateTheater = theaterService.updateTheater(id,theaterRequest);
        return RestResponseBuilder.ok("Theater Updated",updateTheater,HttpStatus.OK);
    }
}
