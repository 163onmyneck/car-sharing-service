package car.sharing.app.carsharingservice.controller;

import car.sharing.app.carsharingservice.dto.user.UserDto;
import car.sharing.app.carsharingservice.model.User;
import car.sharing.app.carsharingservice.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("/{id}/role")
    public void updateRole(@PathVariable Long id,
                           @RequestParam String role) {
        userService.updateRole(id, role);
    }

    @GetMapping("/me")
    public UserDto getMyProfile(Authentication authentication) {
        User user = (User) authentication.getCredentials();
        return userService.getCurrentProfileInfo(user.getId());
    }

    @PutMapping("/me")
    public UserDto updateProfile(Authentication authentication,
                                         @RequestBody UserDto userProfileData) {
        User user = (User) authentication.getCredentials();
        userProfileData.setId(user.getId());
        return userService.updateProfileData(user.getId(), userProfileData);
    }
}
