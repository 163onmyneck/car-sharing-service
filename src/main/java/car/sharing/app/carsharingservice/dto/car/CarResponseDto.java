package car.sharing.app.carsharingservice.dto.car;

import car.sharing.app.carsharingservice.model.CarType;
import java.math.BigDecimal;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CarResponseDto {
    private Long id;
    private String model;
    private String brand;
    private CarType carType;
    private int inventory;
    private BigDecimal feeUsd;
}
