package fioshi.com.github.safedriver.SafeDriver.repository;

import fioshi.com.github.safedriver.SafeDriver.model.Challenge;
import fioshi.com.github.safedriver.SafeDriver.model.Driver;
import fioshi.com.github.safedriver.SafeDriver.model.DriverChallenge;
import fioshi.com.github.safedriver.SafeDriver.model.DriverChallengeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverChallengeRepository extends JpaRepository<DriverChallenge, DriverChallengeId> {

    Optional<DriverChallenge> findByDriverAndChallenge(Driver driver, Challenge challenge);

    List<DriverChallenge> findById_DriverId(Integer driverId);

    Optional<DriverChallenge> findById_DriverIdAndId_ChallengeId(Integer driverId, Integer challengeId);
}
