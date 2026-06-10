package com.golfcaddie.backend.round.dto;

import java.time.Instant;

public record RoundResponse(Long id, String courseId, Instant createdAt) {
}
