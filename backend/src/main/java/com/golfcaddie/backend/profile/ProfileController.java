package com.golfcaddie.backend.profile;

import com.golfcaddie.backend.profile.dto.PlayerProfileResponse;
import com.golfcaddie.backend.profile.dto.UpdateProfileRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** The single golfer's profile: handedness and per-club carry distances. */
@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ResponseEntity<PlayerProfileResponse> getProfile() {
        return ResponseEntity.ok(profileService.getProfileResponse());
    }

    @PutMapping
    public ResponseEntity<PlayerProfileResponse> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(profileService.updateProfile(request));
    }
}
