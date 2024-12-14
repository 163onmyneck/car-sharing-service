package car.sharing.app.carsharingservice.controller;

import car.sharing.app.carsharingservice.dto.car.CarRequestDto;
import car.sharing.app.carsharingservice.dto.car.CarResponseDto;
import car.sharing.app.carsharingservice.service.car.CarService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarResponseDto create(@RequestBody CarRequestDto carRequestDto) {
        return carService.save(carRequestDto);
    }

    @PreAuthorize("hasAnyRole({'MANAGER', 'CUSTOMER'})")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CarResponseDto> getAllCars() {
        return carService.getAll();
    }

    @PreAuthorize("hasAnyRole({'MANAGER', 'CUSTOMER'})")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CarResponseDto getCarById(@PathVariable Long id) {
        return carService.getInfo(id);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CarResponseDto update(@PathVariable Long id, @RequestBody CarRequestDto carRequestDto) {
        return carService.update(id, carRequestDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        carService.deleteById(id);
    }
}
