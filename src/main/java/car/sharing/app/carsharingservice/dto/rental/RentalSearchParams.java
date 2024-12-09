package car.sharing.app.carsharingservice.dto.rental;

import java.time.LocalDate;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RentalSearchParams {
    private LocalDate firstDate;
    private LocalDate secondDate;
    private Long carId;
    private Long userId;
    private Boolean isActive;
}
