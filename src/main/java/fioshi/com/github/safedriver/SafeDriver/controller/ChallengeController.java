package fioshi.com.github.safedriver.SafeDriver.controller;

import fioshi.com.github.safedriver.SafeDriver.dto.ChallengeDTO;
import fioshi.com.github.safedriver.SafeDriver.dto.UserProfileDTO;
import fioshi.com.github.safedriver.SafeDriver.model.User;
import fioshi.com.github.safedriver.SafeDriver.service.ChallengeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;

    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            return user.getUserId();
        }
        throw new IllegalStateException("User not authenticated or user ID not available.");
    }

    @GetMapping
    public ResponseEntity<List<ChallengeDTO>> getAllChallenges() {
        List<ChallengeDTO> challenges = challengeService.getAllChallenges();
        return ResponseEntity.ok(challenges);
    }

    @PostMapping("/{id}/redeem")
    public ResponseEntity<UserProfileDTO> redeemChallengePoints(@PathVariable("id") Integer challengeId) {
        UUID userId = getCurrentUserId();
        try {
            return challengeService.redeemChallengePoints(userId, challengeId)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
