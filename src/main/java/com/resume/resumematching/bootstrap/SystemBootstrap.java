package com.resume.resumematching.bootstrap;

import com.resume.resumematching.entity.User;
import com.resume.resumematching.enums.Role;
import com.resume.resumematching.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SystemBootstrap implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${bootstrap.superuser.email}")
    private String superUserEmail;

    @Value("${bootstrap.superuser.password}")
    private String superUserPassword;

    @Override
    public void run(String... args) {
        createSuperUserIfNotExists();
    }

    private void createSuperUserIfNotExists() {

        if (userRepository.existsByEmail(superUserEmail)) {
            return;
        }

        User superUser = User.builder()
                .email(superUserEmail)
                .passwordHash(passwordEncoder.encode(superUserPassword))
                .role(Role.SUPERUSER)
                .disabled(false)
                .tenant(null)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(superUser);

        System.out.println("Superuser created: " + superUserEmail);
    }
}
