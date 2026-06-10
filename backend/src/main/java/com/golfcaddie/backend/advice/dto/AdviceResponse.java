package com.golfcaddie.backend.advice.dto;

import com.golfcaddie.backend.geo.LieType;

public record AdviceResponse(
        int holeNumber,
        LieType lie,
        double distanceToPinYards,
        String advice) {
}
