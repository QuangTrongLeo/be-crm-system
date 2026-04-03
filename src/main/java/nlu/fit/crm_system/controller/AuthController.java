package nlu.fit.crm_system.Controller;

import lombok.RequiredArgsConstructor;
import nlu.fit.crm_system.DTO.request.LoginRequest;
import nlu.fit.crm_system.DTO.response.ApiResponse;
import nlu.fit.crm_system.DTO.response.TokenResponse;
import nlu.fit.crm_system.Service.Impl.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.crm-system-url}/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    // ===== LOGIN =====
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody LoginRequest request) {
        TokenResponse tokenResponse = authService.login(request);
        return ResponseEntity.ok(
                ApiResponse.<TokenResponse>builder()
                        .success(true)
                        .message("Đăng nhập thành công")
                        .data(tokenResponse)
                        .build()
        );
    }
}
