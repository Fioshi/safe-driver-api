package fioshi.com.github.safedriver.SafeDriver.controller;

import fioshi.com.github.safedriver.SafeDriver.dto.VehicleResponseDTO;
import fioshi.com.github.safedriver.SafeDriver.model.Vehicle;
import fioshi.com.github.safedriver.SafeDriver.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/vehicles")
    public List<VehicleResponseDTO> getAllVehicles() {
        return vehicleService.findAll();
    }

    @GetMapping("/vehicles/{id}")
    public ResponseEntity<VehicleResponseDTO> getVehicleById(@PathVariable Integer id) {
        return vehicleService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/drivers/{driverId}/vehicles")
    public ResponseEntity<VehicleResponseDTO> createVehicle(@PathVariable Integer driverId, @RequestBody Vehicle vehicle) {
        return vehicleService.save(vehicle, driverId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // Or a different error if the driver is not found
    }

    @PutMapping("/vehicles/{id}")
    public ResponseEntity<VehicleResponseDTO> updateVehicle(@PathVariable Integer id, @RequestBody Vehicle vehicleDetails) {
        return vehicleService.update(id, vehicleDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/vehicles/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Integer id) {
        if (vehicleService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
