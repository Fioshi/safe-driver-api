package fioshi.com.github.safedriver.SafeDriver.mapper;

import fioshi.com.github.safedriver.SafeDriver.dto.*;
import fioshi.com.github.safedriver.SafeDriver.model.Driver;
import fioshi.com.github.safedriver.SafeDriver.model.Trip;
import fioshi.com.github.safedriver.SafeDriver.model.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SafeDriverMapper {

    // Driver
    DriverResponseDTO toDriverResponseDTO(Driver driver);
    Driver toDriver(DriverCreateDTO dto);
    void updateDriverFromDto(DriverUpdateDTO dto, @MappingTarget Driver driver);

    // Driver Profile
    @Mapping(source = "points", target = "totalPoints")
    @Mapping(target = "rankingPosition", ignore = true)
    @Mapping(target = "completedChallengesCount", ignore = true)
    DriverProfileDTO toDriverProfileDTO(Driver driver);

    // Vehicle
    Vehicle toVehicle(VehicleDTO dto);
    VehicleDTO toVehicleDTO(Vehicle vehicle);
    List<VehicleDTO> toVehicleDTO(List<Vehicle> vehicles);

    // Trip
    @Mapping(source = "id", target = "tripId")
    TripSummaryResponseDTO toTripSummaryResponseDTO(Trip trip);
}
