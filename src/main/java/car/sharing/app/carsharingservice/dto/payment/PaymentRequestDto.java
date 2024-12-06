package car.sharing.app.carsharingservice.dto.payment;

import lombok.Data;

@Data
public class PaymentRequestDto {
    private String type;
    private Long rentalId;
}
