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
    public ResponseEntity<ResponseStructure<ScreenResponse>> addScreen(@Valid @RequestParam String theaterId, @RequestBody ScreenRequest screenRequest){
        ScreenResponse addScreen = screenService.addScreen(theaterId,screenRequest);
        return RestResponseBuilder.created("Screen Created", addScreen, HttpStatus.CREATED);
    }

    @GetMapping("/screen/id")
    public ResponseEntity<ResponseStructure<ScreenResponseList>> findScreen(@Valid @RequestParam String screenId){
        ScreenResponseList findScreen = screenService.findScreen(screenId);
        return RestResponseBuilder.ok("Screen Found",findScreen,HttpStatus.OK);
    }
}
