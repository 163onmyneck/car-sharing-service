package car.sharing.app.carsharingservice.service.payment.impl;

import car.sharing.app.carsharingservice.dto.payment.PaymentDto;
import car.sharing.app.carsharingservice.dto.payment.PaymentRequestDto;
import car.sharing.app.carsharingservice.exception.EntityNotFoundException;
import car.sharing.app.carsharingservice.mapper.PaymentMapper;
import car.sharing.app.carsharingservice.model.Payment;
import car.sharing.app.carsharingservice.model.Rental;
import car.sharing.app.carsharingservice.repository.payment.PaymentRepository;
import car.sharing.app.carsharingservice.repository.rental.RentalRepository;
import car.sharing.app.carsharingservice.service.checkout.CheckoutService;
import car.sharing.app.carsharingservice.service.payment.PaymentService;
import car.sharing.app.carsharingservice.service.payment.PaymentStrategy;
import com.stripe.model.checkout.Session;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private final PaymentStrategyFactory paymentStrategyFactory;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final RentalRepository rentalRepository;
    private final CheckoutService checkoutService;

    @Override
    public List<PaymentDto> getAllPaymentsByUserId(Long userId) {
        return paymentRepository.findAllByUserId(userId).stream()
                .map(paymentMapper::toDto)
                .toList();
    }

    @Override
    public PaymentDto createPayment(PaymentRequestDto paymentRequestDto)
            throws MalformedURLException {
        Rental rental = rentalRepository.findById(paymentRequestDto.getRentalId())
                .orElseThrow(() -> new EntityNotFoundException("Cannot "
                        + " find rental by id: " + paymentRequestDto.getRentalId()));

        PaymentStrategy strategy = paymentStrategyFactory.getStrategy(
                Payment.Type.valueOf(paymentRequestDto.getType().toUpperCase()));

        if (strategy == null) {
            throw new IllegalArgumentException("Invalid payment type: "
                    + paymentRequestDto.getType());
        }

        BigDecimal totalAmount = strategy.calculatePayment(rental);

        Payment payment = new Payment()
                .setType(Payment.Type.valueOf(paymentRequestDto.getType().toUpperCase()))
                .setRental(rental)
                .setAmountToPay(totalAmount)
                .setStatus(Payment.Status.PENDING);
        Session checkoutSession = checkoutService.createCheckoutSession(payment.getAmountToPay());
        if (checkoutSession != null) {
            payment.setSessionId(checkoutSession.getId())
                    .setSessionUrl(new URL(checkoutSession.getUrl()));
        } else {
            throw new IllegalArgumentException("Session can not be null");
        }
        return paymentMapper.toDto(paymentRepository.save(payment));
    }

    public void checkAndUpdatePaymentStatus() {
        List<Payment> pendingPayments = paymentRepository.findAllByStatus(Payment.Status.PENDING);
        for (Payment payment : pendingPayments) {
            String paymentStatus = checkoutService.getPaymentStatus(payment.getSessionId());
            if (paymentStatus.equalsIgnoreCase("PAID")) {
                payment.setStatus(Payment.Status.PAID);
            } else if (paymentStatus.equalsIgnoreCase("EXPIRED")) {
                payment.setStatus(Payment.Status.EXPIRED);
            }
            paymentRepository.save(payment);
        }
    }
}
