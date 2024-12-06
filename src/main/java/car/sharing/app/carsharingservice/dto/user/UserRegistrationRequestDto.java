package car.sharing.app.carsharingservice.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class UserRegistrationRequestDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String password;
    @NotBlank
    private String repeatPassword;
}
