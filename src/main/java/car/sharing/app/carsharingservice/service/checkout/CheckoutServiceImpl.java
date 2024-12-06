package car.sharing.app.carsharingservice.service.checkout;

import car.sharing.app.carsharingservice.exception.PaymentSessionException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutServiceImpl.class);
    private static final String CURRENCY = "usd";
    private static final String DEFAULT_PRODUCT_NAME = "Послуга або продукт";
    private static final Long DEFAULT_QUANTITY = 1L;
    @Value("${CANCEL_URL}")
    private String cancelUrl;
    @Value("${SUCCESS_URL}")
    private String successUrl;
    @Value("${STRIPE_SECRET_KEY}")
    private String apiKey;

    @PostConstruct
    private void setApiKey() {
        Stripe.apiKey = apiKey;
    }

    public Session createCheckoutSession(BigDecimal amount) {
        long amountToCents = convertToCents(amount);

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl).setCancelUrl(cancelUrl)
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(DEFAULT_QUANTITY).setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency(CURRENCY).setUnitAmount(amountToCents)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData
                                        .builder().setName(DEFAULT_PRODUCT_NAME)
                                        .build()).build()).build()).build();
        try {
            return Session.create(params);
        } catch (StripeException e) {
            logger.error("Failed to create checkout session. ");
            throw new PaymentSessionException("Could not create payment session");
        }
    }

    @Override
    public String getPaymentStatus(String sessionId) {
        Session session = null;
        try {
            session = Session.retrieve(sessionId);
        } catch (StripeException e) {
            logger.error("Failed to retrieve session with id: {}", sessionId);
            throw new RuntimeException("Can not retrieve session with id: " + sessionId, e);
        }
        return session.getPaymentStatus();
    }

    private long convertToCents(BigDecimal amount) {
        return Math.round(amount.doubleValue() * 100);
    }
}
