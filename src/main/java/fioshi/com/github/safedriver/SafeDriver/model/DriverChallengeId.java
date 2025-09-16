package fioshi.com.github.safedriver.SafeDriver.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class DriverChallengeId implements Serializable {

    private Integer driverId;
    private Integer challengeId;

}
