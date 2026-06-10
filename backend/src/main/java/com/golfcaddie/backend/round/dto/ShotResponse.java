package com.golfcaddie.backend.round.dto;

import com.golfcaddie.backend.geo.LieType;

public record ShotResponse(
        Long id,
        int holeNumber,
        int shotNumber,
        double lat,
        double lng,
        LieType lie,
        double distanceToPinYards) {
}
