package car.sharing.app.carsharingservice.service.car.impl;

import car.sharing.app.carsharingservice.dto.car.CarRequestDto;
import car.sharing.app.carsharingservice.dto.car.CarResponseDto;
import car.sharing.app.carsharingservice.exception.EntityNotFoundException;
import car.sharing.app.carsharingservice.mapper.CarMapper;
import car.sharing.app.carsharingservice.model.Car;
import car.sharing.app.carsharingservice.model.CarType;
import car.sharing.app.carsharingservice.repository.car.CarRepository;
import car.sharing.app.carsharingservice.repository.cartype.CarTypeRepository;
import car.sharing.app.carsharingservice.service.car.CarService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final CarTypeRepository carTypeRepository;

    @Override
    public CarResponseDto save(CarRequestDto carRequestDto) {
        CarType carType = carTypeRepository.findByCarTypeName(
                CarType.CarTypeName.valueOf(carRequestDto.getCarType().toUpperCase()))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid car type. Car type:" + carRequestDto.getCarType()));

        Car model = carMapper.toModel(carRequestDto);
        model.setCarType(carType);
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
        Car car = carRepository.findById(id).orElseThrow();
        car.setBrand(dto.getBrand() != null ? dto.getBrand() : car.getBrand());
        car.setModel(dto.getModel() != null ? dto.getModel() : car.getModel());
        car.setFeeUsd(dto.getFeeUsd() != null ? dto.getFeeUsd() : car.getFeeUsd());
        car.setCarType(dto.getCarType() != null ? carTypeRepository.findByCarTypeName(
                        CarType.CarTypeName.valueOf(dto.getCarType().toUpperCase()))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid car type. Car type:" + dto.getCarType()))
                : car.getCarType());
        return carMapper.toDto(carRepository.save(car));
    }

    @Override
    public void deleteById(Long id) {
        carRepository.deleteById(id);
    }
}
