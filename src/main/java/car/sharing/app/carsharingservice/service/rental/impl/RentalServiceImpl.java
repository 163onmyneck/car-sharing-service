package car.sharing.app.carsharingservice.service.rental.impl;

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
import car.sharing.app.carsharingservice.repository.rental.RentalSpecifications;
import car.sharing.app.carsharingservice.repository.rental.SpecificationBuilder;
import car.sharing.app.carsharingservice.repository.user.UserRepository;
import car.sharing.app.carsharingservice.service.notification.NotificationService;
import car.sharing.app.carsharingservice.service.rental.RentalService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;
    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public RentalResponseDto createRental(RentalRequestDto requestDto) {
        if (requestDto.getReturnDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Return date cannot be before current date");
        }
        Car car = carRepository.findById(requestDto.getCarId())
                .orElseThrow(() -> new EntityNotFoundException("Cannot find car with id "
                                                        + requestDto.getCarId()));
        Car updatedCar = carRepository.save(car.decreaseInventory());
        CarResponseDto carDto = carMapper.toDto(updatedCar);

        Rental rental = new Rental();
        rental.setReturnDate(requestDto.getReturnDate());
        rental.setRentalDate(LocalDate.now());
        rental.setCar(updatedCar);
        rental.setUser(userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Cannot find user with id "
                                                        + requestDto.getUserId())));

        List<User> managers = userRepository.getAllByRole(Role.RoleName.MANAGER);

        notificationService.sendMessageToAllManagers("Rental with id " + rental.getId()
                    + " was created. More details: " + rental, managers);

        notificationService.sendMessageToCustomer(rental.getUser().getTgChatId(),
                "Your rental with id " + rental.getId()
                        + " was created. More details: " + rental);

        RentalResponseDto dto = rentalMapper.toDto(rentalRepository.save(rental));
        dto.setCarResponseDto(carDto);
        return dto;
    }

    @Override
    public List<RentalResponseDto> getAllRentalsByUserId(Long userId) {
        return rentalRepository.getAllRentalsByUserId(userId).stream()
                .map(rentalMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public RentalResponseDto returnRental(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find rental with id "
                                                                        + rentalId));
        Car car = rental.getCar();
        Car updatedCar = carRepository.save(car.increaseInventory());
        rental.setActualReturnDate(LocalDate.now());
        rental.setActive(false);
        RentalResponseDto rentalDto = rentalMapper.toDto(rentalRepository.save(rental));
        rentalDto.setCarResponseDto(carMapper.toDto(updatedCar));
        notificationService.sendMessageToAllManagers("Rental with id " + rental.getId()
                + " was returned. More details: " + rental, userRepository.getAllByRole(
                                                                        Role.RoleName.MANAGER));
        notificationService.sendMessageToCustomer(rental.getUser().getTgChatId(),
                "Your rental with id " + rental.getId() + " was returned. More details: "
                                                                                        + rental);
        return rentalDto;
    }

    @Override
    public List<RentalResponseDto> getSpecificRental(RentalSearchParams rentalSearchParams) {
        SpecificationBuilder specificationBuilder = new SpecificationBuilder();

        Specification<Rental> rentalSpecifications = specificationBuilder
                .with(RentalSpecifications.byCarId(rentalSearchParams.getCarId()))
                .with(RentalSpecifications.byDateRange(rentalSearchParams.getFirstDate(),
                        rentalSearchParams.getSecondDate()))
                .with(RentalSpecifications.byIsActive(rentalSearchParams.getIsActive()))
                .with(RentalSpecifications.byUserId(rentalSearchParams.getUserId())).build();

        return rentalRepository.findAll(rentalSpecifications).stream()
                .map(rentalMapper::toDto)
                .toList();
    }
}
