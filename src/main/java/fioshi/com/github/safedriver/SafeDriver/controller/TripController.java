package fioshi.com.github.safedriver.SafeDriver.controller;

import fioshi.com.github.safedriver.SafeDriver.dto.TripTelemetryRequestDTO;
import fioshi.com.github.safedriver.SafeDriver.dto.TripSummaryResponseDTO;
import fioshi.com.github.safedriver.SafeDriver.model.User;
import fioshi.com.github.safedriver.SafeDriver.service.TripService;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/trips")
@CrossOrigin(origins = "*")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
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

    @PostMapping("/analyze")
    public ResponseEntity<TripSummaryResponseDTO> analyzeTrip(@RequestBody TripTelemetryRequestDTO request) {
        UUID userId = getCurrentUserId();
        try {
            TripSummaryResponseDTO summary = tripService.analyzeTrip(request, userId);
            return ResponseEntity.ok(summary);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
