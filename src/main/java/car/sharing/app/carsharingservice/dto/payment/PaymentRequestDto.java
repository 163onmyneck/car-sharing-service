package car.sharing.app.carsharingservice.dto.payment;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class PaymentRequestDto {
    private String type;
    private Long rentalId;
}
