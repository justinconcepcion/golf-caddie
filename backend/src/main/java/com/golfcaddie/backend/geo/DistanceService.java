package com.golfcaddie.backend.geo;

import org.springframework.stereotype.Service;

/** Great-circle distance helpers for on-course yardages. */
@Service
public class DistanceService {

    private static final double EARTH_RADIUS_METERS = 6_371_000.0;
    private static final double METERS_PER_YARD = 0.9144;

    /** Haversine distance between two coordinates, in yards. */
    public double yardsBetween(GeoPoint from, GeoPoint to) {
        double lat1 = Math.toRadians(from.lat());
        double lat2 = Math.toRadians(to.lat());
        double deltaLat = Math.toRadians(to.lat() - from.lat());
        double deltaLng = Math.toRadians(to.lng() - from.lng());

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(deltaLng / 2) * Math.sin(deltaLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (EARTH_RADIUS_METERS * c) / METERS_PER_YARD;
    }
}
