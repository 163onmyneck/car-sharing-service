package car.sharing.app.carsharingservice.service.user;

import car.sharing.app.carsharingservice.dto.user.UserDto;
import car.sharing.app.carsharingservice.dto.user.UserRegistrationRequestDto;
import car.sharing.app.carsharingservice.exception.RegistrationException;

public interface UserService {
    UserDto register(UserRegistrationRequestDto userRequestDto)
                                        throws RegistrationException;

    UserDto updateRole(Long id, String role);

    UserDto getCurrentProfileInfo(Long id);

    UserDto updateProfileData(Long id, UserDto dto);
}
