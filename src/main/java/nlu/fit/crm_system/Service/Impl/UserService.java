package nlu.fit.crm_system.Service.Impl;

import lombok.RequiredArgsConstructor;
import nlu.fit.crm_system.Entities.User;
import nlu.fit.crm_system.Repositories.UserRepository;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public User findUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại!"));
    }
}
