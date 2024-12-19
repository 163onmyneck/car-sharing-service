package car.sharing.app.carsharingservice.repository.payment;

import car.sharing.app.carsharingservice.model.Payment;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PaymentRepositoryTest {
    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    @DisplayName("Find all by user with id 2, expected list size - 2")
    @Sql(scripts = {
            "classpath:database/clear-database.sql",
            "classpath:database/user/02-insert-2-users.sql",
            "classpath:database/rental/02-insert-2-rentals.sql",
            "classpath:database/payment/create-5-payments.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findAllByUserId() {
        List<Payment> allByUserId = paymentRepository.findAllByUserId(2L);
        Assertions.assertEquals(2, allByUserId.size());
    }

    @Test
    @DisplayName("Find all by Payment.Status.PAID, expecting 1 payment")
    @Sql(scripts = {
            "classpath:database/clear-database.sql",
            "classpath:database/user/02-insert-2-users.sql",
            "classpath:database/rental/02-insert-2-rentals.sql",
            "classpath:database/payment/create-5-payments.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findAllByStatus() {
        List<Payment> allByStatus = paymentRepository.findAllByStatus(Payment.Status.PAID);
        Assertions.assertEquals(1, allByStatus.size());
    }
}
