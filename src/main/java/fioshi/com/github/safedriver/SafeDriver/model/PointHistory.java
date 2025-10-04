package fioshi.com.github.safedriver.SafeDriver.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private int points;

    private String reason;

    private LocalDateTime timestamp;

    public PointHistory(User user, int points, String reason) {
        this.user = user;
        this.points = points;
        this.reason = reason;
        this.timestamp = LocalDateTime.now();
    }
}
