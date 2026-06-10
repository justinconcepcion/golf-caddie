package com.golfcaddie.backend.round;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShotRepository extends JpaRepository<Shot, Long> {

    int countByRoundIdAndHoleNumber(Long roundId, int holeNumber);
}
