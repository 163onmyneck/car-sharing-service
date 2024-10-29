package car.sharing.app.carsharingservice.controller;

import car.sharing.app.carsharingservice.dto.rental.RentalDto;
import car.sharing.app.carsharingservice.service.rental.RentalService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/rentals")
@RequiredArgsConstructor
public class RentalController {
    private final RentalService rentalService;

    @PostMapping
    public RentalDto createRental(@RequestBody RentalDto rentalDto) {
        return rentalService.createRental(rentalDto);
    }

    @GetMapping
    public List<RentalDto> getRentals(@RequestParam Long userId) {
        return rentalService.getAllRentalsByUserId(userId);
    }

    @PostMapping("/{id}/return")
    public void updateRental(@PathVariable Long id) {
        rentalService.returnRental(id);
    }
}
