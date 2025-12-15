package com.resume.resumematching.bootstrap;

import com.resume.resumematching.entity.User;
import com.resume.resumematching.enums.Role;
import com.resume.resumematching.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SystemBootstrap implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        createSuperUserIfNotExists();
    }

    private void createSuperUserIfNotExists() {
        String superUserEmail = "superadmin@resumematch.com";

        if (userRepository.existsByEmail(superUserEmail)) {
            return;
        }

        User superUser = User.builder()
                .email(superUserEmail)
                .passwordHash(passwordEncoder.encode("SuperAdmin@123"))
                .role(Role.SUPERUSER)
                .disabled(false)
                .tenant(null)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(superUser);

        System.out.println("Superuser created: " + superUserEmail);
    }
}
