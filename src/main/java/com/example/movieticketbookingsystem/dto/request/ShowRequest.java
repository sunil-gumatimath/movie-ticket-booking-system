package com.example.movieticketbookingsystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ShowRequest(

        @NotNull(message = "Start time is required (epoch millis)")
        Long startTimeEpochMillis,

        @NotBlank(message = "Movie ID must not be blank")
        String movieId

) {}
