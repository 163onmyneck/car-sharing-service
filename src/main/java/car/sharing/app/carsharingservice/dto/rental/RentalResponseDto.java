package car.sharing.app.carsharingservice.dto.rental;

import car.sharing.app.carsharingservice.dto.car.CarResponseDto;
import java.time.LocalDate;
import lombok.Data;

@Data
public class RentalResponseDto {
    private Long id;
    private LocalDate rentalDate;
    private LocalDate returnDate;
    private LocalDate actualReturnDate;
    private CarResponseDto carResponseDto;
    private Long userId;
}
