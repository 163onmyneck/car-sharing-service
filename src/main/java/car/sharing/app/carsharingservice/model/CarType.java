package car.sharing.app.carsharingservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "car_type")
@Getter
@Setter
public class CarType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CarTypeName carTypeName;

    public CarType() {
    }

    public CarType(CarTypeName carTypeName) {
        this.carTypeName = carTypeName;
    }

    public enum CarTypeName {
        SEDAN("sedan"),
        SUV("suv"),
        UNIVERSAL("universal"),
        HATCHBACK("hatchback");

        private final String name;

        CarTypeName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
