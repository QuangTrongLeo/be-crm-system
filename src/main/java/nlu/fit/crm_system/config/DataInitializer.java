package nlu.fit.crm_system.config;

import lombok.RequiredArgsConstructor;
import nlu.fit.crm_system.Entities.User;
import nlu.fit.crm_system.enums.Role;
import nlu.fit.crm_system.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepo userRepository;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail(adminEmail).isPresent()) {
            return;
        }

        User admin = User.builder()
                .username(adminUsername)
                .email(adminEmail)
                .password(adminPassword)
                .role(Role.ADMIN)
                .fullName("System Admin")
                .build();

        userRepository.save(admin);

        System.out.println(">>> ADMIN account created!");
    }
}