package com.golfcaddie.backend.round;

import com.golfcaddie.backend.round.dto.CreateRoundRequest;
import com.golfcaddie.backend.round.dto.LogShotRequest;
import com.golfcaddie.backend.round.dto.RoundResponse;
import com.golfcaddie.backend.round.dto.ShotResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Starts rounds and logs shots within them. */
@RestController
@RequestMapping("/api/v1/rounds")
public class RoundController {

    private final RoundService roundService;
    private final ShotService shotService;

    public RoundController(RoundService roundService, ShotService shotService) {
        this.roundService = roundService;
        this.shotService = shotService;
    }

    @PostMapping
    public ResponseEntity<RoundResponse> createRound(@Valid @RequestBody CreateRoundRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roundService.createRound(request));
    }

    @PostMapping("/{roundId}/shots")
    public ResponseEntity<ShotResponse> logShot(
            @PathVariable Long roundId,
            @Valid @RequestBody LogShotRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(shotService.logShot(roundId, request));
    }
}
