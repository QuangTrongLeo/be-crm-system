package nlu.fit.crm_system.service;

import lombok.RequiredArgsConstructor;
import nlu.fit.crm_system.dto.request.LoginRequest;
import nlu.fit.crm_system.dto.response.TokenResponse;
import nlu.fit.crm_system.entity.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;

    // Đăng nhập
    public TokenResponse login(LoginRequest request) {
        User user = userService.findUserByEmail(request.getEmail());

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new TokenResponse(accessToken, refreshToken);
    }
}
