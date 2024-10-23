package car.sharing.app.carsharingservice.service.user;

import car.sharing.app.carsharingservice.dto.user.UserRequestDto;
import car.sharing.app.carsharingservice.dto.user.UserResponseDto;

public interface UserService {
    void updateRole(Long id, String role);

    UserResponseDto getCurrentProfileInfo(Long id);

    UserResponseDto updateProfileData(Long id, UserRequestDto dto);
}
