package fioshi.com.github.safedriver.SafeDriver.service;

import fioshi.com.github.safedriver.SafeDriver.dto.DriverProfileDTO;
import fioshi.com.github.safedriver.SafeDriver.mapper.SafeDriverMapper;
import fioshi.com.github.safedriver.SafeDriver.model.Driver;
import fioshi.com.github.safedriver.SafeDriver.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ChallengeService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private SafeDriverMapper mapper;

    @Transactional(readOnly = true)
    public Optional<DriverProfileDTO> getProfile(Integer driverId) {
        return driverRepository.findById(driverId).map(driver -> {
            DriverProfileDTO profile = mapper.toDriverProfileDTO(driver);

            // Since challenges are removed, this is now 0.
            profile.setCompletedChallengesCount(0L);

            // Calculate ranking
            List<Driver> allDrivers = driverRepository.findAll();
            allDrivers.sort(Comparator.comparingInt(Driver::getPoints).reversed());
            int rankingPosition = 1;
            for (int i = 0; i < allDrivers.size(); i++) {
                if (allDrivers.get(i).getId_motorista().equals(driverId)) {
                    rankingPosition = i + 1;
                    break;
                }
            }
            profile.setRankingPosition(rankingPosition);

            return profile;
        });
    }
}
