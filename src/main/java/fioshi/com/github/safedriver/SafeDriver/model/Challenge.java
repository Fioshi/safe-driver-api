package fioshi.com.github.safedriver.SafeDriver.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    private int points;

    // You might want to add fields to define the challenge logic, for example:
    // private String challengeType; // e.g., "DISTANCE_KM", "NO_HARD_BRAKING"
    // private double targetValue;   // e.g., 50.0 (for 50km)
}
