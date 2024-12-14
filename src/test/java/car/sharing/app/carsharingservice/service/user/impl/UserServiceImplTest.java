package car.sharing.app.carsharingservice.service.user.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import car.sharing.app.carsharingservice.dto.user.UserDto;
import car.sharing.app.carsharingservice.dto.user.UserRegistrationRequestDto;
import car.sharing.app.carsharingservice.exception.EntityNotFoundException;
import car.sharing.app.carsharingservice.exception.RegistrationException;
import car.sharing.app.carsharingservice.mapper.UserMapper;
import car.sharing.app.carsharingservice.model.Role;
import car.sharing.app.carsharingservice.model.User;
import car.sharing.app.carsharingservice.repository.role.RoleRepository;
import car.sharing.app.carsharingservice.repository.user.UserRepository;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Given valid register user dto. Should return user dto")
    void register_NewUserWithValidCredentials_ShouldReturnItsUserDto()
            throws RegistrationException {
        User user = new User()
                .setId(1L)
                .setRoles(Set.of(new Role(Role.RoleName.CUSTOMER)))
                .setEmail("email@test.com")
                .setDeleted(false)
                .setPassword("password")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setTgChatId(213123L);

        UserDto expectedUserDto = new UserDto()
                .setEmail(user.getEmail())
                .setId(user.getId())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName());

        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto()
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setRepeatPassword(user.getPassword());

        Mockito.when(userRepository.findByEmail(expectedUserDto.getEmail()))
                .thenReturn(Optional.empty());
        Mockito.when(userMapper.toModel(requestDto)).thenReturn(user);
        Mockito.when(passwordEncoder.encode(user.getPassword()))
                .thenReturn(user.getPassword() + "encoded");
        Mockito.when(roleRepository.findRoleByRoleName(Role.RoleName.CUSTOMER))
                .thenReturn(Optional.of(new Role(Role.RoleName.CUSTOMER)));
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userMapper.toDto(user)).thenReturn(expectedUserDto);

        UserDto registeredUser = userService.register(requestDto);

        String expectedUserEmail = user.getEmail();
        String actualUserEmail = registeredUser.getEmail();

        assertThat(expectedUserEmail).isEqualTo(actualUserEmail);
    }

    @Test
    @DisplayName("Given existing user. Should throw RegistrationException")
    void register_ExistingUser_ShouldThrowRegistrationException() {
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto()
                .setEmail("email@test.com")
                .setPassword("password")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setRepeatPassword("password");

        Mockito.when(userRepository.findByEmail(requestDto.getEmail()))
                .thenReturn(Optional.of(new User()));

        assertThrows(RegistrationException.class, () -> userService.register(requestDto));
    }

    @Test
    @DisplayName("Given userDto with different passwords. Should throw IllegalArgumentException")
    void register_UserWithInvalidCredentials_ShouldThrowException() {
        User user = new User()
                .setId(1L)
                .setRoles(Set.of(new Role(Role.RoleName.CUSTOMER)))
                .setEmail("email@test.com")
                .setDeleted(false)
                .setPassword("password")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setTgChatId(213123L);

        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto()
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setRepeatPassword(user.getPassword() + "mistake");

        assertThrows(IllegalArgumentException.class, () -> userService.register(requestDto));
    }

    @Test
    @DisplayName("Given invalid user id. Should throw EntityNotFoundException")
    void updateRole_InvalidUserId_ShouldThrowEntityNotFoundException() {
        Mockito.when(userRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService
                .updateRole(100L, Role.RoleName.MANAGER.name()));
    }

    @Test
    @DisplayName("Given invalid role name. Should throw EntityNotFoundException")
    void updateRole_InvalidRoleName_ShouldThrowException() {
        long userId = 1L;
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        Mockito.when(roleRepository.findRoleByRoleName(Role.RoleName.CUSTOMER))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> userService.updateRole(userId, "customer"));
    }

    @Test
    @DisplayName("Given valid roleName and user's id. "
            + "Should update user's role and return it's dto")
    void updateRole_ValidDtoRequest_ShouldReturnItsUserDto() {
        User user = new User()
                .setId(1L)
                .setRoles(Set.of(new Role(Role.RoleName.CUSTOMER)))
                .setEmail("email@test.com")
                .setDeleted(false)
                .setPassword("password")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setTgChatId(213123L);

        String roleName = "manager";

        UserDto expected = new UserDto()
                .setEmail(user.getEmail())
                .setId(user.getId())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setRole(roleName.toUpperCase());

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(roleRepository.findRoleByRoleName(
                Role.RoleName.valueOf(roleName.toUpperCase())))
                .thenReturn(Optional.of(new Role(Role.RoleName.MANAGER)));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(userMapper.toDto(user)).thenReturn(expected);

        UserDto actual = userService.updateRole(1L, roleName);

        Assertions.assertEquals(actual.getRole(), expected.getRole());

    }

    @Test
    @DisplayName("Given valid user id. Should return user's dto")
    void getCurrentProfileInfo_CorrectId_ShouldReturnValidUserProfile() {
        User user = new User()
                .setId(1L)
                .setRoles(Set.of(new Role(Role.RoleName.CUSTOMER)))
                .setEmail("email@test.com")
                .setDeleted(false)
                .setPassword("password")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setTgChatId(213123L);

        UserDto expectedProfileInfo = new UserDto()
                .setEmail(user.getEmail())
                .setId(user.getId())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName());

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(userMapper.toDto(user)).thenReturn(expectedProfileInfo);

        UserDto actualProfileInfo = userService.getCurrentProfileInfo(user.getId());

        assertThat(expectedProfileInfo).isEqualTo(actualProfileInfo);
    }

    @Test
    @DisplayName("Given valid dtoRequest. Should update user and return it's dto")
    void updateProfileData_ValidDataForUpdate_ShouldReturnValidDto() {
        User user = new User()
                .setId(1L)
                .setRoles(new HashSet<>(Set.of(new Role(Role.RoleName.CUSTOMER))))
                .setEmail("email@test.com")
                .setDeleted(false)
                .setPassword("password")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setTgChatId(213123L);

        User updatedUser = new User()
                .setId(1L)
                .setRoles(Set.of(new Role(Role.RoleName.CUSTOMER)))
                .setEmail("email1@test.com")
                .setDeleted(false)
                .setPassword("passwor1d1131df")
                .setFirstName("firstNam13rfwede")
                .setLastName("lastNam1wdfsdsfe")
                .setTgChatId(213123213L);

        UserDto expectedProfileData = new UserDto()
                .setEmail(updatedUser.getEmail())
                .setId(updatedUser.getId())
                .setFirstName(updatedUser.getFirstName())
                .setLastName(updatedUser.getLastName());

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user)).thenReturn(updatedUser);
        Mockito.when(userMapper.toDto(updatedUser)).thenReturn(expectedProfileData);

        UserDto actualProfileData = userService
                .updateProfileData(user.getId(), expectedProfileData);

        assertThat(actualProfileData).isEqualTo(expectedProfileData);
    }
}
