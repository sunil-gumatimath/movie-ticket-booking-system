package com.example.movieticketbookingsystem.controller;

import com.example.movieticketbookingsystem.dto.request.ScreenRequest;
import com.example.movieticketbookingsystem.dto.response.ScreenResponse;
import com.example.movieticketbookingsystem.dto.response.ScreenDetailResponse;
import com.example.movieticketbookingsystem.service.ScreenService;
import com.example.movieticketbookingsystem.utility.ResponseStructure;
import com.example.movieticketbookingsystem.utility.RestResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ScreenController {

    private final ScreenService screenService;
    private final RestResponseBuilder restResponseBuilder;

    @PreAuthorize("hasAuthority('ROLE_THEATER_OWNER')")
    @PostMapping("/screen")
    public ResponseEntity<ResponseStructure<ScreenResponse>> addScreen(
            @RequestParam String theaterId,
            @Valid @RequestBody ScreenRequest screenRequest) {
        ScreenResponse screen = screenService.addScreen(theaterId, screenRequest);
        return restResponseBuilder.success(HttpStatus.CREATED, "Screen Created", screen);
    }

    @GetMapping("/screen/{screenId}")
    public ResponseEntity<ResponseStructure<ScreenDetailResponse>> findScreen(@PathVariable String screenId) {
        ScreenDetailResponse screen = screenService.findScreen(screenId);
        return restResponseBuilder.success(HttpStatus.OK, "Screen Found", screen);
    }
}
