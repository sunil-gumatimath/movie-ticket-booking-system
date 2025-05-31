package com.example.movieticketbookingsystem.dto.response;

import com.example.movieticketbookingsystem.entity.Seat;
import com.example.movieticketbookingsystem.enums.ScreenType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ScreenResponseList(
        String screenId,
        ScreenType screenType,
        Integer capacity,
        Integer noOfRows,
        List<SeatResponse> seatResponses
) {
}
