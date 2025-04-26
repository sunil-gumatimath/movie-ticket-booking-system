package com.example.movieticketbookingsystem.dto.request;

import com.example.movieticketbookingsystem.entity.Screen;
import com.example.movieticketbookingsystem.enums.ScreenType;

public record ScreenRequest(
      ScreenType screenType,
      Integer capacity,
      Integer noOfRows
) {
}
