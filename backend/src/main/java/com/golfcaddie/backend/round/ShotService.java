package com.golfcaddie.backend.round;

import com.golfcaddie.backend.course.CourseService;
import com.golfcaddie.backend.course.Hole;
import com.golfcaddie.backend.geo.DistanceService;
import com.golfcaddie.backend.geo.GeoPoint;
import com.golfcaddie.backend.geo.LieInferenceService;
import com.golfcaddie.backend.geo.LieType;
import com.golfcaddie.backend.round.dto.LogShotRequest;
import com.golfcaddie.backend.round.dto.ShotResponse;
import org.springframework.stereotype.Service;

/** Logs shots, deriving lie and distance-to-pin from course geometry at log time. */
@Service
public class ShotService {

    private final ShotRepository shotRepository;
    private final RoundService roundService;
    private final CourseService courseService;
    private final DistanceService distanceService;
    private final LieInferenceService lieInferenceService;

    public ShotService(ShotRepository shotRepository,
                       RoundService roundService,
                       CourseService courseService,
                       DistanceService distanceService,
                       LieInferenceService lieInferenceService) {
        this.shotRepository = shotRepository;
        this.roundService = roundService;
        this.courseService = courseService;
        this.distanceService = distanceService;
        this.lieInferenceService = lieInferenceService;
    }

    public ShotResponse logShot(Long roundId, LogShotRequest request) {
        Round round = roundService.getRound(roundId);
        Hole hole = courseService.getHole(round.getCourseId(), request.holeNumber());

        GeoPoint position = new GeoPoint(request.lat(), request.lng());
        LieType lie = lieInferenceService.inferLie(position, hole.polygons());
        double distanceToPin = distanceService.yardsBetween(position, hole.pin());
        int shotNumber = shotRepository.countByRoundIdAndHoleNumber(roundId, request.holeNumber()) + 1;

        Shot shot = new Shot();
        shot.setRound(round);
        shot.setHoleNumber(request.holeNumber());
        shot.setShotNumber(shotNumber);
        shot.setLat(request.lat());
        shot.setLng(request.lng());
        shot.setLieType(lie);
        shot.setDistanceToPinYards(distanceToPin);

        return toResponse(shotRepository.save(shot));
    }

    private ShotResponse toResponse(Shot shot) {
        return new ShotResponse(
                shot.getId(),
                shot.getHoleNumber(),
                shot.getShotNumber(),
                shot.getLat(),
                shot.getLng(),
                shot.getLieType(),
                shot.getDistanceToPinYards());
    }
}
