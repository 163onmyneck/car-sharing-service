package car.sharing.app.carsharingservice.service.rental;

import car.sharing.app.carsharingservice.dto.rental.RentalDto;
import java.util.List;

public interface RentalService {
    RentalDto createRental(RentalDto rentalDto);

    List<RentalDto> getAllRentalsByUserId(Long userId);

    void returnRental(Long rentalId);
}
