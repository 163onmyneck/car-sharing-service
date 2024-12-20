package car.sharing.app.carsharingservice.repository.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import car.sharing.app.carsharingservice.model.Role;
import car.sharing.app.carsharingservice.model.User;
import java.util.List;
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
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("find by email, should return 1 user")
    @Sql(scripts = {
            "classpath:database/clear-database.sql",
            "classpath:database/user/01-insert-roles.sql",
            "classpath:database/user/02-insert-2-users.sql",
            "classpath:database/user/03-fill-users-roles.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findByEmailFetchRoles() {
        String expectedEmail = "test1@gmail.com";
        String actualEmail = userRepository.findByEmailFetchRoles(expectedEmail).get().getEmail();

        assertEquals(actualEmail, expectedEmail);
    }

    @Test
    @DisplayName("get all by role, should return 2 users")
    @Sql(scripts = {
            "classpath:database/clear-database.sql",
            "classpath:database/user/01-insert-roles.sql",
            "classpath:database/user/02-insert-2-users.sql",
            "classpath:database/user/03-fill-users-roles.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getAllByRoleFetchRoles() {
        int expectedCustomerListSize = 2;
        int expectedManagerListSize = 1;

        List<User> customers = userRepository.getAllByRoleFetchRoles(Role.RoleName.CUSTOMER);
        List<User> managers = userRepository.getAllByRoleFetchRoles(Role.RoleName.MANAGER);

        int actualCustomerListSize = customers.size();
        int actualManagerListSize = managers.size();

        assertEquals(expectedCustomerListSize, actualCustomerListSize);
        assertEquals(expectedManagerListSize, actualManagerListSize);
    }

    @Test
    @DisplayName("find by tg chat id,expecting user from db")
    @Sql(scripts = {
            "classpath:database/clear-database.sql",
            "classpath:database/user/01-insert-roles.sql",
            "classpath:database/user/02-insert-2-users.sql",
            "classpath:database/user/03-fill-users-roles.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findByTgChatId() {
        Long tgChatId = 343241L;

        User actual = userRepository.findByTgChatId(tgChatId).get();

        assertEquals(tgChatId, actual.getTgChatId());
    }
}
