package fioshi.com.github.safedriver.SafeDriver.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class TripSummaryResponseDTO {

    private Long tripId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double totalDistanceKm;
    private Long durationSeconds;
    private Double averageSpeedKmH;
    private Integer hardBrakingCount;
    private Integer suddenAccelerationCount;
    private Integer speedingEventsCount;
    private Integer performanceScore;
    private Integer pointsEarned;
    private String message;

}
