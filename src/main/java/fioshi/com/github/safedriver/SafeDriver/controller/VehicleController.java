package fioshi.com.github.safedriver.SafeDriver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fioshi.com.github.safedriver.SafeDriver.dto.VehicleDTO;
import fioshi.com.github.safedriver.SafeDriver.mapper.SafeDriverMapper;
import fioshi.com.github.safedriver.SafeDriver.model.Vehicle;
import fioshi.com.github.safedriver.SafeDriver.service.VehicleService;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService service;

    @Autowired
    private SafeDriverMapper mapper;

    @PostMapping
    public ResponseEntity<VehicleDTO> save(@RequestBody VehicleDTO dto) {
        Vehicle vehicle = mapper.toVehicle(dto);
        vehicle = service.save(vehicle);
        return ResponseEntity.ok(mapper.toVehicleDTO(vehicle));
    }

    @GetMapping
    public ResponseEntity<List<VehicleDTO>> findAll() {
        List<Vehicle> vehicles = service.findAll();
        return ResponseEntity.ok(mapper.toVehicleDTO(vehicles));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> findById(@PathVariable Long id) {
        Vehicle vehicle = service.findById(id);
        if (vehicle == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapper.toVehicleDTO(vehicle));
    }
}
