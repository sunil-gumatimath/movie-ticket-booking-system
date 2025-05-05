package com.example.movieticketbookingsystem.controller;

import com.example.movieticketbookingsystem.dto.request.ShowRequest;
import com.example.movieticketbookingsystem.dto.response.ShowResponse;
import com.example.movieticketbookingsystem.serviceImpl.ShowServiceImpl;
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
@RequestMapping("/theaters")
public class ShowController {

    private final ShowServiceImpl showService;

    @PostMapping("/{theaterId}/screens/{screenId}/shows")
    @PreAuthorize("hasRole('THEATER_OWNER')")
    public ResponseEntity<ResponseStructure<ShowResponse>> addShow(
            @PathVariable String theaterId,
            @PathVariable String screenId,
            @Valid @RequestBody ShowRequest showRequest) {

        ShowResponse showResponse = showService.addShow(showRequest, theaterId, screenId);
        return RestResponseBuilder.created("Show Created", showResponse, HttpStatus.CREATED);
    }
}
