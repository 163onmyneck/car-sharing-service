package car.sharing.app.carsharingservice.service.payment;

import car.sharing.app.carsharingservice.dto.payment.PaymentDto;
import car.sharing.app.carsharingservice.dto.payment.PaymentRequestDto;
import java.net.MalformedURLException;
import java.util.List;

public interface PaymentService {
    List<PaymentDto> getAllPaymentsByUserId(Long userId);

    PaymentDto createPayment(PaymentRequestDto paymentRequestDto) throws MalformedURLException;

    void checkAndUpdatePaymentStatus();
}
