package car.sharing.app.carsharingservice.service.payment.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import car.sharing.app.carsharingservice.dto.payment.PaymentDto;
import car.sharing.app.carsharingservice.dto.payment.PaymentRequestDto;
import car.sharing.app.carsharingservice.exception.EntityNotFoundException;
import car.sharing.app.carsharingservice.mapper.PaymentMapper;
import car.sharing.app.carsharingservice.model.Car;
import car.sharing.app.carsharingservice.model.Payment;
import car.sharing.app.carsharingservice.model.Rental;
import car.sharing.app.carsharingservice.model.Role;
import car.sharing.app.carsharingservice.model.User;
import car.sharing.app.carsharingservice.repository.payment.PaymentRepository;
import car.sharing.app.carsharingservice.repository.rental.RentalRepository;
import car.sharing.app.carsharingservice.service.checkout.CheckoutService;
import car.sharing.app.carsharingservice.service.payment.PaymentStrategy;
import com.stripe.model.checkout.Session;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource("classpath:application-test.properties")
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class PaymentServiceImplTest {
    private static final long DEFAULT_ID_VALUE = 1L;
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
    @DisplayName("Get all payments by correct user id. Returns list with 2 dtos")
    void getAllPaymentsByUserId_ShouldReturnListWithTwoDtos() {
        Car car = new Car()
                .setId(DEFAULT_ID_VALUE)
                .setBrand("BMW")
                .setModel("model")
                .setCarType(Car.CarType.SEDAN)
                .setDeleted(false)
                .setFeeUsd(BigDecimal.TEN)
                .setInventory(100);

        User user = new User()
                .setId(DEFAULT_ID_VALUE)
                .setRoles(Set.of(new Role(Role.RoleName.CUSTOMER)))
                .setEmail("email@test.com")
                .setDeleted(false)
                .setPassword("password")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setTgChatId(213123L);

        Rental rental1 = new Rental()
                .setCar(car)
                .setUser(user)
                .setReturnDate(LocalDate.now().plusDays(10))
                .setId(DEFAULT_ID_VALUE)
                .setRentalDate(LocalDate.now());

        Rental rental2 = new Rental()
                .setCar(car)
                .setUser(user)
                .setReturnDate(LocalDate.now().plusDays(10))
                .setId(2L)
                .setRentalDate(LocalDate.now());

        Payment payment1 = new Payment()
                .setId(DEFAULT_ID_VALUE)
                .setStatus(Payment.Status.PENDING)
                .setType(Payment.Type.PAYMENT)
                .setRental(rental1);

        Payment payment2 = new Payment()
                .setId(2L)
                .setStatus(Payment.Status.PENDING)
                .setType(Payment.Type.PAYMENT)
                .setRental(rental2);

        PaymentDto paymentDto1 = new PaymentDto()
                .setStatus(payment1.getStatus().toString())
                .setSessionId(payment1.getSessionId())
                .setType(payment1.getType().toString())
                .setAmountToPay(payment1.getAmountToPay());

        PaymentDto paymentDto2 = new PaymentDto()
                .setStatus(payment2.getStatus().toString())
                .setSessionId(payment2.getSessionId())
                .setType(payment2.getType().toString())
                .setAmountToPay(payment2.getAmountToPay());

        final List<Payment> payments = List.of(payment1, payment2);

        final List<PaymentDto> expectedPaymentDtos = List.of(paymentDto1, paymentDto2);

        Mockito.when(paymentRepository.findAllByUserId(user.getId()))
                .thenReturn(payments);
        Mockito.when(paymentMapper.toDto(payment1)).thenReturn(paymentDto1);
        Mockito.when(paymentMapper.toDto(payment2)).thenReturn(paymentDto2);

        List<PaymentDto> actual = paymentService.getAllPaymentsByUserId(user.getId());
        assertEquals(expectedPaymentDtos, actual);
    }

    @Test
    @DisplayName("Given incorrect rental id, should throw EntityNotFoundException")
    void createPayment_InvalidRentalId_ShouldThrowException() {
        PaymentRequestDto requestDto = new PaymentRequestDto()
                .setRentalId(DEFAULT_ID_VALUE)
                .setType(String.valueOf(Payment.Type.PAYMENT));

        Mockito.when(rentalRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> paymentService.createPayment(requestDto));

    }

    @Test
    @DisplayName("Given incorrect payment type, should throw IllegalArgumentException")
    void createPayment_InvalidPaymentType_ShouldThrowException() {
        PaymentRequestDto requestDto = new PaymentRequestDto()
                .setRentalId(DEFAULT_ID_VALUE)
                .setType(String.valueOf(Payment.Type.PAYMENT));

        Car car = new Car()
                .setId(DEFAULT_ID_VALUE)
                .setBrand("BMW")
                .setModel("model")
                .setCarType(Car.CarType.SEDAN)
                .setDeleted(false)
                .setFeeUsd(BigDecimal.TEN)
                .setInventory(100);

        User user = new User()
                .setId(DEFAULT_ID_VALUE)
                .setRoles(Set.of(new Role(Role.RoleName.CUSTOMER)))
                .setEmail("email@test.com")
                .setDeleted(false)
                .setPassword("password")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setTgChatId(213123L);

        Rental rental = new Rental()
                .setCar(car)
                .setUser(user)
                .setReturnDate(LocalDate.now().plusDays(10))
                .setId(DEFAULT_ID_VALUE)
                .setRentalDate(LocalDate.now());

        Mockito.when(rentalRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(rental));
        Mockito.when(paymentStrategyFactory.getStrategy(Payment.Type.valueOf(
                requestDto.getType().toUpperCase()))).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> paymentService.createPayment(requestDto));
    }

    @Test
    @DisplayName("Session is null, should throw IllegalArgumentException")
    void createPayment_SessionIsNull_ShouldThrowException() {
        PaymentRequestDto requestDto = new PaymentRequestDto()
                .setRentalId(DEFAULT_ID_VALUE)
                .setType(String.valueOf(Payment.Type.PAYMENT));

        Car car = new Car()
                .setId(DEFAULT_ID_VALUE)
                .setBrand("BMW")
                .setModel("model")
                .setCarType(Car.CarType.SEDAN)
                .setDeleted(false)
                .setFeeUsd(BigDecimal.TEN)
                .setInventory(100);

        User user = new User()
                .setId(DEFAULT_ID_VALUE)
                .setRoles(Set.of(new Role(Role.RoleName.CUSTOMER)))
                .setEmail("email@test.com")
                .setDeleted(false)
                .setPassword("password")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setTgChatId(213123L);

        Rental rental = new Rental()
                .setCar(car)
                .setUser(user)
                .setReturnDate(LocalDate.now().plusDays(10))
                .setId(DEFAULT_ID_VALUE)
                .setRentalDate(LocalDate.now());

        PaymentStrategy paymentStrategy = new RegularPaymentStrategy();
        BigDecimal expectedPrice = paymentStrategy.calculatePayment(rental);

        Mockito.when(rentalRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(rental));
        Mockito.when(paymentStrategyFactory.getStrategy(Payment.Type.valueOf(
                requestDto.getType().toUpperCase()))).thenReturn(paymentStrategy);
        Mockito.when(checkoutService.createCheckoutSession(expectedPrice))
                .thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> paymentService.createPayment(requestDto));
    }

    @Test
    @DisplayName("Given valid PaymentRequestDto. Should return Payment dto")
    void createPaymentValidDto_ShouldReturnPaymentDto() throws MalformedURLException {
        PaymentRequestDto requestDto = new PaymentRequestDto()
                .setRentalId(DEFAULT_ID_VALUE)
                .setType(String.valueOf(Payment.Type.PAYMENT));

        Car car = new Car()
                .setId(DEFAULT_ID_VALUE)
                .setBrand("BMW")
                .setModel("model")
                .setCarType(Car.CarType.SEDAN)
                .setDeleted(false)
                .setFeeUsd(BigDecimal.TEN)
                .setInventory(100);

        User user = new User()
                .setId(DEFAULT_ID_VALUE)
                .setRoles(Set.of(new Role(Role.RoleName.CUSTOMER)))
                .setEmail("email@test.com")
                .setDeleted(false)
                .setPassword("password")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setTgChatId(213123L);

        Rental rental = new Rental()
                .setCar(car)
                .setUser(user)
                .setReturnDate(LocalDate.now().plusDays(10))
                .setId(DEFAULT_ID_VALUE)
                .setRentalDate(LocalDate.now());

        PaymentStrategy paymentStrategy = new RegularPaymentStrategy();
        BigDecimal expectedPrice = paymentStrategy.calculatePayment(rental);

        Payment payment = new Payment()
                .setType(Payment.Type.valueOf(requestDto.getType().toUpperCase()))
                .setRental(rental)
                .setAmountToPay(expectedPrice)
                .setStatus(Payment.Status.PENDING)
                .setSessionId("sessionId")
                .setSessionUrl(new URL("https://checkout.stripe.com/c/pay/cs_test"))
                .setId(DEFAULT_ID_VALUE);

        final PaymentDto expected = new PaymentDto()
                .setSessionUrl(payment.getSessionId())
                .setStatus(payment.getStatus().toString())
                .setId(payment.getId())
                .setType(payment.getType().toString())
                .setRentalId(rental.getId())
                .setAmountToPay(expectedPrice)
                .setSessionId(payment.getSessionId());

        Session expectedSession = new Session();
        expectedSession.setId(payment.getSessionId());
        expectedSession.setUrl(payment.getSessionUrl().toString());

        Mockito.when(rentalRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(rental));
        Mockito.when(paymentStrategyFactory.getStrategy(Payment.Type.valueOf(
                requestDto.getType().toUpperCase()))).thenReturn(paymentStrategy);
        Mockito.when(checkoutService.createCheckoutSession(expectedPrice))
                .thenReturn(expectedSession);
        Mockito.when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(payment);
        Mockito.when(paymentMapper.toDto(payment)).thenReturn(expected);

        PaymentDto actual = paymentService.createPayment(requestDto);

        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Given valid PaymentRequestDto. "
            + "Should change payments' statuses and update them")
    void checkAndUpdatePaymentStatus_ShouldChangeStatus()
            throws MalformedURLException {
        Car car = new Car()
                .setId(DEFAULT_ID_VALUE)
                .setBrand("BMW")
                .setModel("model")
                .setCarType(Car.CarType.SEDAN)
                .setDeleted(false)
                .setFeeUsd(BigDecimal.TEN)
                .setInventory(100);

        User user = new User()
                .setId(DEFAULT_ID_VALUE)
                .setRoles(Set.of(new Role(Role.RoleName.CUSTOMER)))
                .setEmail("email@test.com")
                .setDeleted(false)
                .setPassword("password")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setTgChatId(213123L);

        Rental rental = new Rental()
                .setCar(car)
                .setUser(user)
                .setReturnDate(LocalDate.now().plusDays(10))
                .setId(DEFAULT_ID_VALUE)
                .setRentalDate(LocalDate.now());

        PaymentStrategy paymentStrategy = new RegularPaymentStrategy();
        BigDecimal expectedPrice = paymentStrategy.calculatePayment(rental);

        Payment payment1 = new Payment()
                .setType(Payment.Type.PAYMENT)
                .setRental(rental)
                .setAmountToPay(expectedPrice)
                .setStatus(Payment.Status.PENDING)
                .setSessionId("sessionId1")
                .setSessionUrl(new URL("https://checkout.stripe.com/c/pay/cs_test1"))
                .setId(DEFAULT_ID_VALUE);

        Payment payment2 = new Payment()
                .setType(Payment.Type.PAYMENT)
                .setRental(rental)
                .setAmountToPay(expectedPrice)
                .setStatus(Payment.Status.PENDING)
                .setSessionId("sessionId2")
                .setSessionUrl(new URL("https://checkout.stripe.com/c/pay/cs_test2"))
                .setId(DEFAULT_ID_VALUE + 1);

        List<Payment> payments = List.of(payment1, payment2);

        Mockito.when(paymentRepository.findAllByStatus(Payment.Status.PENDING))
                .thenReturn(payments);

        Mockito.when(checkoutService.getPaymentStatus("sessionId1")).thenReturn("PAID");
        Mockito.when(checkoutService.getPaymentStatus("sessionId2")).thenReturn("EXPIRED");

        Mockito.when(paymentRepository.save(Mockito.any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        paymentService.checkAndUpdatePaymentStatus();

        assertEquals(Payment.Status.PAID, payment1.getStatus());
        assertEquals(Payment.Status.EXPIRED, payment2.getStatus());

        Mockito.verify(paymentRepository, Mockito.times(1)).findAllByStatus(Payment.Status.PENDING);
        Mockito.verify(checkoutService, Mockito.times(1)).getPaymentStatus("sessionId1");
        Mockito.verify(checkoutService, Mockito.times(1)).getPaymentStatus("sessionId2");
        Mockito.verify(paymentRepository, Mockito.times(1)).save(payment1);
        Mockito.verify(paymentRepository, Mockito.times(1)).save(payment2);
    }
}
