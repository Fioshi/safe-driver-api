package fioshi.com.github.safedriver.SafeDriver.controller;

import fioshi.com.github.safedriver.SafeDriver.dto.PointHistoryResponseDTO;
import fioshi.com.github.safedriver.SafeDriver.model.Driver;
import fioshi.com.github.safedriver.SafeDriver.model.enums.HistoryPeriod;
import fioshi.com.github.safedriver.SafeDriver.service.PointHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/points")
public class PointHistoryController {

    @Autowired
    private PointHistoryService pointHistoryService;

    @GetMapping("/history")
    public ResponseEntity<PointHistoryResponseDTO> getPointHistory(
            @RequestParam("period") HistoryPeriod period,
            Authentication authentication) {

        Driver driverPrincipal = (Driver) authentication.getPrincipal();
        Integer driverId = driverPrincipal.getId_motorista();

        PointHistoryResponseDTO history = pointHistoryService.getPointHistory(driverId, period);

        return ResponseEntity.ok(history);
    }
}
