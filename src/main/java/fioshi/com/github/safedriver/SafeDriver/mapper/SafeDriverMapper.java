package fioshi.com.github.safedriver.SafeDriver.mapper;

import java.util.List;

import fioshi.com.github.safedriver.SafeDriver.dto.DriverCreateDTO;
import fioshi.com.github.safedriver.SafeDriver.dto.DriverResponseDTO;
import fioshi.com.github.safedriver.SafeDriver.dto.DriverUpdateDTO;
import fioshi.com.github.safedriver.SafeDriver.dto.DriverProfileDTO;
import fioshi.com.github.safedriver.SafeDriver.dto.DriverChallengeDetailsDTO;
import fioshi.com.github.safedriver.SafeDriver.dto.VehicleDTO;
import fioshi.com.github.safedriver.SafeDriver.model.Driver;
import fioshi.com.github.safedriver.SafeDriver.model.DriverChallenge;
import fioshi.com.github.safedriver.SafeDriver.model.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SafeDriverMapper {

    DriverResponseDTO toDriverResponseDTO(Driver driver);

    Driver toDriver(DriverCreateDTO dto);

    void updateDriverFromDto(DriverUpdateDTO dto, @MappingTarget Driver driver);

    @Mapping(source = "points", target = "totalPoints")
    @Mapping(target = "rankingPosition", ignore = true)
    @Mapping(target = "completedChallengesCount", ignore = true)
    DriverProfileDTO toDriverProfileDTO(Driver driver);

    @Mapping(source = "challenge.id", target = "id")
    @Mapping(source = "challenge.name", target = "title")
    @Mapping(source = "challenge.description", target = "description")
    @Mapping(source = "progress", target = "progress")
    @Mapping(source = "challenge.points", target = "rewardPoints")
    @Mapping(source = "status", target = "status")
    DriverChallengeDetailsDTO toDriverChallengeDetailsDTO(DriverChallenge driverChallenge);

    Vehicle toVehicle(VehicleDTO dto);

    VehicleDTO toVehicleDTO(Vehicle vehicle);

    List<VehicleDTO> toVehicleDTO(List<Vehicle> vehicles);

}
