package com.golfcaddie.backend.course;

import com.golfcaddie.backend.geo.GeoPoint;
import com.golfcaddie.backend.geo.LiePolygon;

import java.util.List;

/**
 * A single hole. {@code pin} is the green-centre target used for distance-to-pin;
 * {@code polygons} carry the typed boundaries used to infer the lie of a shot.
 */
public record Hole(
        int number,
        int par,
        int yards,
        GeoPoint tee,
        GeoPoint pin,
        List<LiePolygon> polygons) {
}
