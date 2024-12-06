package car.sharing.app.carsharingservice.service.payment.impl;

import car.sharing.app.carsharingservice.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentScheduler {
    private static final Logger logger = LoggerFactory.getLogger(PaymentScheduler.class);
    private final PaymentService paymentService;

    @Scheduled(fixedRate = 300000)
    public void checkPendingPayments() {
        logger.info("Running scheduled payment status check...");
        paymentService.checkAndUpdatePaymentStatus();
    }
}
