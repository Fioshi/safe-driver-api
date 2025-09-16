package fioshi.com.github.safedriver.SafeDriver.model;

import fioshi.com.github.safedriver.SafeDriver.model.enums.ChallengeStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class DriverChallenge {

    @EmbeddedId
    @EqualsAndHashCode.Include
    @ToString.Include
    private DriverChallengeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("driverId")
    @JoinColumn(name = "driver_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("challengeId")
    @JoinColumn(name = "challenge_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Challenge challenge;

    @Enumerated(EnumType.STRING)
    @EqualsAndHashCode.Include
    @ToString.Include
    private ChallengeStatus status;

    @EqualsAndHashCode.Include
    @ToString.Include
    private int progress = 0;

    @EqualsAndHashCode.Include
    @ToString.Include
    private LocalDateTime lastUpdated;

    public DriverChallenge(Driver driver, Challenge challenge) {
        this.driver = driver;
        this.challenge = challenge;
        this.id = new DriverChallengeId(driver.getId_motorista(), challenge.getId());
        this.status = ChallengeStatus.IN_PROGRESS;
        this.progress = 0;
        this.lastUpdated = LocalDateTime.now();
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}
