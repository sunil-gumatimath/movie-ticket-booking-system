package com.example.movieticketbookingsystem.dto.response;

public record ShowResponse(
        String showId,
        String movieTitle,
        String theaterName,
        String screenId,
        Long startTimeEpochMillis,
        Long endTimeEpochMillis
) {}
