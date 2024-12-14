package car.sharing.app.carsharingservice.repository.payment;

import car.sharing.app.carsharingservice.model.Payment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("FROM Payment p WHERE p.rental.user.id = :id")
    List<Payment> findAllByUserId(@Param("id") Long userId);

    List<Payment> findAllByStatus(Payment.Status status);
}
