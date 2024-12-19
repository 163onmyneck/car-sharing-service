package car.sharing.app.carsharingservice.repository.user;

import car.sharing.app.carsharingservice.model.Role;
import car.sharing.app.carsharingservice.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("from User u LEFT JOIN FETCH u.roles WHERE u.email =:email")
    Optional<User> findByEmailFetchRoles(@Param("email") String email);

    @Query("FROM User u JOIN FETCH u.roles r "
            + "WHERE r.roleName = :roleName AND u.tgChatId IS NOT NULL")
    List<User> getAllByRoleFetchRoles(@Param("roleName") Role.RoleName roleName);

    @Query("FROM User WHERE tgChatId = :tgChatId")
    Optional<User> findByTgChatId(@Param("tgChatId") Long tgChatId);
}
