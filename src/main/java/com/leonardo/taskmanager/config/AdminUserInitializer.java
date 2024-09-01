package com.leonardo.taskmanager.config;

import com.leonardo.taskmanager.entity.User;
import com.leonardo.taskmanager.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class AdminUserInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //Create admin user when building app
    @Bean
    public CommandLineRunner createAdminUser() {
        return args -> {
            if (!userRepository.existsByRole(User.Role.ADMIN)) {
                User adminUser = new User();
                adminUser.setUsername("admin");
                adminUser.setEmail("admin@gmail.com");
                adminUser.setCreatedBy("system");
                adminUser.setLastModifiedBy(LocalDateTime.now().toString());
                adminUser.setPassword(passwordEncoder.encode("12345"));
                adminUser.setRole(User.Role.ADMIN);

                userRepository.save(adminUser);
                log.info("Admin user was created");
            } else {
                log.info("Admin user already exists.");
            }
        };
    }
}
