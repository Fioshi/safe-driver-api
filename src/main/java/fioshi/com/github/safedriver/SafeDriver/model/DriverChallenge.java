package fioshi.com.github.safedriver.SafeDriver.model;

import fioshi.com.github.safedriver.SafeDriver.model.enums.ChallengeStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class DriverChallenge {

    @EmbeddedId
    private DriverChallengeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("driverId")
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("challengeId")
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @Enumerated(EnumType.STRING)
    private ChallengeStatus status;

    private LocalDateTime lastUpdated;

    // Constructors, Getters, and Setters

    public DriverChallenge() {}

    public DriverChallenge(Driver driver, Challenge challenge) {
        this.driver = driver;
        this.challenge = challenge;
        this.id = new DriverChallengeId(driver.getId_motorista(), challenge.getId());
        this.status = ChallengeStatus.IN_PROGRESS;
        this.lastUpdated = LocalDateTime.now();
    }

    public DriverChallengeId getId() {
        return id;
    }

    public void setId(DriverChallengeId id) {
        this.id = id;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public ChallengeStatus getStatus() {
        return status;
    }

    public void setStatus(ChallengeStatus status) {
        this.status = status;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
