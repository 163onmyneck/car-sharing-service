package car.sharing.app.carsharingservice.controller;

import car.sharing.app.carsharingservice.dto.rental.RentalRequestDto;
import car.sharing.app.carsharingservice.dto.rental.RentalResponseDto;
import car.sharing.app.carsharingservice.dto.rental.RentalSearchParams;
import car.sharing.app.carsharingservice.model.User;
import car.sharing.app.carsharingservice.service.rental.RentalService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
public class RentalController {
    private final RentalService rentalService;

    @PostMapping("/create-rental")
    public RentalResponseDto createRental(Authentication authentication,
                                          @RequestBody RentalRequestDto rentalRequestDto) {
        User user = (User) authentication.getPrincipal();
        rentalRequestDto.setUserId(user.getId());
        return rentalService.createRental(rentalRequestDto);
    }

    @GetMapping
    public List<RentalResponseDto> getRentals(Authentication authentication) {
        User user = (User) authentication.getCredentials();
        return rentalService.getAllRentalsByUserId(user.getId());
    }

    @PostMapping("/{id}/return")
    public RentalResponseDto returnRental(@PathVariable Long id) {
        return rentalService.returnRental(id);
    }

    @GetMapping("/search")
    public List<RentalResponseDto> getSpecificRental(Authentication authentication,
                                                     @RequestBody RentalSearchParams
                                                             rentalSearchParams) {
        User user = (User) authentication.getCredentials();
        rentalSearchParams.setUserId(user.getId());
        return rentalService.getSpecificRental(rentalSearchParams);
    }
}
