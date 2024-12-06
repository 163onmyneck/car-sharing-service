package car.sharing.app.carsharingservice.service.rental;

import car.sharing.app.carsharingservice.dto.rental.RentalRequestDto;
import car.sharing.app.carsharingservice.dto.rental.RentalResponseDto;
import car.sharing.app.carsharingservice.dto.rental.RentalSearchParams;
import java.util.List;

public interface RentalService {
    RentalResponseDto createRental(RentalRequestDto requestDto);

    List<RentalResponseDto> getAllRentalsByUserId(Long userId);

    RentalResponseDto returnRental(Long rentalId);

    List<RentalResponseDto> getSpecificRental(RentalSearchParams rentalSearchParams);
}
