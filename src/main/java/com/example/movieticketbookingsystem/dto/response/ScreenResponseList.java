package com.example.movieticketbookingsystem.dto.response;

import com.example.movieticketbookingsystem.enums.ScreenType;

import java.util.List;

public record ScreenResponseList(
        String screenId,
        ScreenType screenType,
        Integer capacity,
        Integer noOfRows,
        List<SeatResponse> seatResponses
) {
}
