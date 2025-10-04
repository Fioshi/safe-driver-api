package fioshi.com.github.safedriver.SafeDriver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateDTO {

    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;
    private String corporateEmail;
    private String automobileType;
    private String drivingGoal;

}
