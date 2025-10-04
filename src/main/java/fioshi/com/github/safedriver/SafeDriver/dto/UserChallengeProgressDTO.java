package fioshi.com.github.safedriver.SafeDriver.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
public class UserChallengeProgressDTO {

    private UUID userChallengeId;
    private Integer challengeId;
    private String title;
    private String description;
    private Integer pointsReward;
    private String status;
    private BigDecimal progress;
    private OffsetDateTime completedAt;
    private OffsetDateTime redeemedAt;
}
