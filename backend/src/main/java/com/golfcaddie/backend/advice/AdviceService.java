package com.golfcaddie.backend.advice;

import com.golfcaddie.backend.advice.dto.AdviceRequest;
import com.golfcaddie.backend.advice.dto.AdviceResponse;
import com.golfcaddie.backend.course.CourseService;
import com.golfcaddie.backend.course.Hole;
import com.golfcaddie.backend.geo.DistanceService;
import com.golfcaddie.backend.geo.GeoPoint;
import com.golfcaddie.backend.geo.LieInferenceService;
import com.golfcaddie.backend.geo.LieType;
import com.golfcaddie.backend.profile.Club;
import com.golfcaddie.backend.profile.PlayerProfile;
import com.golfcaddie.backend.profile.ProfileService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Assembles the caddie prompt server-side from course geometry and the player profile,
 * then proxies the call to Claude. The static (player + hole) context is kept in the
 * system prompt so it can be cached separately from the per-shot situation.
 */
@Service
public class AdviceService {

    private final ProfileService profileService;
    private final CourseService courseService;
    private final DistanceService distanceService;
    private final LieInferenceService lieInferenceService;
    private final ClaudeAdvisor claudeAdvisor;

    public AdviceService(ProfileService profileService,
                         CourseService courseService,
                         DistanceService distanceService,
                         LieInferenceService lieInferenceService,
                         ClaudeAdvisor claudeAdvisor) {
        this.profileService = profileService;
        this.courseService = courseService;
        this.distanceService = distanceService;
        this.lieInferenceService = lieInferenceService;
        this.claudeAdvisor = claudeAdvisor;
    }

    public AdviceResponse getAdvice(AdviceRequest request) {
        Hole hole = courseService.getHole(request.courseId(), request.holeNumber());
        GeoPoint position = new GeoPoint(request.lat(), request.lng());

        LieType lie = lieInferenceService.inferLie(position, hole.polygons());
        double distanceToPin = distanceService.yardsBetween(position, hole.pin());
        PlayerProfile profile = profileService.getProfile();

        String advice = claudeAdvisor.advise(
                buildSystemPrompt(profile, hole),
                buildUserPrompt(distanceToPin, lie, request.shotNumber()));

        return new AdviceResponse(hole.number(), lie, distanceToPin, advice);
    }

    private String buildSystemPrompt(PlayerProfile profile, Hole hole) {
        return """
                You are an expert golf caddie. Recommend exactly one club and a one or two \
                sentence strategy for the shot. Be concise, specific, and direct. Do not hedge.

                Player handedness: %s
                Player club carry distances (yards): %s

                Hole %d: par %d, %d yards."""
                .formatted(
                        profile.getHandedness(),
                        formatClubDistances(profile.getClubDistancesYards()),
                        hole.number(),
                        hole.par(),
                        hole.yards());
    }

    private String buildUserPrompt(double distanceToPin, LieType lie, Integer shotNumber) {
        String shotContext = shotNumber == null ? "" : String.format(" This is shot number %d.", shotNumber);
        return String.format(
                "I am %.0f yards from the pin. My lie is %s.%s What club should I hit and what is the strategy?",
                distanceToPin, lie, shotContext);
    }

    private String formatClubDistances(Map<Club, Integer> clubDistances) {
        if (clubDistances.isEmpty()) {
            return "not provided";
        }
        return clubDistances.entrySet().stream()
                .map(entry -> String.format("%s %d", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining(", "));
    }
}
