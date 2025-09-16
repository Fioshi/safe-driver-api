package fioshi.com.github.safedriver.SafeDriver.controller;

import fioshi.com.github.safedriver.SafeDriver.dto.DriverResponseDTO;
import fioshi.com.github.safedriver.SafeDriver.dto.DriverUpdateDTO;
import fioshi.com.github.safedriver.SafeDriver.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/drivers")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @GetMapping
    public List<DriverResponseDTO> getAllDrivers() {
        return driverService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverResponseDTO> getDriverById(@PathVariable Integer id) {
        return driverService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DriverResponseDTO> updateDriver(@PathVariable Integer id, @RequestBody DriverUpdateDTO dto) {
        return driverService.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Integer id) {
        if (driverService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
