package car.sharing.app.carsharingservice.repository.payment;

import car.sharing.app.carsharingservice.model.Payment;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@ActiveProfiles("test")
class PaymentRepositoryTest {
    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    @Sql(scripts = {
            "classpath:database/clear-database.sql",
            "classpath:database/rental/02-insert-2-rentals.sql",
            "classpath:database/payment/create-5-payments.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findAllByUserId() {
        List<Payment> allByUserId = paymentRepository.findAllByUserId(2L);
        Assertions.assertEquals(2, allByUserId.size());
    }

    @Test
    @Sql(scripts = {
            "classpath:database/clear-database.sql",
            "classpath:database/user/02-insert-2-users.sql",
            "classpath:database/user/03-insert-roles.sql",
            "classpath:database/rental/02-insert-2-rentals.sql",
            "classpath:database/payment/create-5-payments.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findAllByStatus() {
        List<Payment> allByStatus = paymentRepository.findAllByStatus(Payment.Status.PAID);
        Assertions.assertEquals(1, allByStatus.size());
    }
}
