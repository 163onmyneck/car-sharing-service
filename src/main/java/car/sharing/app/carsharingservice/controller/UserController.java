package car.sharing.app.carsharingservice.controller;

import car.sharing.app.carsharingservice.dto.user.UserDto;
import car.sharing.app.carsharingservice.model.User;
import car.sharing.app.carsharingservice.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('MANAGER')")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateRole(@PathVariable Long id,
                           @RequestParam String role) {
        return userService.updateRole(id, role);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('MANAGER', 'CUSTOMER')")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getMyProfile(@Autowired Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return userService.getCurrentProfileInfo(user.getId());
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'CUSTOMER')")
    @PutMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateProfile(@Autowired Authentication authentication,
                                         @RequestBody UserDto userProfileData) {
        User user = (User) authentication.getPrincipal();
        return userService.updateProfileData(user.getId(), userProfileData);
    }
}
