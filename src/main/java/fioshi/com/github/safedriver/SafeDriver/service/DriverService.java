package fioshi.com.github.safedriver.SafeDriver.service;

import fioshi.com.github.safedriver.SafeDriver.dto.DriverResponseDTO;
import fioshi.com.github.safedriver.SafeDriver.mapper.DriverMapper;
import fioshi.com.github.safedriver.SafeDriver.model.Driver;
import fioshi.com.github.safedriver.SafeDriver.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;

    public List<DriverResponseDTO> findAll() {
        return driverRepository.findAll().stream()
                .map(DriverMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<DriverResponseDTO> findById(Integer id) {
        return driverRepository.findById(id).map(DriverMapper::toDTO);
    }

    public DriverResponseDTO save(Driver driver) {
        Driver savedDriver = driverRepository.save(driver);
        return DriverMapper.toDTO(savedDriver);
    }

    public Optional<DriverResponseDTO> update(Integer id, Driver driverDetails) {
        return driverRepository.findById(id)
                .map(driver -> {
                    driver.setNome(driverDetails.getNome());
                    driver.setEmail(driverDetails.getEmail());
                    driver.setData_cadastro(driverDetails.getData_cadastro());
                    driver.setOutros_dados(driverDetails.getOutros_dados());
                    Driver updatedDriver = driverRepository.save(driver);
                    return DriverMapper.toDTO(updatedDriver);
                });
    }

    public boolean deleteById(Integer id) {
        if (driverRepository.existsById(id)) {
            driverRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
