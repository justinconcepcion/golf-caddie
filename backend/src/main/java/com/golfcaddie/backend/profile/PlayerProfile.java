package com.golfcaddie.backend.profile;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.MapKeyEnumerated;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.EnumMap;
import java.util.Map;

/**
 * The single golfer using the app (MVP is single-user, so this is a singleton row).
 * Club distances seed the caddie advice prompt.
 */
@Getter
@Setter
@Entity
@Table(name = "player_profiles")
public class PlayerProfile {

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "handedness", nullable = false, length = 10)
    private Handedness handedness = Handedness.RIGHT;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "player_club_distances",
            joinColumns = @JoinColumn(name = "profile_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "club", length = 20)
    @Column(name = "carry_yards", nullable = false)
    private Map<Club, Integer> clubDistancesYards = new EnumMap<>(Club.class);

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    @PreUpdate
    void touch() {
        this.updatedAt = Instant.now();
    }
}
