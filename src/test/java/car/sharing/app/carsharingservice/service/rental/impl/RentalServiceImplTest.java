package car.sharing.app.carsharingservice.service.rental.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import car.sharing.app.carsharingservice.dto.car.CarResponseDto;
import car.sharing.app.carsharingservice.dto.rental.RentalRequestDto;
import car.sharing.app.carsharingservice.dto.rental.RentalResponseDto;
import car.sharing.app.carsharingservice.dto.rental.RentalSearchParams;
import car.sharing.app.carsharingservice.exception.EntityNotFoundException;
import car.sharing.app.carsharingservice.mapper.CarMapper;
import car.sharing.app.carsharingservice.mapper.RentalMapper;
import car.sharing.app.carsharingservice.model.Car;
import car.sharing.app.carsharingservice.model.Rental;
import car.sharing.app.carsharingservice.model.Role;
import car.sharing.app.carsharingservice.model.User;
import car.sharing.app.carsharingservice.repository.car.CarRepository;
import car.sharing.app.carsharingservice.repository.rental.RentalRepository;
import car.sharing.app.carsharingservice.repository.user.UserRepository;
import car.sharing.app.carsharingservice.service.notification.NotificationService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
class RentalServiceImplTest {
    private static final Long DEFAULT_ID_VALUE = 1L;
    @InjectMocks
    private RentalServiceImpl rentalService;
    @Mock
    private RentalRepository rentalRepository;
    @Mock
    private RentalMapper rentalMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CarRepository carRepository;
    @Mock
    private CarMapper carMapper;
    @Mock
    private NotificationService notificationService;

    @Test
    @DisplayName("Given invalid time, should throw IllegalArgumentException")
    void createRental_InvalidDate_ShouldThrowException() {
        LocalDate invalidDate = LocalDate.now().minusDays(10);
        RentalRequestDto rentalRequestDto = new RentalRequestDto()
                .setReturnDate(invalidDate)
                .setCarId(DEFAULT_ID_VALUE)
                .setUserId(DEFAULT_ID_VALUE);

        assertThrows(IllegalArgumentException.class,
                () -> rentalService.createRental(rentalRequestDto));
    }

    @Test
    @DisplayName("Given invalid rental id, should throw EntityNotFoundException")
    void createRental_InvalidCarId_ShouldThrowException() {
        long invalidCarId = 1000L;
        Mockito.when(carRepository.findById(invalidCarId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> rentalService.createRental(
                new RentalRequestDto()
                        .setCarId(invalidCarId)
                        .setUserId(1L)
                        .setReturnDate(LocalDate.now())));
    }

    @Test
    @DisplayName("Given invalid user id, should throw EntityNotFoundException")
    void createRental_InvalidUserId_ShouldThrowException() {
        Mockito.when(carRepository.findById(DEFAULT_ID_VALUE)).thenReturn(Optional.of(
                Mockito.mock(Car.class)));
        Mockito.when(userRepository.findById(DEFAULT_ID_VALUE)).thenReturn(
                Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> rentalService.createRental(
                new RentalRequestDto()
                        .setCarId(DEFAULT_ID_VALUE)
                        .setUserId(DEFAULT_ID_VALUE)
                        .setReturnDate(LocalDate.now())));
    }

    @Test
    @DisplayName("Given valid rentalResponseDto, should save and return carResponseDto")
    void createRental_AllCredentialsAreValid_ShouldReturnValidDto() {
        Car car = new Car()
                .setId(DEFAULT_ID_VALUE)
                .setBrand("BMW")
                .setModel("model")
                .setCarType(Car.CarType.SEDAN)
                .setDeleted(false)
                .setFeeUsd(BigDecimal.TEN)
                .setInventory(100);

        Car updatedCar = car.decreaseInventory();

        CarResponseDto carResponseDto = new CarResponseDto()
                .setId(updatedCar.getId())
                .setBrand(updatedCar.getBrand())
                .setModel(updatedCar.getModel())
                .setInventory(updatedCar.getInventory())
                .setCarType(updatedCar.getCarType())
                .setFeeUsd(updatedCar.getFeeUsd());

        User user = new User()
                .setId(DEFAULT_ID_VALUE)
                .setRole(Set.of(new Role(Role.RoleName.CUSTOMER)))
                .setEmail("email@test.com")
                .setDeleted(false)
                .setPassword("password")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setTgChatId(213123L);

        Rental rental = new Rental()
                .setCar(car)
                .setUser(user)
                .setReturnDate(LocalDate.now().plusDays(10))
                .setId(DEFAULT_ID_VALUE)
                .setRentalDate(LocalDate.now());

        RentalResponseDto rentalResponseDto = new RentalResponseDto()
                .setId(rental.getId())
                .setCarResponseDto(carResponseDto)
                .setUserId(rental.getUser().getId())
                .setReturnDate(rental.getReturnDate())
                .setRentalDate(rental.getRentalDate());

        Mockito.when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));
        Mockito.when(carRepository.save(car)).thenReturn(updatedCar);
        Mockito.when(carMapper.toDto(updatedCar)).thenReturn(carResponseDto);
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(rentalRepository.save(Mockito.any(Rental.class))).thenReturn(rental);
        Mockito.when(rentalMapper.toDto(rental)).thenReturn(rentalResponseDto);

        RentalRequestDto rentalRequestDto = new RentalRequestDto()
                .setCarId(car.getId())
                .setUserId(user.getId())
                .setReturnDate(rental.getReturnDate());

        RentalResponseDto actualResponseDto = rentalService.createRental(rentalRequestDto);

        assertThat(actualResponseDto.getCarResponseDto()).isEqualTo(carResponseDto);
        assertThat(user.getId()).isEqualTo(actualResponseDto.getUserId());
    }

    @Test
    @DisplayName("Get all rentals by valid user id. Should return list with "
            + "2 carResponseDtos")
    void getAllRentalsByUserId_ValidUserId_ShouldReturnList() {
        Car car = new Car()
                .setId(DEFAULT_ID_VALUE)
                .setBrand("BMW")
                .setModel("model")
                .setCarType(Car.CarType.SEDAN)
                .setDeleted(false)
                .setFeeUsd(BigDecimal.TEN)
                .setInventory(100);

        CarResponseDto carResponseDto = new CarResponseDto()
                .setId(car.getId())
                .setBrand(car.getBrand())
                .setModel(car.getModel())
                .setInventory(car.getInventory())
                .setCarType(car.getCarType())
                .setFeeUsd(car.getFeeUsd());

        User user = new User()
                .setId(DEFAULT_ID_VALUE)
                .setRole(Set.of(new Role(Role.RoleName.CUSTOMER)))
                .setEmail("email@test.com")
                .setDeleted(false)
                .setPassword("password")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setTgChatId(213123L);

        Rental rental1 = new Rental()
                .setCar(car)
                .setUser(user)
                .setReturnDate(LocalDate.now().plusDays(10))
                .setId(DEFAULT_ID_VALUE)
                .setRentalDate(LocalDate.now());

        Rental rental2 = new Rental()
                .setCar(car)
                .setUser(user)
                .setReturnDate(LocalDate.now().plusDays(10))
                .setId(DEFAULT_ID_VALUE)
                .setRentalDate(LocalDate.now());

        RentalResponseDto rentalResponseDto1 = new RentalResponseDto()
                .setId(rental1.getId())
                .setCarResponseDto(carResponseDto)
                .setUserId(rental1.getUser().getId())
                .setReturnDate(rental1.getReturnDate())
                .setRentalDate(rental1.getRentalDate());

        RentalResponseDto rentalResponseDto2 = new RentalResponseDto()
                .setId(rental1.getId())
                .setCarResponseDto(carResponseDto)
                .setUserId(rental1.getUser().getId())
                .setReturnDate(rental1.getReturnDate())
                .setRentalDate(rental1.getRentalDate());

        List<Rental> rentals = List.of(rental1, rental2);

        List<RentalResponseDto> expectedRentals = List.of(rentalResponseDto1, rentalResponseDto2);

        Mockito.when(rentalRepository.getAllRentalsByUserId(user.getId())).thenReturn(rentals);
        Mockito.when(rentalMapper.toDto(rental1)).thenReturn(rentalResponseDto1);
        Mockito.when(rentalMapper.toDto(rental2)).thenReturn(rentalResponseDto2);

        List<RentalResponseDto> actual = rentalService.getAllRentalsByUserId(user.getId());

        Assertions.assertThat(actual).hasSize(expectedRentals.size());
        assertThat(actual).isEqualTo(expectedRentals);
    }

    @Test
    @DisplayName("Given invalid rental id, should throw EntityNotFoundException")
    void returnRental_InvalidRentalId_ShouldThrowException() {
        Mockito.when(rentalRepository.findById(DEFAULT_ID_VALUE))
                .thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> rentalService.returnRental(DEFAULT_ID_VALUE));
    }

    @Test
    @DisplayName("Return rental by valid id, should return rentalResponseDto "
            + "and fill its properties")
    void returnRental_ValidRental_ShouldReturnValidDto() {
        Car car = new Car()
                .setId(DEFAULT_ID_VALUE)
                .setBrand("BMW")
                .setModel("model")
                .setCarType(Car.CarType.SEDAN)
                .setDeleted(false)
                .setFeeUsd(BigDecimal.TEN)
                .setInventory(100);

        CarResponseDto carResponseDto = new CarResponseDto()
                .setId(car.getId())
                .setBrand(car.getBrand())
                .setModel(car.getModel())
                .setInventory(car.increaseInventory().getInventory())
                .setCarType(car.getCarType())
                .setFeeUsd(car.getFeeUsd());

        Rental rental = new Rental()
                .setCar(car)
                .setUser(new User())
                .setReturnDate(LocalDate.now().plusDays(10))
                .setId(DEFAULT_ID_VALUE)
                .setRentalDate(LocalDate.now());

        RentalResponseDto rentalResponseDto = new RentalResponseDto()
                .setId(rental.getId())
                .setCarResponseDto(carResponseDto)
                .setUserId(rental.getUser().getId())
                .setReturnDate(rental.getReturnDate())
                .setRentalDate(rental.getRentalDate());

        Mockito.when(rentalRepository.findById(DEFAULT_ID_VALUE)).thenReturn(Optional.of(rental));
        Mockito.when(carRepository.save(car)).thenReturn(car.increaseInventory());
        Mockito.when(rentalRepository.save(Mockito.any(Rental.class))).thenReturn(rental);
        Mockito.when(rentalMapper.toDto(rental)).thenReturn(rentalResponseDto);
        Mockito.when(carMapper.toDto(car)).thenReturn(carResponseDto);

        RentalResponseDto actualRental = rentalService.returnRental(DEFAULT_ID_VALUE);

        assertThat(actualRental.getCarResponseDto()).isEqualTo(carResponseDto);
    }

    @Test
    @DisplayName("Get specific rental by rentalParams. Expecting list with 1 dto")
    void getSpecificRental_ValidSearchParams_ShouldReturnList() {
        RentalSearchParams rentalSearchParams = new RentalSearchParams()
                .setUserId(DEFAULT_ID_VALUE);

        Car car = new Car()
                .setId(DEFAULT_ID_VALUE)
                .setBrand("BMW")
                .setModel("model")
                .setCarType(Car.CarType.SEDAN)
                .setDeleted(false)
                .setFeeUsd(BigDecimal.TEN)
                .setInventory(100);

        CarResponseDto carResponseDto = new CarResponseDto()
                .setId(car.getId())
                .setBrand(car.getBrand())
                .setModel(car.getModel())
                .setInventory(car.increaseInventory().getInventory())
                .setCarType(car.getCarType())
                .setFeeUsd(car.getFeeUsd());

        Rental rental = new Rental()
                .setCar(car)
                .setUser(new User())
                .setReturnDate(LocalDate.now().plusDays(10))
                .setId(DEFAULT_ID_VALUE)
                .setRentalDate(LocalDate.now());

        RentalResponseDto expected = new RentalResponseDto()
                .setId(rental.getId())
                .setCarResponseDto(carResponseDto)
                .setUserId(rental.getUser().getId())
                .setReturnDate(rental.getReturnDate())
                .setRentalDate(rental.getRentalDate());

        Mockito.when(rentalRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(List.of(rental));
        Mockito.when(rentalMapper.toDto(rental)).thenReturn(expected);

        List<RentalResponseDto> actual = rentalService.getSpecificRental(rentalSearchParams);

        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.get(0)).isEqualTo(expected);
    }
}
