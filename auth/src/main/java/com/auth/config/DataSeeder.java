package com.auth.config;

import com.auth.entities.CustomUserDetails;
import com.auth.repositories.UserRepository;
import com.auth.utils.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class DataSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${seed.admin.username:admin}")
    private String adminUsername;

    @Value("${seed.admin.email:admin@example.com}")
    private String adminEmail;

    @Value("${seed.admin.password:Admin123#}")
    private String adminPassword;

    @Value("${seed.user.username:user}")
    private String userUsername;

    @Value("${seed.user.email:user@example.com}")
    private String userEmail;

    @Value("${seed.user.password:User123#}")
    private String userPassword;

    @Value("${seed.user.name:Test}")
    private String defaultName;

    @Value("${seed.user.avatar:/avatars/user.png}")
    private String avatar;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        seedUserIfMissing(adminUsername, adminEmail, adminPassword, Role.ADMIN);
        seedUserIfMissing(userUsername, userEmail, userPassword, Role.USER);
    }

    private void seedUserIfMissing(String username, String email, String rawPassword, Role role) {
        boolean exists = userRepository.existsByUsername(username) || userRepository.existsByEmail(email);
        if (exists) {
            System.out.println("User " + username + " already exists");
            return;
        }
        CustomUserDetails user = new CustomUserDetails(
                username,
                email,
                defaultName,
                passwordEncoder.encode(rawPassword),
                role,
                avatar
        );
        userRepository.save(user);
        System.out.println("User " + username + " created");
    }
}
