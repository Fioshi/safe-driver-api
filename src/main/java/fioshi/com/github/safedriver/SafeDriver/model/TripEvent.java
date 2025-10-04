package fioshi.com.github.safedriver.SafeDriver.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "trip_events")
public class TripEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "event_id")
    private UUID eventId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(nullable = false)
    private OffsetDateTime timestamp;

    private BigDecimal latitude;
    private BigDecimal longitude;

    @JdbcTypeCode(SqlTypes.JSON)
    private String details;
}
