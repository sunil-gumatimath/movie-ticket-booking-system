package com.example.movieticketbookingsystem.dto.response;

import com.example.movieticketbookingsystem.enums.ScreenType;

public record ScreenResponse(
        String screenId,
        ScreenType screenType,
        Integer capacity,
        Integer noOfRows
) {
}
