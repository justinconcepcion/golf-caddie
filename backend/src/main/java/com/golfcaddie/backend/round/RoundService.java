package com.golfcaddie.backend.round;

import com.golfcaddie.backend.course.CourseService;
import com.golfcaddie.backend.round.dto.CreateRoundRequest;
import com.golfcaddie.backend.round.dto.RoundResponse;
import com.golfcaddie.backend.shared.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RoundService {

    private final RoundRepository roundRepository;
    private final CourseService courseService;

    public RoundService(RoundRepository roundRepository, CourseService courseService) {
        this.roundRepository = roundRepository;
        this.courseService = courseService;
    }

    public RoundResponse createRound(CreateRoundRequest request) {
        courseService.getCourse(request.courseId());
        Round round = new Round();
        round.setCourseId(request.courseId());
        return toResponse(roundRepository.save(round));
    }

    public Round getRound(Long roundId) {
        return roundRepository.findById(roundId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Round not found: %d", roundId)));
    }

    private RoundResponse toResponse(Round round) {
        return new RoundResponse(round.getId(), round.getCourseId(), round.getCreatedAt());
    }
}
