package car.sharing.app.carsharingservice.repository.rental;

import car.sharing.app.carsharingservice.model.Rental;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long>,
        JpaSpecificationExecutor<Rental> {
    @Query("FROM Rental r LEFT JOIN FETCH r.car WHERE r.user.id = :id "
            + "AND r.isDeleted = false")
    List<Rental> getAllRentalsByUserIdFetchCars(@Param("id") Long userId);
}
