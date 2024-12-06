package car.sharing.app.carsharingservice.mapper;

import car.sharing.app.carsharingservice.config.MapperConfig;
import car.sharing.app.carsharingservice.dto.rental.RentalResponseDto;
import car.sharing.app.carsharingservice.model.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface RentalMapper {
    @Mapping(target = "carResponseDto", ignore = true)
    @Mapping(target = "userId", source = "user.id")
    RentalResponseDto toDto(Rental rental);
}
