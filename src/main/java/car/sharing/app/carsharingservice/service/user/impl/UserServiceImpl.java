package car.sharing.app.carsharingservice.service.user.impl;

import car.sharing.app.carsharingservice.dto.user.UserRequestDto;
import car.sharing.app.carsharingservice.dto.user.UserResponseDto;
import car.sharing.app.carsharingservice.mapper.UserMapper;
import car.sharing.app.carsharingservice.model.Role;
import car.sharing.app.carsharingservice.model.User;
import car.sharing.app.carsharingservice.repository.role.RoleRepository;
import car.sharing.app.carsharingservice.repository.user.UserRepository;
import car.sharing.app.carsharingservice.service.user.UserService;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void updateRole(Long id, String role) {
        User user = userRepository.findById(id).orElseThrow(RuntimeException::new);
        Role roleFromBd = roleRepository.findByRoleName(Role.RoleName.valueOf(role.toUpperCase()));
        Set<Role> updatedRoles = new HashSet<>(user.getRole());
        updatedRoles.clear();
        updatedRoles.add(roleFromBd);
        user.setRole(updatedRoles);
        userRepository.save(user);
    }

    @Override
    public UserResponseDto getCurrentProfileInfo(Long id) {
        return userMapper.toDto(userRepository.findById(id).orElseThrow(RuntimeException::new));
    }

    @Override
    public UserResponseDto updateProfileData(Long id, UserRequestDto dto) {
        User user = userRepository.findById(id).orElseThrow(RuntimeException::new);
        user.setEmail(dto.getEmail() == null ? user.getEmail() : dto.getEmail());
        user.setFirstName(dto.getFirstName() == null ? user.getFirstName() : dto.getFirstName());
        user.setLastName(dto.getLastName() == null ? user.getLastName() : dto.getLastName());
        return userMapper.toDto(userRepository.save(user));
    }
}
