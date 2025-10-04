package fioshi.com.github.safedriver.SafeDriver.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "user_challenges")
public class UserChallenge {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_challenge_id")
    private UUID userChallengeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private BigDecimal progress;

    @Column(name = "completed_at")
    private OffsetDateTime completedAt;

    @Column(name = "redeemed_at")
    private OffsetDateTime redeemedAt;
}
