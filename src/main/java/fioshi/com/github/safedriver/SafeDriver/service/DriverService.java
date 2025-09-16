package fioshi.com.github.safedriver.SafeDriver.service;

import fioshi.com.github.safedriver.SafeDriver.dto.DriverCreateDTO;
import fioshi.com.github.safedriver.SafeDriver.dto.DriverResponseDTO;
import fioshi.com.github.safedriver.SafeDriver.dto.DriverUpdateDTO;
import fioshi.com.github.safedriver.SafeDriver.mapper.SafeDriverMapper;
import fioshi.com.github.safedriver.SafeDriver.model.Driver;
import fioshi.com.github.safedriver.SafeDriver.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private SafeDriverMapper safeDriverMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<DriverResponseDTO> findAll() {
        return driverRepository.findAll().stream()
                .map(safeDriverMapper::toDriverResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<DriverResponseDTO> findById(Integer id) {
        return driverRepository.findById(id).map(safeDriverMapper::toDriverResponseDTO);
    }

    public DriverResponseDTO save(DriverCreateDTO dto) {
        Driver driver = safeDriverMapper.toDriver(dto);
        driver.setPassword(passwordEncoder.encode(dto.getPassword()));
        Driver savedDriver = driverRepository.save(driver);
        return safeDriverMapper.toDriverResponseDTO(savedDriver);
    }

    public Optional<DriverResponseDTO> update(Integer id, DriverUpdateDTO dto) {
        return driverRepository.findById(id)
                .map(driver -> {
                    safeDriverMapper.updateDriverFromDto(dto, driver);
                    Driver updatedDriver = driverRepository.save(driver);
                    return safeDriverMapper.toDriverResponseDTO(updatedDriver);
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
