package com.golfcaddie.backend.advice.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdviceRequest(
        @NotBlank(message = "courseId is required") String courseId,

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
        Double lng,

        @Min(value = 1, message = "shotNumber must be at least 1")
        Integer shotNumber) {
}
