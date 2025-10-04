package fioshi.com.github.safedriver.SafeDriver.controller;

import fioshi.com.github.safedriver.SafeDriver.dto.TripSummaryResponseDTO;
import fioshi.com.github.safedriver.SafeDriver.dto.UserChallengeProgressDTO;
import fioshi.com.github.safedriver.SafeDriver.dto.UserResponseDTO;
import fioshi.com.github.safedriver.SafeDriver.dto.UserUpdateDTO;
import fioshi.com.github.safedriver.SafeDriver.dto.UserProfileDTO;
import fioshi.com.github.safedriver.SafeDriver.model.User;
import fioshi.com.github.safedriver.SafeDriver.service.ChallengeService;
import fioshi.com.github.safedriver.SafeDriver.service.TripService;
import fioshi.com.github.safedriver.SafeDriver.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final ChallengeService challengeService;
    private final TripService tripService;

    public UserController(UserService userService, ChallengeService challengeService, TripService tripService) {
        this.userService = userService;
        this.challengeService = challengeService;
        this.tripService = tripService;
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
    public List<UserResponseDTO> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID userId) {
        return userService.findById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID userId, @RequestBody UserUpdateDTO dto) {
        return userService.update(userId, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        if (userService.deleteById(userId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getUserProfile() {
        UUID userId = getCurrentUserId();
        return challengeService.getProfile(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/me/challenges")
    public ResponseEntity<List<UserChallengeProgressDTO>> getCurrentUserChallenges() {
        UUID userId = getCurrentUserId();
        List<UserChallengeProgressDTO> challenges = challengeService.getUserChallenges(userId);
        return ResponseEntity.ok(challenges);
    }

    @GetMapping("/me/trips")
    public ResponseEntity<List<TripSummaryResponseDTO>> getUserTrips() {
        UUID userId = getCurrentUserId();
        List<TripSummaryResponseDTO> trips = tripService.getTripsByUserId(userId);
        return ResponseEntity.ok(trips);
    }
}
