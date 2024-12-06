package car.sharing.app.carsharingservice.mapper;

import car.sharing.app.carsharingservice.config.MapperConfig;
import car.sharing.app.carsharingservice.dto.payment.PaymentDto;
import car.sharing.app.carsharingservice.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface PaymentMapper {
    @Mapping(target = "rentalId", source = "rental.id")
    PaymentDto toDto(Payment payment);
}
