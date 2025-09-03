package fioshi.com.github.safedriver.SafeDriver.service;

import fioshi.com.github.safedriver.SafeDriver.dto.VehicleResponseDTO;
import fioshi.com.github.safedriver.SafeDriver.mapper.VehicleMapper;
import fioshi.com.github.safedriver.SafeDriver.model.Vehicle;
import fioshi.com.github.safedriver.SafeDriver.repository.DriverRepository;
import fioshi.com.github.safedriver.SafeDriver.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private DriverRepository driverRepository;

    public List<VehicleResponseDTO> findAll() {
        return vehicleRepository.findAll().stream()
                .map(VehicleMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<VehicleResponseDTO> findById(Integer id) {
        return vehicleRepository.findById(id).map(VehicleMapper::toDTO);
    }

    public Optional<VehicleResponseDTO> save(Vehicle vehicle, Integer driverId) {
        return driverRepository.findById(driverId)
                .map(driver -> {
                    vehicle.setDriver(driver);
                    Vehicle savedVehicle = vehicleRepository.save(vehicle);
                    return VehicleMapper.toDTO(savedVehicle);
                });
    }

    public Optional<VehicleResponseDTO> update(Integer id, Vehicle vehicleDetails) {
        return vehicleRepository.findById(id)
                .map(vehicle -> {
                    vehicle.setMarca(vehicleDetails.getMarca());
                    vehicle.setModelo(vehicleDetails.getModelo());
                    vehicle.setAno(vehicleDetails.getAno());
                    vehicle.setCaracteristicas(vehicleDetails.getCaracteristicas());
                    Vehicle updatedVehicle = vehicleRepository.save(vehicle);
                    return VehicleMapper.toDTO(updatedVehicle);
                });
    }

    public boolean deleteById(Integer id) {
        if (vehicleRepository.existsById(id)) {
            vehicleRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
