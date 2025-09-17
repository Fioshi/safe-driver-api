package fioshi.com.github.safedriver.SafeDriver.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class TripTelemetryRequestDTO {

    private Long vehicleId;
    private List<TelemetryPoint> telemetryPoints;

    // Event counts pre-processed by the app
    private int hardBrakingCount;
    private int suddenAccelerationCount;
    private int speedingEventsCount;
}
