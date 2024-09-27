package car.sharing.app.carsharingservice.service.car;

import car.sharing.app.carsharingservice.dto.car.CarRequestDto;
import car.sharing.app.carsharingservice.dto.car.CarResponseDto;
import java.util.List;

public interface CarService {
    CarResponseDto save(CarRequestDto carRequestDto);

    List<CarResponseDto> getAll();

    CarResponseDto getInfo(Long id);

    CarResponseDto update(Long id, CarRequestDto carRequestDto);

    void deleteById(Long id);
}
