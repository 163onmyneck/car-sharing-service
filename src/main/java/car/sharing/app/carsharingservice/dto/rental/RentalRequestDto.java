package car.sharing.app.carsharingservice.dto.rental;

import java.time.LocalDate;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class RentalRequestDto {
    private LocalDate returnDate;
    private Long carId;
    private Long userId;
}
