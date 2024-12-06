package car.sharing.app.carsharingservice.security;

import car.sharing.app.carsharingservice.dto.user.UserLoginRequestDto;
import car.sharing.app.carsharingservice.dto.user.UserLoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserLoginResponseDto authenticate(UserLoginRequestDto dto) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

        String token = jwtUtil.generateToken(authentication.getName());
        return new UserLoginResponseDto(token);
    }
}
