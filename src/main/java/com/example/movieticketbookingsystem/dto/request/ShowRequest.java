package com.example.movieticketbookingsystem.dto.request;

import jakarta.validation.constraints.NotNull;

public record ShowRequest(

        @NotNull(message = "Start time is required (epoch millis)")
        Long startTimeEpochMillis,

        @NotNull(message = "Movie ID must not be null")
        String movieId

) {}
