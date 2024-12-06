package car.sharing.app.carsharingservice.repository.rental;

import car.sharing.app.carsharingservice.model.Rental;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long>,
        JpaSpecificationExecutor<Rental> {
    @Query("FROM Rental r WHERE r.user.id = :id AND r.isDeleted = false")
    List<Rental> getAllRentalsByUserId(@Param("id") Long userId);

    @Query("FROM Rental r WHERE r.isDeleted = false "
            + "AND r.isActive = true AND r.id = :id")
    Optional<Rental> findById(@Value("id") Long id);

    @Query("FROM Rental r WHERE r.isDeleted = false "
            + "AND r.isActive = false AND r.id = :id")
    Optional<Rental> findByIdNotActive(@Value("id") Long id);
}
