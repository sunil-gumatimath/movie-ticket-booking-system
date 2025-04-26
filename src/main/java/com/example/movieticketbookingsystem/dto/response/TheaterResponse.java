package com.example.movieticketbookingsystem.dto.response;

import jakarta.validation.constraints.NotBlank;

public record TheaterResponse(
        @NotBlank(message = "Name must not be blank")
        String name,

        @NotBlank(message = "Address must not be blank")
        String address,

        @NotBlank(message = "City must not be blank")
        String city,

        @NotBlank(message = "Landmark must not be blank")
        String landmark
) {
}