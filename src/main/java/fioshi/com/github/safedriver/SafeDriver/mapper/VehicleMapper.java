package fioshi.com.github.safedriver.SafeDriver.mapper;

import fioshi.com.github.safedriver.SafeDriver.dto.VehicleResponseDTO;
import fioshi.com.github.safedriver.SafeDriver.model.Vehicle;

public class VehicleMapper {

    public static VehicleResponseDTO toDTO(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }

        VehicleResponseDTO dto = new VehicleResponseDTO();
        dto.setId_veiculo(vehicle.getId_veiculo());
        dto.setMarca(vehicle.getMarca());
        dto.setModelo(vehicle.getModelo());
        dto.setAno(vehicle.getAno());
        dto.setCaracteristicas(vehicle.getCaracteristicas());
        if (vehicle.getDriver() != null) {
            dto.setDriverId(vehicle.getDriver().getId_motorista());
        }

        return dto;
    }
}
