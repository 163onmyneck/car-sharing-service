package car.sharing.app.carsharingservice.repository.rental;

import car.sharing.app.carsharingservice.model.Rental;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@DataJpaTest
@ActiveProfiles("test")
class RentalRepositoryTest {
    @Autowired
    private RentalRepository rentalRepository;

    @Test
    @Sql(scripts = {
            "classpath:database/clear-database.sql",
            "classpath:database/user/02-insert-2-users.sql",
            "classpath:database/rental/02-insert-2-rentals.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getAllRentalsByUserId() {
        int expectedSize = 2;
        List<Rental> actual = rentalRepository.getAllRentalsByUserIdFetchCars(2L);

        Assertions.assertEquals(expectedSize, actual.size());
    }
}
