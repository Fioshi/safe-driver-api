package fioshi.com.github.safedriver.SafeDriver.repository;

import fioshi.com.github.safedriver.SafeDriver.model.UserChallenge;
import fioshi.com.github.safedriver.SafeDriver.model.User;
import fioshi.com.github.safedriver.SafeDriver.model.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserChallengeRepository extends JpaRepository<UserChallenge, UUID> {
    List<UserChallenge> findByUser(User user);
    Optional<UserChallenge> findByUserAndChallenge(User user, Challenge challenge);
    Optional<UserChallenge> findByUserUserIdAndChallengeChallengeId(UUID userId, Integer challengeId);
}
