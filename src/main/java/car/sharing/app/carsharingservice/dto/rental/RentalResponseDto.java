package car.sharing.app.carsharingservice.dto.rental;

import car.sharing.app.carsharingservice.dto.car.CarResponseDto;
import java.time.LocalDate;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RentalResponseDto {
    private Long id;
    private LocalDate rentalDate;
    private LocalDate returnDate;
    private LocalDate actualReturnDate;
    private CarResponseDto carResponseDto;
    private Long userId;
}
