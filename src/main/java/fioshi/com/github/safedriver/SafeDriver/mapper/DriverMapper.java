package fioshi.com.github.safedriver.SafeDriver.mapper;

import fioshi.com.github.safedriver.SafeDriver.dto.DriverResponseDTO;
import fioshi.com.github.safedriver.SafeDriver.model.Driver;

import java.util.stream.Collectors;

public class DriverMapper {

    public static DriverResponseDTO toDTO(Driver driver) {
        if (driver == null) {
            return null;
        }

        DriverResponseDTO dto = new DriverResponseDTO();
        dto.setId_motorista(driver.getId_motorista());
        dto.setNome(driver.getNome());
        dto.setEmail(driver.getEmail());
        dto.setPoints(driver.getPoints());
        dto.setData_cadastro(driver.getData_cadastro());
        dto.setOutros_dados(driver.getOutros_dados());

        if (driver.getVehicles() != null) {
            dto.setVehicles(driver.getVehicles().stream()
                    .map(VehicleMapper::toDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}
