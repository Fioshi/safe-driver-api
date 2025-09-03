package fioshi.com.github.safedriver.SafeDriver.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    private int points;

    @OneToMany(mappedBy = "challenge")
    private Set<DriverChallenge> driverChallenges;

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Set<DriverChallenge> getDriverChallenges() {
        return driverChallenges;
    }

    public void setDriverChallenges(Set<DriverChallenge> driverChallenges) {
        this.driverChallenges = driverChallenges;
    }
}
