package fioshi.com.github.safedriver.SafeDriver.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "trip_id")
    private UUID tripId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "start_time", nullable = false)
    private OffsetDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private OffsetDateTime endTime;

    @Column(name = "distance_km", nullable = false)
    private BigDecimal distanceKm;

    @Column(name = "economy_saved_brl", nullable = false)
    private BigDecimal economySavedBrl;

    @Column(name = "points_earned", nullable = false)
    private Integer pointsEarned;

    @Column(name = "end_latitude")
    private BigDecimal endLatitude;

    @Column(name = "end_longitude")
    private BigDecimal endLongitude;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripEvent> tripEvents;

    @Column(name = "feedback_text", columnDefinition = "TEXT")
    private String feedbackText;
}
