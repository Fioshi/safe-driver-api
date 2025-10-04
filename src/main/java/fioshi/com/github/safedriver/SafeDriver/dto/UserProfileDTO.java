package fioshi.com.github.safedriver.SafeDriver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDTO {

    private Integer totalPoints;
    private int rankingPosition;
    private long completedChallengesCount;

}
