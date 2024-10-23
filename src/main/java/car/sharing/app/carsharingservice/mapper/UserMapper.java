package car.sharing.app.carsharingservice.mapper;

import car.sharing.app.carsharingservice.config.MapperConfig;
import car.sharing.app.carsharingservice.dto.user.UserResponseDto;
import car.sharing.app.carsharingservice.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {

    UserResponseDto toDto(User user);

}
