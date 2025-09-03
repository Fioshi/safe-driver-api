package fioshi.com.github.safedriver.SafeDriver.controller;

import fioshi.com.github.safedriver.SafeDriver.model.Driver;
import fioshi.com.github.safedriver.SafeDriver.service.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/challenges")
public class ChallengeController {

    @Autowired
    private ChallengeService challengeService;

    @PostMapping("/{challengeId}/redeem")
    public ResponseEntity<?> redeemChallenge(@PathVariable Integer challengeId, Authentication authentication) {
        Driver driverPrincipal = (Driver) authentication.getPrincipal();
        Integer driverId = driverPrincipal.getId_motorista();

        try {
            Driver updatedDriver = challengeService.redeemChallenge(driverId, challengeId);
            return ResponseEntity.ok(updatedDriver);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
