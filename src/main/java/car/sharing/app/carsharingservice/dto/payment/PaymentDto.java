package car.sharing.app.carsharingservice.dto.payment;

import java.math.BigDecimal;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class PaymentDto {
    private Long id;
    private Long rentalId;
    private String status;
    private String type;
    private String sessionUrl;
    private String sessionId;
    private BigDecimal amountToPay;
}
