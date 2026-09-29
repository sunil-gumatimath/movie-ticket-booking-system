package com.example.movieticketbookingsystem.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TheaterRequest(

        @NotBlank(message = "Theater name cannot be blank")
        String name,

        @NotBlank(message = "Address cannot be blank")
        String address,

        @NotBlank(message = "City cannot be blank")
        String city,

        @NotBlank(message = "Landmark cannot be blank")
        String landmark
) {
}
