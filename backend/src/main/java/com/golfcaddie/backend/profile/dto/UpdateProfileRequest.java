package com.golfcaddie.backend.profile.dto;

import com.golfcaddie.backend.profile.Club;
import com.golfcaddie.backend.profile.Handedness;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record UpdateProfileRequest(
        @NotNull(message = "handedness is required") Handedness handedness,
        @NotNull(message = "clubDistancesYards is required") Map<Club, Integer> clubDistancesYards) {
}
