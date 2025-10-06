package fioshi.com.github.safedriver.SafeDriver.repository;

import fioshi.com.github.safedriver.SafeDriver.model.UserChallenge;
import fioshi.com.github.safedriver.SafeDriver.model.User;
import fioshi.com.github.safedriver.SafeDriver.model.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserChallengeRepository extends JpaRepository<UserChallenge, UUID> {
    List<UserChallenge> findByUser(User user);
    Optional<UserChallenge> findByUserAndChallenge(User user, Challenge challenge);
    Optional<UserChallenge> findByUserUserIdAndChallengeChallengeId(UUID userId, Integer challengeId);

    // <<< NOVO MÉTODO OTIMIZADO >>>
    // Este método usa JOIN FETCH para carregar os detalhes do Challenge associado
    // em uma única consulta ao banco de dados, resolvendo o problema N+1.
    @Query("SELECT uc FROM UserChallenge uc JOIN FETCH uc.challenge WHERE uc.user = :user")
    List<UserChallenge> findByUserWithChallengeDetails(@Param("user") User user);
}