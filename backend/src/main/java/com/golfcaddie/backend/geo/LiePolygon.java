package com.golfcaddie.backend.geo;

import java.util.List;

/** A typed boundary on a hole (e.g. the green surface, a bunker, the fairway). */
public record LiePolygon(LieType type, List<GeoPoint> ring) {
}
