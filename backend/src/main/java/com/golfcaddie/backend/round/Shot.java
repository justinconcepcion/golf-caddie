package com.golfcaddie.backend.round;

import com.golfcaddie.backend.geo.LieType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * A logged shot. Position is captured from the phone; lie and distance-to-pin are
 * derived server-side at log time from the course geometry.
 */
@Getter
@Setter
@Entity
@Table(name = "shots")
public class Shot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "round_id", nullable = false)
    private Round round;

    @Column(name = "hole_number", nullable = false)
    private int holeNumber;

    @Column(name = "shot_number", nullable = false)
    private int shotNumber;

    @Column(nullable = false)
    private double lat;

    @Column(nullable = false)
    private double lng;

    @Enumerated(EnumType.STRING)
    @Column(name = "lie_type", nullable = false, length = 20)
    private LieType lieType;

    @Column(name = "distance_to_pin_yards", nullable = false)
    private double distanceToPinYards;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void prePersist() {
        this.createdAt = Instant.now();
    }
}
