package car.sharing.app.carsharingservice.mapper;

import car.sharing.app.carsharingservice.config.MapperConfig;
import car.sharing.app.carsharingservice.dto.user.UserDto;
import car.sharing.app.carsharingservice.dto.user.UserRegistrationRequestDto;
import car.sharing.app.carsharingservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    @Mapping(target = "role", ignore = true)
    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "tgChatId", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    User toModel(UserRegistrationRequestDto dto);

}
