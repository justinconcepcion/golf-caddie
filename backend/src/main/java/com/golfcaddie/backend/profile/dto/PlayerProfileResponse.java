package com.golfcaddie.backend.profile.dto;

import com.golfcaddie.backend.profile.Club;
import com.golfcaddie.backend.profile.Handedness;

import java.util.Map;

public record PlayerProfileResponse(
        Long id,
        Handedness handedness,
        Map<Club, Integer> clubDistancesYards) {
}
