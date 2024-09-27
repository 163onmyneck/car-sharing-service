package car.sharing.app.carsharingservice.repository.car;

import car.sharing.app.carsharingservice.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
}
