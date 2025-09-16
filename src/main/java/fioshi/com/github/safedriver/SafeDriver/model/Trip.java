package fioshi.com.github.safedriver.SafeDriver.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "trip")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

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

}
