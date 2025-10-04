package fioshi.com.github.safedriver.SafeDriver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChallengeDTO {
    private Integer challengeId;
    private String title;
    private String description;
    private Integer pointsReward;
    private Boolean isActive;
}
