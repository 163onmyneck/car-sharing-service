package car.sharing.app.carsharingservice.service.payment;

import car.sharing.app.carsharingservice.model.Payment;
import car.sharing.app.carsharingservice.model.Rental;
import java.math.BigDecimal;

public interface PaymentStrategy {
    Payment.Type getPaymentType();

    BigDecimal calculatePayment(Rental rental);
}
