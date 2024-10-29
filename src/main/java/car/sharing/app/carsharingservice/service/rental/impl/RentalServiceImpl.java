package car.sharing.app.carsharingservice.service.rental.impl;

import car.sharing.app.carsharingservice.dto.rental.RentalDto;
import car.sharing.app.carsharingservice.mapper.RentalMapper;
import car.sharing.app.carsharingservice.model.Car;
import car.sharing.app.carsharingservice.model.Rental;
import car.sharing.app.carsharingservice.repository.car.CarRepository;
import car.sharing.app.carsharingservice.repository.rental.RentalRepository;
import car.sharing.app.carsharingservice.repository.user.UserRepository;
import car.sharing.app.carsharingservice.service.rental.RentalService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;
    private final UserRepository userRepository;
    private final CarRepository carRepository;

    @Override
    @Transactional
    public RentalDto createRental(RentalDto rentalDto) {
        Rental rental = rentalMapper.toModel(rentalDto);
        Car car = carRepository.findById(rentalDto.getCarId()).orElse(null);
        car.decreaseInventory();
        carRepository.save(car);
        rental.setCar(car);
        rental.setUser(userRepository.findById(rentalDto.getUserId()).orElse(null));
        return rentalMapper.toDto(rentalRepository.save(rental));
    }

    @Override
    public List<RentalDto> getAllRentalsByUserId(Long userId) {
        return rentalRepository.getAllRentalsByUserId(userId).stream()
                .map(rentalMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void returnRental(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId).orElseThrow(RuntimeException::new);
        Car car = rental.getCar();
        car.increaseInventory();
        carRepository.save(car);
        rental.setActualReturnDate(LocalDate.now());
        rentalRepository.save(rental);
    }
}
