package fioshi.com.github.safedriver.SafeDriver.mapper;

import fioshi.com.github.safedriver.SafeDriver.dto.VehicleResponseDTO;
import fioshi.com.github.safedriver.SafeDriver.model.Vehicle;

public class VehicleMapper {

    public static VehicleResponseDTO toDTO(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }

        VehicleResponseDTO dto = new VehicleResponseDTO();
        if (vehicle.getId() != null) {
            dto.setId_veiculo(vehicle.getId().intValue());
        }
        dto.setMarca(vehicle.getBrand());
        dto.setModelo(vehicle.getModel());

        return dto;
    }
}
