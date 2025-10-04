package fioshi.com.github.safedriver.SafeDriver.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UserResponseDTO {

    private UUID userId;
    private String fullName;
    private String email;
    private String profilePictureUrl;
    private String phoneNumber;
    private String corporateEmail;
    private String automobileType;
    private String drivingGoal;
    private OffsetDateTime createdAt;
    private int points;

}
