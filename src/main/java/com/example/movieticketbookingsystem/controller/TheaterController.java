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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class TheaterController {

    private final TheaterServiceImpl theaterService;

    @PostMapping("/create")
    public ResponseEntity<ResponseStructure<TheaterResponse>> createTheater(@RequestBody TheaterRequest theaterRequest, @Valid @RequestParam String email) {
        TheaterResponse createTheater = theaterService.createTheater(email, theaterRequest);
        return RestResponseBuilder.created("Theater Created", createTheater, HttpStatus.CREATED);
    }
}
