package nlu.fit.crm_system.mapper;

import nlu.fit.crm_system.Entities.User;
import nlu.fit.crm_system.DTO.response.UserResponse;

public class UserMapper {
    public static UserResponse toUserResponse(User user) {
        if (user == null) return null;

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole() != null ? user.getRole().name() : null)
                .build();
    }
}
