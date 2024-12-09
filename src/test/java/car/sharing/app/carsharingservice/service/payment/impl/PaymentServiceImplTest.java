package car.sharing.app.carsharingservice.service.payment.impl;

import car.sharing.app.carsharingservice.mapper.PaymentMapper;
import car.sharing.app.carsharingservice.repository.payment.PaymentRepository;
import car.sharing.app.carsharingservice.repository.rental.RentalRepository;
import car.sharing.app.carsharingservice.service.checkout.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class PaymentServiceImplTest {
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private PaymentMapper paymentMapper;
    @Mock
    private PaymentStrategyFactory paymentStrategyFactory;
    @Mock
    private RentalRepository rentalRepository;
    @Mock
    private CheckoutService checkoutService;
    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    void getAllPaymentsByUserId() {

    }

    @Test
    void createPayment() {
    }

    @Test
    void checkAndUpdatePaymentStatus() {
    }
}