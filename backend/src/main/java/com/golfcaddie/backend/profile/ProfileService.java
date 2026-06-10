package com.golfcaddie.backend.profile;

import com.golfcaddie.backend.profile.dto.PlayerProfileResponse;
import com.golfcaddie.backend.profile.dto.UpdateProfileRequest;
import org.springframework.stereotype.Service;

import java.util.EnumMap;

/** Manages the single golfer profile. MVP is single-user, so all access targets one row. */
@Service
public class ProfileService {

    private static final Long SINGLETON_ID = 1L;

    private final PlayerProfileRepository playerProfileRepository;

    public ProfileService(PlayerProfileRepository playerProfileRepository) {
        this.playerProfileRepository = playerProfileRepository;
    }

    public PlayerProfile getProfile() {
        return playerProfileRepository.findById(SINGLETON_ID)
                .orElseGet(this::newDefaultProfile);
    }

    public PlayerProfileResponse getProfileResponse() {
        return toResponse(getProfile());
    }

    public PlayerProfileResponse updateProfile(UpdateProfileRequest request) {
        PlayerProfile profile = getProfile();
        profile.setHandedness(request.handedness());
        EnumMap<Club, Integer> clubDistances = new EnumMap<>(Club.class);
        clubDistances.putAll(request.clubDistancesYards());
        profile.setClubDistancesYards(clubDistances);
        return toResponse(playerProfileRepository.save(profile));
    }

    private PlayerProfile newDefaultProfile() {
        PlayerProfile profile = new PlayerProfile();
        profile.setId(SINGLETON_ID);
        return profile;
    }

    private PlayerProfileResponse toResponse(PlayerProfile profile) {
        return new PlayerProfileResponse(
                profile.getId(),
                profile.getHandedness(),
                profile.getClubDistancesYards());
    }
}
