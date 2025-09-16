package fioshi.com.github.safedriver.SafeDriver.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fioshi.com.github.safedriver.SafeDriver.model.Vehicle;
import fioshi.com.github.safedriver.SafeDriver.repository.VehicleRepository;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository repository;

    public Vehicle save(Vehicle vehicle) {
        return repository.save(vehicle);
    }

    public List<Vehicle> findAll() {
        return repository.findAll();
    }

    public Vehicle findById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
