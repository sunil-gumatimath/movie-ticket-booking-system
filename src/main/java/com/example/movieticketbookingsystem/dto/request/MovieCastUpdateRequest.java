package com.example.movieticketbookingsystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record MovieCastUpdateRequest(
        @NotEmpty(message = "Movie cast list cannot be empty")
        Set<@NotBlank(message = "Cast member names cannot be blank") String> castList
) {
}
