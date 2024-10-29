package car.sharing.app.carsharingservice.mapper;

import car.sharing.app.carsharingservice.config.MapperConfig;
import car.sharing.app.carsharingservice.dto.car.CarRequestDto;
import car.sharing.app.carsharingservice.dto.car.CarResponseDto;
import car.sharing.app.carsharingservice.model.Car;
import car.sharing.app.carsharingservice.model.CarType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CarMapper {
    @Mapping(target = "carType", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Car toModel(CarRequestDto carRequestDto);

    @Mapping(target = "carType", source = "carType.carTypeName")
    CarResponseDto toDto(Car car);

    default String map(CarType carType) {
        return carType != null ? carType.getCarTypeName().name() : null;
    }
}
