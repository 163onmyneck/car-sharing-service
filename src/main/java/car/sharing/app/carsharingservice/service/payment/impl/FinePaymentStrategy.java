package car.sharing.app.carsharingservice.service.payment.impl;

import car.sharing.app.carsharingservice.model.Payment;
import car.sharing.app.carsharingservice.model.Rental;
import car.sharing.app.carsharingservice.service.payment.PaymentStrategy;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Component;

@Component
public class FinePaymentStrategy implements PaymentStrategy {
    private static final double FINE_MULTIPLIER = 1.1;

    @Override
    public Payment.Type getPaymentType() {
        return Payment.Type.FINE;
    }

    @Override
    public BigDecimal calculatePayment(Rental rental) {
        long totalDays = ChronoUnit.DAYS.between(
                rental.getRentalDate(), rental.getReturnDate());

        return rental.getCar().getFeeUsd()
                .multiply(BigDecimal.valueOf(totalDays))
                .multiply(BigDecimal.valueOf(FINE_MULTIPLIER));
    }
}
