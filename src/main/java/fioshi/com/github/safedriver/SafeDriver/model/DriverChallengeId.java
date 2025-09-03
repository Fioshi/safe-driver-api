package fioshi.com.github.safedriver.SafeDriver.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DriverChallengeId implements Serializable {

    private Integer driverId;
    private Integer challengeId;

    // Constructors, Getters, Setters, equals, and hashCode

    public DriverChallengeId() {}

    public DriverChallengeId(Integer driverId, Integer challengeId) {
        this.driverId = driverId;
        this.challengeId = challengeId;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public Integer getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(Integer challengeId) {
        this.challengeId = challengeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DriverChallengeId that = (DriverChallengeId) o;
        return Objects.equals(driverId, that.driverId) &&
               Objects.equals(challengeId, that.challengeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(driverId, challengeId);
    }
}
