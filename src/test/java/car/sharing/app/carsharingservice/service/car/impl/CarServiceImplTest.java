package car.sharing.app.carsharingservice.service.car.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import car.sharing.app.carsharingservice.dto.car.CarRequestDto;
import car.sharing.app.carsharingservice.dto.car.CarResponseDto;
import car.sharing.app.carsharingservice.exception.EntityNotFoundException;
import car.sharing.app.carsharingservice.mapper.CarMapper;
import car.sharing.app.carsharingservice.model.Car;
import car.sharing.app.carsharingservice.repository.car.CarRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {
    private static final Long DEFAULT_ID = 1L;
    @Mock
    private CarRepository carRepository;

    @Mock
    private CarMapper carMapper;

    @InjectMocks
    private CarServiceImpl carService;

    @Test
    @DisplayName("Given valid carRequestDto, should return carResponseDto")
    void save_CarWithValidCredentials_ShouldReturnCarDto() {
        CarResponseDto expected = new CarResponseDto()
                .setModel("model")
                .setBrand("brand")
                .setCarType(Car.CarType.SEDAN)
                .setFeeUsd(BigDecimal.TEN)
                .setInventory(100)
                .setId(DEFAULT_ID);

        CarRequestDto request = new CarRequestDto()
                .setModel(expected.getModel())
                .setBrand(expected.getBrand())
                .setCarType(expected.getCarType())
                .setFeeUsd(expected.getFeeUsd())
                .setInventory(expected.getInventory());

        Car car = new Car()
                .setModel(expected.getModel())
                .setBrand(expected.getBrand())
                .setId(expected.getId())
                .setCarType(Car.CarType.SEDAN)
                .setInventory(expected.getInventory())
                .setFeeUsd(expected.getFeeUsd());

        Mockito.when(carMapper.toModel(Mockito.any(CarRequestDto.class))).thenReturn(car);
        Mockito.when(carRepository.save(Mockito.any(Car.class))).thenReturn(car);
        Mockito.when(carMapper.toDto(car)).thenReturn(expected);

        CarResponseDto actual = carService.save(request);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should return list with 2 cars")
    void getAll_ShouldReturnAllExistingCars() {
        Car car1 = new Car()
                .setModel("model1")
                .setBrand("brand1")
                .setFeeUsd(BigDecimal.TEN)
                .setInventory(100)
                .setId(DEFAULT_ID)
                .setCarType(Car.CarType.SEDAN);

        Car car2 = new Car()
                .setModel("model2")
                .setBrand("brand2")
                .setFeeUsd(BigDecimal.TEN)
                .setInventory(100)
                .setId(DEFAULT_ID + 1L)
                .setCarType(Car.CarType.SEDAN);

        CarResponseDto carResponseDto1 = new CarResponseDto()
                .setModel(car1.getModel())
                .setBrand(car1.getBrand())
                .setId(car1.getId())
                .setInventory(car1.getInventory())
                .setCarType(car1.getCarType())
                .setFeeUsd(car1.getFeeUsd());

        CarResponseDto carResponseDto2 = new CarResponseDto()
                .setModel(car2.getModel())
                .setBrand(car2.getBrand())
                .setId(car2.getId())
                .setInventory(car2.getInventory())
                .setCarType(car2.getCarType())
                .setFeeUsd(car2.getFeeUsd());

        List<Car> cars = List.of(car1, car2);

        List<CarResponseDto> expected = List.of(carResponseDto1, carResponseDto2);

        Mockito.when(carRepository.findAll()).thenReturn(cars);
        Mockito.when(carMapper.toDto(car1)).thenReturn(carResponseDto1);
        Mockito.when(carMapper.toDto(car2)).thenReturn(carResponseDto2);

        List<CarResponseDto> actual = carService.getAll();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Given incorrect car id. Should throw EntityNotFoundException")
    void getInfo_IncorrectId_ShouldThrowException() {
        Mockito.when(carRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> carService.getInfo(Mockito.anyLong()));
    }

    @Test
    @DisplayName("Given valid carRequestDto, should update and return carResponseDto")
    void update_ValidRequestDto_ShouldReturnCarDto() {
        CarResponseDto expected = new CarResponseDto()
                .setModel("model")
                .setBrand("brand")
                .setCarType(Car.CarType.SEDAN)
                .setFeeUsd(BigDecimal.TEN)
                .setInventory(100)
                .setId(DEFAULT_ID);

        CarRequestDto requestForExpected = new CarRequestDto()
                .setModel(expected.getModel())
                .setBrand(expected.getBrand())
                .setCarType(Car.CarType.SEDAN)
                .setFeeUsd(expected.getFeeUsd())
                .setInventory(expected.getInventory());

        Car car = new Car()
                .setModel(expected.getModel())
                .setBrand(expected.getBrand())
                .setId(expected.getId())
                .setCarType(Car.CarType.SEDAN)
                .setInventory(expected.getInventory())
                .setFeeUsd(expected.getFeeUsd());

        Mockito.when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));
        Mockito.when(carMapper.toModel(requestForExpected)).thenReturn(car);
        Mockito.when(carMapper.updateCar(Mockito.any(Car.class), Mockito.any(Car.class)))
                .thenReturn(car);
        Mockito.when(carRepository.save(Mockito.any(Car.class))).thenReturn(car);
        Mockito.when(carMapper.toDto(car)).thenReturn(expected);

        CarResponseDto actual = carService.update(car.getId(), requestForExpected);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Given incorrect car id. Should throw EntityNotFoundException")
    void update_InvalidId_ShouldReturnCarDto() {
        Mockito.when(carRepository.findById(DEFAULT_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> carService.update(
                DEFAULT_ID, new CarRequestDto()));
    }
}
