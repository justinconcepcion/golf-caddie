package com.golfcaddie.backend.geo;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Determines the lie of a ball from its GPS position by point-in-polygon testing
 * against a hole's typed boundaries. Specific surfaces (green, bunker, tee) are
 * tested before the fairway so overlapping rings resolve to the more specific lie.
 */
@Service
public class LieInferenceService {

    private static final List<LieType> PRIORITY = List.of(
            LieType.GREEN, LieType.BUNKER, LieType.TEE, LieType.FAIRWAY);

    private final GeometryFactory geometryFactory = new GeometryFactory();

    /**
     * @return the most specific lie whose polygon contains the point; ROUGH when the
     * point falls inside the hole's data but no polygon, UNKNOWN when no polygons exist.
     */
    public LieType inferLie(GeoPoint position, List<LiePolygon> polygons) {
        if (polygons == null || polygons.isEmpty()) {
            return LieType.UNKNOWN;
        }
        Point point = geometryFactory.createPoint(new Coordinate(position.lng(), position.lat()));
        return PRIORITY.stream()
                .filter(type -> containsPointForType(type, polygons, point))
                .findFirst()
                .orElse(LieType.ROUGH);
    }

    private boolean containsPointForType(LieType type, List<LiePolygon> polygons, Point point) {
        return polygons.stream()
                .filter(polygon -> polygon.type() == type)
                .anyMatch(polygon -> toPolygon(polygon.ring()).contains(point));
    }

    private Polygon toPolygon(List<GeoPoint> ring) {
        Coordinate[] coordinates = new Coordinate[ring.size() + 1];
        for (int i = 0; i < ring.size(); i++) {
            coordinates[i] = new Coordinate(ring.get(i).lng(), ring.get(i).lat());
        }
        coordinates[ring.size()] = coordinates[0];
        LinearRing shell = geometryFactory.createLinearRing(coordinates);
        return geometryFactory.createPolygon(shell);
    }
}
