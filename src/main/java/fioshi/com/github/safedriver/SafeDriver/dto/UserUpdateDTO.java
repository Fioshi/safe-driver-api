package fioshi.com.github.safedriver.SafeDriver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDTO {

    private String fullName;
    private String profilePictureUrl;
    private String phoneNumber;
    private String corporateEmail;
    private String automobileType;
    private String drivingGoal;

}
