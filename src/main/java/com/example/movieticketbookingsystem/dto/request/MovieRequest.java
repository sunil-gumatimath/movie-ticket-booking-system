package com.example.movieticketbookingsystem.dto.request;

import com.example.movieticketbookingsystem.enums.Certificate;
import com.example.movieticketbookingsystem.enums.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.time.DurationMax;
import org.hibernate.validator.constraints.time.DurationMin;

import java.time.Duration;
import java.util.Set;

public record MovieRequest(

        @NotBlank(message = "Movie title cannot be blank")
        String title,

        @NotBlank(message = "Movie description cannot be blank")
        String description,

        @NotNull(message = "Movie runtime is required")
        @DurationMin(nanos = 1, message = "Movie runtime must be positive")
        @DurationMax(hours = 24, message = "Movie runtime must be no longer than 24 hours")
        Duration runtime,

        @NotNull(message = "Movie certificate is required")
        Certificate certificate,

        @NotNull(message = "Movie genre is required")
        Genre genre,

        @NotEmpty(message = "Movie cast list cannot be empty")
        Set<@NotBlank(message = "Cast member names cannot be blank") String> castList
) {
}
