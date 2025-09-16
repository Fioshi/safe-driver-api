package fioshi.com.github.safedriver.SafeDriver.controller;

import fioshi.com.github.safedriver.SafeDriver.dto.DriverProfileDTO;
import fioshi.com.github.safedriver.SafeDriver.dto.DriverChallengeDetailsDTO;
import fioshi.com.github.safedriver.SafeDriver.model.Driver;
import fioshi.com.github.safedriver.SafeDriver.service.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
public class DriverProfileController {

    @Autowired
    private ChallengeService challengeService;

    private Integer getCurrentDriverId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Driver) {
            Driver driver = (Driver) authentication.getPrincipal();
            return driver.getId_motorista();
        }
        throw new IllegalStateException("User not authenticated or driver ID not available.");
    }

    @GetMapping("/profile")
    public ResponseEntity<DriverProfileDTO> getDriverProfile() {
        Integer driverId = getCurrentDriverId();
        return challengeService.getProfile(driverId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/challenges")
    public ResponseEntity<List<DriverChallengeDetailsDTO>> getDriverChallenges() {
        Integer driverId = getCurrentDriverId();
        List<DriverChallengeDetailsDTO> challenges = challengeService.getUserChallenges(driverId);
        return ResponseEntity.ok(challenges);
    }
}
