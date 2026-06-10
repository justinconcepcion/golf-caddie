package com.golfcaddie.backend.round.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record LogShotRequest(
        @NotNull(message = "holeNumber is required")
        @Min(value = 1, message = "holeNumber must be at least 1")
        Integer holeNumber,

        @NotNull(message = "lat is required")
        @DecimalMin(value = "-90.0", message = "lat out of range")
        @DecimalMax(value = "90.0", message = "lat out of range")
        Double lat,

        @NotNull(message = "lng is required")
        @DecimalMin(value = "-180.0", message = "lng out of range")
        @DecimalMax(value = "180.0", message = "lng out of range")
        Double lng) {
}
