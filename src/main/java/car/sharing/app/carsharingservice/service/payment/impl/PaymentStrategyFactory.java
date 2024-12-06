package car.sharing.app.carsharingservice.service.payment.impl;

import car.sharing.app.carsharingservice.model.Payment;
import car.sharing.app.carsharingservice.service.payment.PaymentStrategy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class PaymentStrategyFactory {
    private final Map<Payment.Type, PaymentStrategy> strategies = new HashMap<>();

    public PaymentStrategyFactory(List<PaymentStrategy> strategyList) {
        for (PaymentStrategy strategy : strategyList) {
            strategies.put(strategy.getPaymentType(), strategy);
        }
    }

    public PaymentStrategy getStrategy(Payment.Type paymentType) {
        return strategies.get(paymentType);
    }
}
