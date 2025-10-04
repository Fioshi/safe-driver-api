package fioshi.com.github.safedriver.SafeDriver.mapper;

import fioshi.com.github.safedriver.SafeDriver.dto.*;
import fioshi.com.github.safedriver.SafeDriver.model.User;
import fioshi.com.github.safedriver.SafeDriver.model.Trip;
import fioshi.com.github.safedriver.SafeDriver.model.TripEvent;
import fioshi.com.github.safedriver.SafeDriver.model.Challenge;
import fioshi.com.github.safedriver.SafeDriver.model.UserChallenge;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SafeDriverMapper {

    // User mappings
    UserResponseDTO toUserResponseDTO(User user);

    @Mapping(source = "password", target = "hashedPassword")
    User toUser(UserCreateDTO dto);

    void updateUserFromDto(UserUpdateDTO dto, @MappingTarget User user);

    // User Profile
    @Mapping(source = "points", target = "totalPoints")
    @Mapping(target = "rankingPosition", ignore = true)
    @Mapping(target = "completedChallengesCount", ignore = true)
    UserProfileDTO toUserProfileDTO(User user);

    // Trip
    @Mapping(source = "tripId", target = "tripId")
    TripSummaryResponseDTO toTripSummaryResponseDTO(Trip trip);

    List<TripSummaryResponseDTO> toTripSummaryResponseDTOList(List<Trip> trips);

    // TripEvent
    TripEvent toTripEvent(TripEventDTO dto);

    // Custom mapping for Map<String, Object> to String and vice-versa for details field
    default String map(Map<String, Object> details) {
        return details != null ? details.toString() : null;
    }

    default Map<String, Object> map(String details) {
        return details != null ? new HashMap<>() : null;
    }

    // Challenge
    Challenge toChallenge(ChallengeDTO dto);
    ChallengeDTO toChallengeDTO(Challenge challenge);

    // UserChallenge
    UserChallenge toUserChallenge(UserChallengeProgressDTO dto);
    UserChallengeProgressDTO toUserChallengeProgressDTO(UserChallenge userChallenge);
}
