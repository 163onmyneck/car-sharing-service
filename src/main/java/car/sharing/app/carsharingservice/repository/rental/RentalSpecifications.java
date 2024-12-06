package car.sharing.app.carsharingservice.repository.rental;

import car.sharing.app.carsharingservice.model.Rental;
import java.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;

public class RentalSpecifications {
    public static Specification<Rental> byUserId(Long userId) {
        return (root, query, criteriaBuilder) ->
                userId == null ? null : criteriaBuilder.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Rental> byCarId(Long carId) {
        return (root, query, criteriaBuilder) ->
                carId == null ? null : criteriaBuilder.equal(root.get("car").get("id"), carId);
    }

    public static Specification<Rental> byIsActive(Boolean isActive) {
        return (root, query, criteriaBuilder) ->
                isActive == null ? null : criteriaBuilder.equal(root.get("isActive"), isActive);
    }

    public static Specification<Rental> byDateRange(LocalDate firstDate, LocalDate secondDate) {
        return (root, query, criteriaBuilder) -> {
            if (firstDate != null && secondDate != null) {
                return criteriaBuilder.between(root.get("rentalDate"), firstDate, secondDate);
            }
            return null;
        };
    }
}
