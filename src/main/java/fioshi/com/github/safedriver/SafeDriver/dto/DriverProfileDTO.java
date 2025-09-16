package fioshi.com.github.safedriver.SafeDriver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverProfileDTO {

    private Integer totalPoints;
    private int rankingPosition;
    private long completedChallengesCount;

}
