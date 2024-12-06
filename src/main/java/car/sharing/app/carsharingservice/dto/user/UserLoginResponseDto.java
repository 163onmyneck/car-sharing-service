package car.sharing.app.carsharingservice.dto.user;

import lombok.Data;

@Data
public class UserLoginResponseDto {
    private String token;

    public UserLoginResponseDto(String token) {
        this.token = token;
    }
}
