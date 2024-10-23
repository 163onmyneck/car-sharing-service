package car.sharing.app.carsharingservice.dto.user;

import lombok.Data;

@Data
public class UserRequestDto {
    private String email;
    private String firstName;
    private String lastName;
}
