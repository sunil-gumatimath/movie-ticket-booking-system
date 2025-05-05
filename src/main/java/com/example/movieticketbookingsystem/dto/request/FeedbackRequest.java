package com.example.movieticketbookingsystem.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FeedbackRequest(

        @NotNull(message = "Rating is required.")
        @Min(value = 1,message = "Rating should be greater than or equal to 1.")
        @Max(value = 5,message = "Rating should be less than or equal to 5.")
        int rating,

        @NotBlank(message = "Review cannot be blank.")
        String review
) {
}
