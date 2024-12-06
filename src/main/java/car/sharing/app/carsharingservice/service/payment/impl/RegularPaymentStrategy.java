package car.sharing.app.carsharingservice.service.payment.impl;

import car.sharing.app.carsharingservice.model.Payment;
import car.sharing.app.carsharingservice.model.Rental;
import car.sharing.app.carsharingservice.service.payment.PaymentStrategy;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Component;

@Component
public class RegularPaymentStrategy implements PaymentStrategy {
    public Payment.Type getPaymentType() {
        return Payment.Type.PAYMENT;
    }

    public BigDecimal calculatePayment(Rental rental) {
        long totalDays = ChronoUnit.DAYS.between(rental.getRentalDate(), rental.getReturnDate());
        return rental.getCar().getFeeUsd().multiply(BigDecimal.valueOf(totalDays));
    }
}
