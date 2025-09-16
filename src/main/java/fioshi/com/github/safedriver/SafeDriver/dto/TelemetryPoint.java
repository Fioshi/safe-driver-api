package fioshi.com.github.safedriver.SafeDriver.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TelemetryPoint {
    private LocalDateTime timestamp;
    private Double latitude;
    private Double longitude;
    private Double speedKmH;
}
