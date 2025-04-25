package com.example.movieticketbookingsystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TheaterRequest(

//        @NotNull(message = "Theater name cannot be null")
//        @NotBlank(message = "Theater name cannot be blank")
        String name,

//        @NotNull(message = "Address cannot be null")
//        @NotBlank(message = "Address cannot be blank")
        String address,

//        @NotNull(message = "City cannot be null")
//        @NotBlank(message = "City cannot be blank")
        String city,

//        @NotNull(message = "Landmark cannot be null")
//        @NotBlank(message = "Landmark cannot be blank")
        String landmark
) {
}