package com.example.movieticketbookingsystem.dto.response;

import com.example.movieticketbookingsystem.entity.Seat;
import com.example.movieticketbookingsystem.enums.ScreenType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ScreenResponseList(
//        @NotNull(message = "Screen type must not be null")
        ScreenType screenType,

//        @NotNull(message = "Capacity must not be null")
//        @Min(value = 1, message = "Capacity must be at least 1")
        Integer capacity,

//        @NotNull(message = "Number of rows must not be null")
//        @Min(value = 1, message = "Number of rows must be at least 1")
        Integer noOfRows,

        List<SeatResponse> seatResponses
) {
}
