package fioshi.com.github.safedriver.SafeDriver.service;

import fioshi.com.github.safedriver.SafeDriver.dto.DriverProfileDTO;
import fioshi.com.github.safedriver.SafeDriver.dto.DriverChallengeDetailsDTO;
import fioshi.com.github.safedriver.SafeDriver.mapper.SafeDriverMapper;
import fioshi.com.github.safedriver.SafeDriver.model.Driver;
import fioshi.com.github.safedriver.SafeDriver.model.DriverChallenge;
import fioshi.com.github.safedriver.SafeDriver.model.enums.ChallengeStatus;
import fioshi.com.github.safedriver.SafeDriver.repository.ChallengeRepository;
import fioshi.com.github.safedriver.SafeDriver.repository.DriverChallengeRepository;
import fioshi.com.github.safedriver.SafeDriver.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChallengeService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DriverChallengeRepository driverChallengeRepository;

    @Autowired
    private SafeDriverMapper mapper;

    @Autowired
    private ChallengeRepository challengeRepository; // Injetar ChallengeRepository para buscar desafios

    @Transactional(readOnly = true)
    public Optional<DriverProfileDTO> getProfile(Integer driverId) {
        return driverRepository.findById(driverId).map(driver -> {
            DriverProfileDTO profile = mapper.toDriverProfileDTO(driver);

            // Calcular desafios concluídos
            long completedChallengesCount = driver.getDriverChallenges().stream()
                    .filter(dc -> dc.getStatus() == ChallengeStatus.COMPLETED || dc.getStatus() == ChallengeStatus.REDEEMED)
                    .count();
            profile.setCompletedChallengesCount(completedChallengesCount);

            // Calcular ranking (simplificado: baseado apenas em pontos)
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

    @Transactional(readOnly = true)
    public List<DriverChallengeDetailsDTO> getUserChallenges(Integer driverId) {
        return driverChallengeRepository.findById_DriverId(driverId).stream()
                .filter(dc -> dc.getStatus() != ChallengeStatus.REDEEMED) // Listar apenas desafios não resgatados
                .map(mapper::toDriverChallengeDetailsDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<DriverProfileDTO> redeemPoints(Integer driverId, Integer challengeId) {
        Optional<DriverChallenge> optionalDriverChallenge = driverChallengeRepository.findById_DriverIdAndId_ChallengeId(driverId, challengeId);

        if (optionalDriverChallenge.isEmpty()) {
            return Optional.empty(); // Desafio do motorista não encontrado
        }

        DriverChallenge driverChallenge = optionalDriverChallenge.get();

        // Validações
        if (driverChallenge.getStatus() != ChallengeStatus.COMPLETED) {
            throw new IllegalStateException("O desafio não está completo para ser resgatado.");
        }
        if (driverChallenge.getProgress() < 100) {
            throw new IllegalStateException("O progresso do desafio não atingiu 100%.");
        }
        if (driverChallenge.getStatus() == ChallengeStatus.REDEEMED) {
            throw new IllegalStateException("Os pontos deste desafio já foram resgatados.");
        }

        // Adicionar pontos ao motorista
        Driver driver = driverChallenge.getDriver();
        int pointsToAdd = driverChallenge.getChallenge().getPoints();
        driver.addPoints(pointsToAdd);
        driverRepository.save(driver); // Salva o motorista com os pontos atualizados

        // Marcar desafio como resgatado
        driverChallenge.setStatus(ChallengeStatus.REDEEMED);
        driverChallenge.setLastUpdated(LocalDateTime.now());
        driverChallengeRepository.save(driverChallenge);

        // Retornar o perfil atualizado
        return getProfile(driverId);
    }
}
