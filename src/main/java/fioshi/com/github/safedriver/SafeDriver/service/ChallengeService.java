package fioshi.com.github.safedriver.SafeDriver.service;

import fioshi.com.github.safedriver.SafeDriver.dto.ChallengeDTO;
import fioshi.com.github.safedriver.SafeDriver.dto.UserProfileDTO;
import fioshi.com.github.safedriver.SafeDriver.dto.UserChallengeProgressDTO;
import fioshi.com.github.safedriver.SafeDriver.mapper.SafeDriverMapper;
import fioshi.com.github.safedriver.SafeDriver.model.Challenge;
import fioshi.com.github.safedriver.SafeDriver.model.User;
import fioshi.com.github.safedriver.SafeDriver.model.UserChallenge;
import fioshi.com.github.safedriver.SafeDriver.repository.ChallengeRepository;
import fioshi.com.github.safedriver.SafeDriver.repository.UserChallengeRepository;
import fioshi.com.github.safedriver.SafeDriver.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChallengeService {

    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final SafeDriverMapper mapper;

    public ChallengeService(UserRepository userRepository,
                            ChallengeRepository challengeRepository,
                            UserChallengeRepository userChallengeRepository,
                            SafeDriverMapper mapper) {
        this.userRepository = userRepository;
        this.challengeRepository = challengeRepository;
        this.userChallengeRepository = userChallengeRepository;
        this.mapper = mapper;
    }

    // --- Challenge Catalog Management ---

    @Transactional
    public ChallengeDTO createChallenge(ChallengeDTO challengeDTO) {
        Challenge challenge = mapper.toChallenge(challengeDTO);
        challenge.setIsActive(true);
        return mapper.toChallengeDTO(challengeRepository.save(challenge));
    }

    @Transactional(readOnly = true)
    public Optional<ChallengeDTO> getChallengeById(Integer challengeId) {
        return challengeRepository.findById(challengeId).map(mapper::toChallengeDTO);
    }

    @Transactional(readOnly = true)
    public List<ChallengeDTO> getAllChallenges() {
        return challengeRepository.findAll().stream()
                .map(mapper::toChallengeDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<ChallengeDTO> updateChallenge(Integer challengeId, ChallengeDTO challengeDTO) {
        return challengeRepository.findById(challengeId).map(challenge -> {
            challenge.setTitle(challengeDTO.getTitle());
            challenge.setDescription(challengeDTO.getDescription());
            challenge.setPointsReward(challengeDTO.getPointsReward());
            challenge.setIsActive(challengeDTO.getIsActive());
            return mapper.toChallengeDTO(challengeRepository.save(challenge));
        });
    }

    @Transactional
    public boolean deleteChallenge(Integer challengeId) {
        if (challengeRepository.existsById(challengeId)) {
            challengeRepository.deleteById(challengeId);
            return true;
        }
        return false;
    }

    // --- User Profile & Challenge Progress ---

    @Transactional(readOnly = true)
    public Optional<UserProfileDTO> getProfile(UUID userId) {
        return userRepository.findById(userId).map(user -> {
            UserProfileDTO profile = mapper.toUserProfileDTO(user);

            long completedChallengesCount = userChallengeRepository.findByUser(user).stream()
                    .filter(uc -> uc.getStatus().equals("concluido") || uc.getStatus().equals("resgatado"))
                    .count();
            profile.setCompletedChallengesCount(completedChallengesCount);

            List<User> allUsers = userRepository.findAll();
            allUsers.sort(Comparator.comparingInt(User::getPoints).reversed());
            int rankingPosition = 1;
            for (int i = 0; i < allUsers.size(); i++) {
                if (allUsers.get(i).getUserId().equals(userId)) {
                    rankingPosition = i + 1;
                    break;
                }
            }
            profile.setRankingPosition(rankingPosition);

            return profile;
        });
    }

    @Transactional
    public UserChallengeProgressDTO assignChallengeToUser(UUID userId, Integer challengeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new IllegalArgumentException("Challenge not found"));

        Optional<UserChallenge> existingUserChallenge = userChallengeRepository.findByUserAndChallenge(user, challenge);
        if (existingUserChallenge.isPresent()) {
            throw new IllegalStateException("Challenge already assigned to this user.");
        }

        UserChallenge userChallenge = new UserChallenge();
        userChallenge.setUser(user);
        userChallenge.setChallenge(challenge);
        userChallenge.setStatus("em_progresso");
        userChallenge.setProgress(BigDecimal.ZERO);
        userChallenge.setCompletedAt(null);
        userChallenge.setRedeemedAt(null);

        return mapper.toUserChallengeProgressDTO(userChallengeRepository.save(userChallenge));
    }

    @Transactional(readOnly = true)
    public List<UserChallengeProgressDTO> getUserChallenges(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return userChallengeRepository.findByUser(user).stream()
                .map(mapper::toUserChallengeProgressDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<UserChallengeProgressDTO> updateChallengeProgress(
            UUID userId, Integer challengeId, BigDecimal newProgress) {

        UserChallenge userChallenge = userChallengeRepository.findByUserUserIdAndChallengeChallengeId(userId, challengeId)
                .orElseThrow(() -> new IllegalArgumentException("User challenge not found."));

        userChallenge.setProgress(newProgress);
        if (newProgress.compareTo(BigDecimal.valueOf(100)) >= 0) {
            userChallenge.setStatus("concluido");
            userChallenge.setCompletedAt(OffsetDateTime.now());
        }
        return Optional.of(mapper.toUserChallengeProgressDTO(userChallengeRepository.save(userChallenge)));
    }

    @Transactional
    public Optional<UserProfileDTO> redeemChallengePoints(UUID userId, Integer challengeId) {
        UserChallenge userChallenge = userChallengeRepository.findByUserUserIdAndChallengeChallengeId(userId, challengeId)
                .orElseThrow(() -> new IllegalArgumentException("User challenge not found."));

        if (!userChallenge.getStatus().equals("concluido")) {
            throw new IllegalStateException("Challenge is not completed.");
        }
        if (userChallenge.getRedeemedAt() != null) {
            throw new IllegalStateException("Points for this challenge have already been redeemed.");
        }

        User user = userChallenge.getUser();
        user.addPoints(userChallenge.getChallenge().getPointsReward());
        userRepository.save(user);

        userChallenge.setStatus("resgatado");
        userChallenge.setRedeemedAt(OffsetDateTime.now());
        userChallengeRepository.save(userChallenge);

        return getProfile(userId);
    }
}
