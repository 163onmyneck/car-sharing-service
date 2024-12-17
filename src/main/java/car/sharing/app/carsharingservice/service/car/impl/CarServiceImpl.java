package car.sharing.app.carsharingservice.service.car.impl;

import car.sharing.app.carsharingservice.dto.car.CarRequestDto;
import car.sharing.app.carsharingservice.dto.car.CarResponseDto;
import car.sharing.app.carsharingservice.exception.EntityNotFoundException;
import car.sharing.app.carsharingservice.mapper.CarMapper;
import car.sharing.app.carsharingservice.model.Car;
import car.sharing.app.carsharingservice.repository.car.CarRepository;
import car.sharing.app.carsharingservice.service.car.CarService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Override
    public CarResponseDto save(CarRequestDto carRequestDto) {
        Car model = carMapper.toModel(carRequestDto);
        return carMapper.toDto(carRepository.save(model));
    }

    @Override
    public List<CarResponseDto> getAll() {
        return carRepository.findAll().stream()
                .map(carMapper::toDto)
                .toList();
    }

    @Override
    public CarResponseDto getInfo(Long id) {
        return carMapper.toDto(carRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can not find car with id " + id)));
    }

    @Override
    public CarResponseDto update(Long id, CarRequestDto dto) {
        Car car = carRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can not find car with id " + id));
        Car proxy = carMapper.toModel(dto);
        return carMapper.toDto(carRepository.save(carMapper.updateCar(car, proxy)));
    }

    @Override
    public void deleteById(Long id) {
        carRepository.deleteById(id);
    }
}
