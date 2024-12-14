package car.sharing.app.carsharingservice.service.checkout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import car.sharing.app.carsharingservice.exception.PaymentSessionException;
import com.stripe.exception.AuthenticationException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceImplTest {
    @InjectMocks
    private CheckoutServiceImpl checkoutService;

    @Mock
    private Session sessionMock;

    @Test
    @DisplayName("Create session, should return session")
    void createCheckoutSession_ShouldCreateSession() {
        try (MockedStatic<Session> mockedSession = Mockito.mockStatic(Session.class)) {
            mockedSession.when(() -> Session.create(Mockito.any(
                    SessionCreateParams.class))).thenReturn(sessionMock);

            BigDecimal amount = BigDecimal.valueOf(100);
            Session result = checkoutService.createCheckoutSession(amount);

            assertNotNull(result);
            mockedSession.verify(
                    () -> Session.create(Mockito.any(SessionCreateParams.class)),
                    Mockito.times(1));
        }
    }

    @Test
    @DisplayName("Can not create checkout session, exception was thrown")
    void createCheckoutSession_ShouldThrowPaymentSessionException_WhenStripeException() {
        try (MockedStatic<Session> mockedSession = Mockito.mockStatic(Session.class)) {
            AuthenticationException mockException = new AuthenticationException(
                    "Test exception", null, null, 400, null);
            mockedSession.when(() -> Session.create(Mockito.any(
                    SessionCreateParams.class))).thenThrow(mockException);

            assertThrows(PaymentSessionException.class,
                    () -> checkoutService.createCheckoutSession(BigDecimal.TEN));
        }
    }

    @Test
    @DisplayName("get payment status from session, expecting paid status")
    void getPaymentStatus_ShouldReturnStatus() {
        String sessionId = "session123";
        try (MockedStatic<Session> sessionMockedStatic = Mockito.mockStatic(Session.class)) {
            sessionMockedStatic.when(() -> Session.retrieve(Mockito.anyString()))
                    .thenReturn(sessionMock);
            sessionMockedStatic.when(() -> checkoutService.getPaymentStatus(sessionId))
                    .thenReturn("paid");
            String status = checkoutService.getPaymentStatus(sessionId);

            assertEquals("paid", status);
        }
    }

    @Test
    @DisplayName("get payment status from session, session is null, should throw exception")
    void getPaymentStatus_ShouldThrowRuntimeException_WhenStripeException() {
        String sessionId = "session123";
        try (MockedStatic<Session> sessionMockedStatic = Mockito.mockStatic(Session.class)) {
            AuthenticationException mockException = new AuthenticationException(
                    "Test exception", null, null, 400, null);
            sessionMockedStatic.when(() -> Session.retrieve(
                    Mockito.anyString())).thenThrow(mockException);
            assertThrows(RuntimeException.class,
                    () -> checkoutService.getPaymentStatus(sessionId));
        }
    }
}
