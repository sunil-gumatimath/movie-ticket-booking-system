package com.example.movieticketbookingsystem.dto.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record MovieCastUpdateRequest(
        @NotEmpty(message = "Movie cast list cannot be empty")
        Set<String> castList
) {
}
