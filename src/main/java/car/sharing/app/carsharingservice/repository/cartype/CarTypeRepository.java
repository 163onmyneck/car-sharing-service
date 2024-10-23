package car.sharing.app.carsharingservice.repository.cartype;

import car.sharing.app.carsharingservice.model.CarType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarTypeRepository extends JpaRepository<CarType, Long> {
    Optional<CarType> findByCarTypeName(CarType.CarTypeName carTypeName);
}
