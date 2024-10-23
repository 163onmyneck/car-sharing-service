package car.sharing.app.carsharingservice.controller;

import car.sharing.app.carsharingservice.dto.user.UserRequestDto;
import car.sharing.app.carsharingservice.dto.user.UserResponseDto;
import car.sharing.app.carsharingservice.service.user.UserService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
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
                           @RequestBody Map<String, String> role) {
        String roleName = role.get("role");
        userService.updateRole(id, roleName);
    }

    @GetMapping("/me")
    public UserResponseDto getMyProfile(@RequestParam Long userId) {
        return userService.getCurrentProfileInfo(userId);
    }

    @PutMapping("/me")
    public UserResponseDto updateProfile(@RequestParam Long id,
                                         @RequestBody UserRequestDto userRequestDto) {
        return userService.updateProfileData(id, userRequestDto);
    }
}
