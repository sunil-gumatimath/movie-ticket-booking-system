package com.example.movieticketbookingsystem.dto.response;

import com.example.movieticketbookingsystem.enums.Certificate;
import com.example.movieticketbookingsystem.enums.Genre;

import java.time.Duration;
import java.util.Set;

public record MovieResponse(
        String movieId,
        String title,
        String description,
        String ratings,
        Duration runtime,
        Certificate certificate,
        Genre genre,
        Set<String> castList
) {
}
