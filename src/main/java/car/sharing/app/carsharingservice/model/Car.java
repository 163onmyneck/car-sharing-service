package car.sharing.app.carsharingservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "cars")
@Getter
@Setter
@SQLDelete(sql = "UPDATE cars SET is_deleted = 1 WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
@ToString
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String model;
    @Column(nullable = false)
    private String brand;
    @OneToOne
    private CarType carType;
    @Column(nullable = false)
    private int inventory;
    @Column(nullable = false)
    private BigDecimal feeUsd;
    @Column(name = "is_deleted", columnDefinition = "TINYINT(1)", nullable = false)
    private boolean isDeleted = false;

    public void decreaseInventory() {
        inventory--;
    }

    public void increaseInventory() {
        inventory++;
    }
}
