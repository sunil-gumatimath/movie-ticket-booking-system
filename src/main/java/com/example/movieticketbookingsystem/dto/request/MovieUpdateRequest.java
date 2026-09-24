package com.example.movieticketbookingsystem.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MovieUpdateRequest(
        @NotBlank(message = "Movie title cannot be blank")
        String title
) {
}
