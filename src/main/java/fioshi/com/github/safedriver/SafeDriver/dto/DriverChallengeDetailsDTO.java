package fioshi.com.github.safedriver.SafeDriver.dto;

import fioshi.com.github.safedriver.SafeDriver.model.enums.ChallengeStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverChallengeDetailsDTO {

    private Integer id;
    private String title;
    private String description;
    private int progress;
    private int rewardPoints;
    private ChallengeStatus status;

}
