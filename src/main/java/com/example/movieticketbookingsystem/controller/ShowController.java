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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class ShowController {

    private final ShowServiceImpl showService;

    @PostMapping("/create/show")
    public ResponseEntity<ResponseStructure<ShowResponse>> addShow(@Valid @RequestBody ShowRequest showRequest, @RequestParam String screenId){
        ShowResponse showResponse = showService.addShow(showRequest, screenId);
        return RestResponseBuilder.created("Show Created", showResponse, HttpStatus.CREATED);
    }
}
