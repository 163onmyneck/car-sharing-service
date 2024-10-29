package car.sharing.app.carsharingservice.mapper;

import car.sharing.app.carsharingservice.config.MapperConfig;
import car.sharing.app.carsharingservice.dto.rental.RentalDto;
import car.sharing.app.carsharingservice.model.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface RentalMapper {
    @Mapping(target = "car", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Rental toModel(RentalDto rentalDto);

    @Mapping(target = "carId", source = "car.id")
    @Mapping(target = "userId", source = "user.id")
    RentalDto toDto(Rental rental);
}
