package com.golfcaddie.backend.course;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/** Loads seeded course reference data from {@code classpath:courses/*.json} at startup. */
@Slf4j
@Repository
public class CourseRepository {

    private static final String COURSE_LOCATION_PATTERN = "classpath:courses/*.json";

    private final ObjectMapper objectMapper;
    private final Map<String, Course> coursesById = new LinkedHashMap<>();

    public CourseRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    void loadSeedCourses() throws IOException {
        Resource[] resources = new PathMatchingResourcePatternResolver()
                .getResources(COURSE_LOCATION_PATTERN);
        for (Resource resource : resources) {
            try (InputStream in = resource.getInputStream()) {
                Course course = objectMapper.readValue(in, Course.class);
                coursesById.put(course.id(), course);
            }
        }
        log.info("Loaded {} seed course(s): {}", coursesById.size(), coursesById.keySet());
    }

    public Optional<Course> findById(String id) {
        return Optional.ofNullable(coursesById.get(id));
    }
}
