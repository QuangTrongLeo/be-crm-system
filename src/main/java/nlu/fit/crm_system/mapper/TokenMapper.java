package nlu.fit.crm_system.mapper;


import nlu.fit.crm_system.DTO.response.TokenResponse;
import nlu.fit.crm_system.DTO.response.UserResponse;

public class TokenMapper {

    public static TokenResponse toTokenResponse(
            String accessToken,
            String refreshToken,
            UserResponse userResponse
    ) {
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userResponse)
                .build();
    }
}
