package fioshi.com.github.safedriver.SafeDriver.controller;

import fioshi.com.github.safedriver.SafeDriver.dto.TripAnalysisRequestDTO;
import fioshi.com.github.safedriver.SafeDriver.dto.TripErrorResponseDTO;
import fioshi.com.github.safedriver.SafeDriver.dto.TripSummaryResponseDTO;
import fioshi.com.github.safedriver.SafeDriver.model.Driver;
import fioshi.com.github.safedriver.SafeDriver.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService;

    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<?> analyzeTrip(@RequestBody TripAnalysisRequestDTO request, @AuthenticationPrincipal Driver driver) {
        if (driver == null) {
            return ResponseEntity.status(401).body(new TripErrorResponseDTO("Usuário não autenticado."));
        }
        Integer driverId = driver.getId_motorista();
        try {
            TripSummaryResponseDTO response = tripService.analyzeTrip(request, driverId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new TripErrorResponseDTO(e.getMessage()));
        } catch (Exception e) {
             return ResponseEntity.internalServerError().body(new TripErrorResponseDTO("Erro interno ao analisar a viagem."));
        }
    }
}
