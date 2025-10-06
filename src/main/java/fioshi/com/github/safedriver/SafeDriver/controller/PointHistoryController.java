package fioshi.com.github.safedriver.SafeDriver.controller;

import fioshi.com.github.safedriver.SafeDriver.dto.PointHistoryResponseDTO;
import fioshi.com.github.safedriver.SafeDriver.model.User;
import fioshi.com.github.safedriver.SafeDriver.model.enums.HistoryPeriod;
import fioshi.com.github.safedriver.SafeDriver.service.PointHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/points")
@CrossOrigin(origins = "*")
public class PointHistoryController {

    private final PointHistoryService pointHistoryService;

    public PointHistoryController(PointHistoryService pointHistoryService) {
        this.pointHistoryService = pointHistoryService;
    }

    @GetMapping("/history")
    public ResponseEntity<PointHistoryResponseDTO> getPointHistory(
            @RequestParam("period") HistoryPeriod period,
            Authentication authentication) {

        User userPrincipal = (User) authentication.getPrincipal();
        UUID userId = userPrincipal.getUserId();

        PointHistoryResponseDTO history = pointHistoryService.getPointHistory(userId, period);

        return ResponseEntity.ok(history);
    }
}
