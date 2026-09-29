package com.example.movieticketbookingsystem.dto.response;

import com.example.movieticketbookingsystem.enums.Certificate;
import com.example.movieticketbookingsystem.enums.Genre;

import java.time.Duration;
import java.util.Set;

/**
 * @param ratings average feedback rating (0 when there is no feedback), rounded to two decimals
 */
public record MovieResponse(
        String movieId,
        String title,
        String description,
        double ratings,
        Duration runtime,
        Certificate certificate,
        Genre genre,
        Set<String> castList
) {
}
