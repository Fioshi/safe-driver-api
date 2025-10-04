package fioshi.com.github.safedriver.SafeDriver.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class TripEventDTO {
    private String eventType;
    private OffsetDateTime timestamp;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Map<String, Object> details;
}
