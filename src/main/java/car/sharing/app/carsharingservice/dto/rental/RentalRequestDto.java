package car.sharing.app.carsharingservice.dto.rental;

import java.time.LocalDate;
import lombok.Data;

@Data
public class RentalRequestDto {
    private LocalDate returnDate;
    private Long carId;
    private Long userId;
}
