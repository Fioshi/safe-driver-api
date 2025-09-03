package fioshi.com.github.safedriver.SafeDriver.service;

import fioshi.com.github.safedriver.SafeDriver.model.Challenge;
import fioshi.com.github.safedriver.SafeDriver.model.Driver;
import fioshi.com.github.safedriver.SafeDriver.model.DriverChallenge;
import fioshi.com.github.safedriver.SafeDriver.model.PointHistory;
import fioshi.com.github.safedriver.SafeDriver.model.enums.ChallengeStatus;
import fioshi.com.github.safedriver.SafeDriver.repository.ChallengeRepository;
import fioshi.com.github.safedriver.SafeDriver.repository.DriverChallengeRepository;
import fioshi.com.github.safedriver.SafeDriver.repository.DriverRepository;
import fioshi.com.github.safedriver.SafeDriver.repository.PointHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ChallengeService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private DriverChallengeRepository driverChallengeRepository;

    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    @Transactional
    public Driver redeemChallenge(Integer driverId, Integer challengeId) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("Challenge not found"));

        DriverChallenge driverChallenge = driverChallengeRepository.findByDriverAndChallenge(driver, challenge)
                .orElseGet(() -> {
                    DriverChallenge newDc = new DriverChallenge(driver, challenge);
                    newDc.setStatus(ChallengeStatus.COMPLETED);
                    return newDc;
                });

        if (driverChallenge.getStatus() == ChallengeStatus.REDEEMED) {
            throw new RuntimeException("Challenge already redeemed");
        }

        if (driverChallenge.getStatus() != ChallengeStatus.COMPLETED) {
            throw new RuntimeException("Challenge not yet completed");
        }

        driver.addPoints(challenge.getPoints());

        // Create a record in PointHistory
        PointHistory pointHistory = new PointHistory(driver, challenge.getPoints());
        pointHistoryRepository.save(pointHistory);

        driverChallenge.setStatus(ChallengeStatus.REDEEMED);
        driverChallenge.setLastUpdated(LocalDateTime.now());

        driverRepository.save(driver);
        driverChallengeRepository.save(driverChallenge);

        return driver;
    }
}
