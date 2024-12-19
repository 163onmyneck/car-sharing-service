package car.sharing.app.carsharingservice.dto.user;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class UserDto {
    private Long id;
    private String email;
    private String lastName;
    private String firstName;
}
