package fioshi.com.github.safedriver.SafeDriver.mapper;

import fioshi.com.github.safedriver.SafeDriver.dto.DriverResponseDTO;
import fioshi.com.github.safedriver.SafeDriver.dto.VehicleDTO;
import fioshi.com.github.safedriver.SafeDriver.model.Driver;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class DriverMapper {

    public static DriverResponseDTO toDTO(Driver driver) {
        if (driver == null) {
            return null;
        }

        DriverResponseDTO dto = new DriverResponseDTO();
        dto.setNome(driver.getNome());
        dto.setEmail(driver.getEmail());
        dto.setTelefone(driver.getTelefone());
        dto.setObjetivoDeDirecao(driver.getObjetivoDeDirecao());
        dto.setEmailCorporativo(driver.getEmailCorporativo());

        if (driver.getVehicles() != null) {
            dto.setVehicles(driver.getVehicles().stream().map(vehicle -> {
                VehicleDTO vehicleDTO = new VehicleDTO();
                vehicleDTO.setId(vehicle.getId());
                vehicleDTO.setPlate(vehicle.getPlate());
                vehicleDTO.setBrand(vehicle.getBrand());
                vehicleDTO.setModel(vehicle.getModel());
                return vehicleDTO;
            }).collect(Collectors.toList()));
        } else {
            dto.setVehicles(new ArrayList<>());
        }

        return dto;
    }
}
