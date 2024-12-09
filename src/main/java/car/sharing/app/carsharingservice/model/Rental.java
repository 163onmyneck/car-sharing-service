package car.sharing.app.carsharingservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Accessors(chain = true)
@Entity
@Table(name = "rentals")
@Getter
@Setter
@SQLDelete(sql = "UPDATE rentals SET is_deleted = 1 WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
@ToString
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDate rentalDate;
    @Column(nullable = false)
    private LocalDate returnDate;
    private LocalDate actualReturnDate;
    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "is_deleted", columnDefinition = "TINYINT(1)", nullable = false)
    private boolean isDeleted = false;
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isActive = true;
}
