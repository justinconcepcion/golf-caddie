package com.golfcaddie.backend.course;

import java.util.List;

/** A golf course and its holes. Read-only reference data for the MVP. */
public record Course(String id, String name, List<Hole> holes) {
}
