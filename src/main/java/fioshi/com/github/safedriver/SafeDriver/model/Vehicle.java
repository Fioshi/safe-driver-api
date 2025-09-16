package fioshi.com.github.safedriver.SafeDriver.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String plate;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @ManyToOne
    @JoinColumn(name = "driver_id_motorista")
    @JsonBackReference
    private Driver driver;
}
