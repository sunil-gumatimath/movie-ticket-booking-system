package com.example.movieticketbookingsystem.controller;

import com.example.movieticketbookingsystem.dto.request.ScreenRequest;
import com.example.movieticketbookingsystem.dto.response.ScreenResponse;
import com.example.movieticketbookingsystem.entity.Screen;
import com.example.movieticketbookingsystem.entity.Theater;
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
    public ResponseEntity<ResponseStructure<ScreenResponse>> findScreen(@Valid @RequestParam String screenId,@RequestBody ScreenRequest screenRequest){
        ScreenResponse findScreen = screenService.findScreen(screenId,screenRequest);
        return RestResponseBuilder.ok("Screen Found",findScreen,HttpStatus.OK);
    }
}
