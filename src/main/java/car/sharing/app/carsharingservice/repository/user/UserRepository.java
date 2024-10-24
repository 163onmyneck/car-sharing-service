package car.sharing.app.carsharingservice.repository.user;

import car.sharing.app.carsharingservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
