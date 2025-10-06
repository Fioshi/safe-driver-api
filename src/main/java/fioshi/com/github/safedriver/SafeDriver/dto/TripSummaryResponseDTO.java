package fioshi.com.github.safedriver.SafeDriver.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
public class TripSummaryResponseDTO {

    private UUID tripId;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private BigDecimal distanceKm;
    private BigDecimal economySavedBrl;
    private Integer pointsEarned;
    private BigDecimal endLatitude;
    private BigDecimal endLongitude;
    private String message;

    // O CAMPO feedbackText FOI REMOVIDO DAQUI
}