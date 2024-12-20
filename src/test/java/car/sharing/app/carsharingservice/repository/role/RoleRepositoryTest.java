package car.sharing.app.carsharingservice.repository.role;

import static org.junit.jupiter.api.Assertions.assertEquals;

import car.sharing.app.carsharingservice.model.Role;
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
class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("find role by CUSTOMER role name, expecting Role.RoleName.CUSTOMER")
    @Sql(scripts = {
            "classpath:database/clear-database.sql",
            "classpath:database/user/01-insert-roles.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findRoleByRoleName() {
        Role.RoleName actualRoleName = roleRepository.findRoleByRoleName(
                Role.RoleName.CUSTOMER).get().getRoleName();

        assertEquals(actualRoleName, Role.RoleName.CUSTOMER);
    }
}
