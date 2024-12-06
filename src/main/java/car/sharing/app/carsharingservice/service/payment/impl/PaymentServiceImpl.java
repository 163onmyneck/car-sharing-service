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

@Service
@RequiredArgsConstructor
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
    public PaymentDto createPayment(PaymentRequestDto paymentRequestDto) {
        Rental rental = rentalRepository.findByIdNotActive(paymentRequestDto.getRentalId())
                .orElseThrow(() -> new EntityNotFoundException("Cannot "
                        + " find rental by id: " + paymentRequestDto.getRentalId()));

        PaymentStrategy strategy = paymentStrategyFactory.getStrategy(
                Payment.Type.valueOf(paymentRequestDto.getType().toUpperCase())
        );

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
        try {
            if (checkoutSession != null) {
                payment.setSessionId(checkoutSession.getId())
                        .setSessionUrl(new URL(checkoutSession.getUrl()));
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not create URL. Url: " + checkoutSession.getUrl());
        }
        return paymentMapper.toDto(paymentRepository.save(payment));
    }

    public void checkAndUpdatePaymentStatus() {
        List<Payment> pendingPayments = paymentRepository.findAllByStatus(Payment.Status.PENDING);
        for (Payment payment : pendingPayments) {
            String paymentStatus = checkoutService.getPaymentStatus(payment.getSessionId());
            if (paymentStatus.equalsIgnoreCase(Payment.Status.PAID.toString())) {
                payment.setStatus(Payment.Status.PAID);
            } else if (paymentStatus.equalsIgnoreCase(Payment.Status.EXPIRED.toString())) {
                payment.setStatus(Payment.Status.EXPIRED);
            }
            paymentRepository.save(payment);
        }
    }
}
