package com.golfcaddie.backend.course;

import com.golfcaddie.backend.shared.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course getCourse(String courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Course not found: %s", courseId)));
    }

    public Hole getHole(String courseId, int holeNumber) {
        return getCourse(courseId).holes().stream()
                .filter(hole -> hole.number() == holeNumber)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Hole %d not found on course %s", holeNumber, courseId)));
    }
}
