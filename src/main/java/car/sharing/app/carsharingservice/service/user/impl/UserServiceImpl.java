package car.sharing.app.carsharingservice.service.user.impl;

import car.sharing.app.carsharingservice.dto.user.UserDto;
import car.sharing.app.carsharingservice.dto.user.UserRegistrationRequestDto;
import car.sharing.app.carsharingservice.exception.EntityNotFoundException;
import car.sharing.app.carsharingservice.exception.RegistrationException;
import car.sharing.app.carsharingservice.mapper.UserMapper;
import car.sharing.app.carsharingservice.model.Role;
import car.sharing.app.carsharingservice.model.User;
import car.sharing.app.carsharingservice.repository.role.RoleRepository;
import car.sharing.app.carsharingservice.repository.user.UserRepository;
import car.sharing.app.carsharingservice.service.user.UserService;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private static final String DEFAULT_ROLE = "customer";
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto register(UserRegistrationRequestDto userRequestDto)
            throws RegistrationException {
        if (!userRequestDto.getPassword().equals(userRequestDto.getRepeatPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        if (userRepository.findByEmailFetchRoles(userRequestDto.getEmail()).isEmpty()) {
            User user = userMapper.toModelFromRegisterForm(userRequestDto);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(new HashSet<>(Set.of(getRoleByRoleName(DEFAULT_ROLE))));
            return userMapper.toDto(userRepository.save(user));
        }
        throw new RegistrationException("Can not register user with email: "
                + userRequestDto.getEmail() + "\n User already exists");
    }

    @Override
    public UserDto updateRole(Long id, String role) {
        User user = getUserById(id);
        Role roleFromBd = getRoleByRoleName(role);
        Set<Role> roles = user.getRoles();
        Set<Role> newRoles = new HashSet<>(roles);
        newRoles.add(roleFromBd);
        user.setRoles(newRoles);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto getCurrentProfileInfo(Long id) {
        return userMapper.toDto(getUserById(id));
    }

    @Override
    public UserDto updateProfileData(Long id, UserDto dto) {
        User model = getUserById(id);
        User proxy = userMapper.toModel(dto);
        return userMapper.toDto(userRepository.save(userMapper.updateUser(model, proxy)));
    }

    private User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cannot find user with id " + id));
    }

    private Role getRoleByRoleName(String roleName) {
        return roleRepository
                .findRoleByRoleName(Role.RoleName.valueOf(
                        roleName.toUpperCase())).orElseThrow(
                                () -> new EntityNotFoundException("Cannot find role with name: "
                                                                                + roleName));
    }
}
