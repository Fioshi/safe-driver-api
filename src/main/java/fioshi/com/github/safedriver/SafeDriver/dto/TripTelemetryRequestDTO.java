package fioshi.com.github.safedriver.SafeDriver.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class TripTelemetryRequestDTO {

    private List<TelemetryPoint> telemetryPoints;
    private List<TripEventDTO> tripEvents;
}
