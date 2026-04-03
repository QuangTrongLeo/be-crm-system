package nlu.fit.crm_system.Service.Impl;

import lombok.RequiredArgsConstructor;
import nlu.fit.crm_system.DTO.request.LoginRequest;
import nlu.fit.crm_system.DTO.response.TokenResponse;
import nlu.fit.crm_system.Entities.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;

    // Đăng nhập
    public TokenResponse login(LoginRequest request) {
        User user = userService.findUserByEmail(request.getEmail());
        if (!request.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("Sai mật khẩu!");
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new TokenResponse(accessToken, refreshToken);
    }
}
