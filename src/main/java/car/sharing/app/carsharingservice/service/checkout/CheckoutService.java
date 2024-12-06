package car.sharing.app.carsharingservice.service.checkout;

import com.stripe.model.checkout.Session;
import java.math.BigDecimal;

public interface CheckoutService {
    Session createCheckoutSession(BigDecimal amount);

    String getPaymentStatus(String sessionId);
}
