package com.example.movieticketbookingsystem.dto.request;

import com.example.movieticketbookingsystem.enums.ScreenType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ScreenRequest(
        @NotNull(message = "Screen type must not be null")
        ScreenType screenType,

        @NotNull(message = "Capacity must not be null")
        @Min(value = 1, message = "Capacity must be at least 1")
        @Max(value = 1000, message = "Capacity must not exceed 1000")
        Integer capacity,

        // Rows are lettered A-Z, so at most 26.
        @NotNull(message = "Number of rows must not be null")
        @Min(value = 1, message = "Number of rows must be at least 1")
        @Max(value = 26, message = "Number of rows must not exceed 26")
        Integer noOfRows
) {

    /** Every row must have the same number of seats. Null/range errors are reported by the field constraints. */
    @AssertTrue(message = "Capacity must be evenly divisible by number of rows")
    public boolean isCapacityDivisibleByRows() {
        return capacity == null || noOfRows == null || noOfRows <= 0 || capacity % noOfRows == 0;
    }
}
