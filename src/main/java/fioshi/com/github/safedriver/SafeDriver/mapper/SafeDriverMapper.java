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

    // ... (todos os outros mapeamentos permanecem iguais)
    UserResponseDTO toUserResponseDTO(User user);
    @Mapping(source = "password", target = "hashedPassword")
    User toUser(UserCreateDTO dto);
    void updateUserFromDto(UserUpdateDTO dto, @MappingTarget User user);
    @Mapping(source = "points", target = "totalPoints")
    @Mapping(target = "rankingPosition", ignore = true)
    @Mapping(target = "completedChallengesCount", ignore = true)
    UserProfileDTO toUserProfileDTO(User user);
    @Mapping(source = "tripId", target = "tripId")
    TripSummaryResponseDTO toTripSummaryResponseDTO(Trip trip);
    List<TripSummaryResponseDTO> toTripSummaryResponseDTOList(List<Trip> trips);
    TripEvent toTripEvent(TripEventDTO dto);
    default String map(Map<String, Object> details) { return details != null ? details.toString() : null; }
    default Map<String, Object> map(String details) { return details != null ? new HashMap<>() : null; }
    Challenge toChallenge(ChallengeDTO dto);
    ChallengeDTO toChallengeDTO(Challenge challenge);
    UserChallenge toUserChallenge(UserChallengeProgressDTO dto);


    // <<< MAPEAMENTO MODIFICADO >>>
    // Adicionamos as anotações @Mapping para ensinar o MapStruct a buscar os dados
    // da entidade aninhada 'challenge' que o JOIN FETCH nos trouxe.
    @Mapping(source = "challenge.title", target = "title")
    @Mapping(source = "challenge.description", target = "description")
    @Mapping(source = "challenge.pointsReward", target = "pointsReward")
    UserChallengeProgressDTO toUserChallengeProgressDTO(UserChallenge userChallenge);
}