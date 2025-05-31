package com.example.movieticketbookingsystem.controller;

import com.example.movieticketbookingsystem.dto.request.ScreenRequest;
import com.example.movieticketbookingsystem.dto.response.ScreenResponse;
import com.example.movieticketbookingsystem.dto.response.ScreenResponseList;
import com.example.movieticketbookingsystem.serviceImpl.ScreenServiceImpl;
import com.example.movieticketbookingsystem.utility.ResponseStructure;
import com.example.movieticketbookingsystem.utility.RestResponseBuilder;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class ScreenController {

    private final ScreenServiceImpl screenService;

    @PostMapping("/screen")
    public ResponseEntity<ResponseStructure<ScreenResponse>> addScreen(
            @RequestParam String theaterId,
            @Valid @RequestBody ScreenRequest screenRequest){
        ScreenResponse addScreen = screenService.addScreen(theaterId, screenRequest);
        return new RestResponseBuilder().success(HttpStatus.CREATED, "Screen Created", addScreen);
    }

    @GetMapping("/screen/{screenId}")
    public ResponseEntity<ResponseStructure<ScreenResponseList>> findScreen(@PathVariable String screenId){
        ScreenResponseList findScreen = screenService.findScreen(screenId);
        return new RestResponseBuilder().success(HttpStatus.OK, "Screen Found", findScreen);
    }
}
