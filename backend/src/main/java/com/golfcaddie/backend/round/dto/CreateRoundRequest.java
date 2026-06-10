package com.golfcaddie.backend.round.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateRoundRequest(
        @NotBlank(message = "courseId is required") String courseId) {
}
