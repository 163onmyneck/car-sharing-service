package car.sharing.app.carsharingservice.repository.user;

import car.sharing.app.carsharingservice.model.Role;
import car.sharing.app.carsharingservice.model.User;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("find by email, should return 1 user")
    @Sql(scripts = {
            "classpath:database/clear-database.sql",
            "classpath:database/user/01-create-roles-users-tables.sql",
            "classpath:database/user/02-insert-2-users.sql",
            "classpath:database/user/03-insert-roles.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findByEmail() {
        String expectedEmail = "test1@gmail.com";
        String actualEmail = userRepository.findByEmail(expectedEmail).get().getEmail();

        Assertions.assertEquals(actualEmail, expectedEmail);
    }

    @Test
    @DisplayName("get all by role, should return 2 users")
    @Sql(scripts = {
            "classpath:database/clear-database.sql",
            "classpath:database/user/01-create-roles-users-tables.sql",
            "classpath:database/user/02-insert-2-users.sql",
            "classpath:database/user/03-insert-roles.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getAllByRole() {
        int expectedCustomerListSize = 2;
        int expectedManagerListSize = 1;

        List<User> customers = userRepository.getAllByRole(Role.RoleName.CUSTOMER);
        List<User> managers = userRepository.getAllByRole(Role.RoleName.MANAGER);

        int actualCustomerListSize = customers.size();
        int actualManagerListSize = managers.size();

        Assertions.assertEquals(expectedCustomerListSize, actualCustomerListSize);
        Assertions.assertEquals(expectedManagerListSize, actualManagerListSize);
    }

    @Test
    @DisplayName("find by tg chat id,expecting user from db")
    @Sql(scripts = {
            "classpath:database/clear-database.sql",
            "classpath:database/user/01-create-roles-users-tables.sql",
            "classpath:database/user/02-insert-2-users.sql",
            "classpath:database/user/03-insert-roles.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findByTgChatId() {
        Long tgChatId = 343241L;

        User actual = userRepository.findByTgChatId(tgChatId).get();

        Assertions.assertEquals(tgChatId, actual.getTgChatId());
    }
}
