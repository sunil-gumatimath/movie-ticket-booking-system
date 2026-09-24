package com.example.movieticketbookingsystem.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MovieDescriptionUpdateRequest(
        @NotBlank(message = "Movie description cannot be blank")
        String description
) {
}
