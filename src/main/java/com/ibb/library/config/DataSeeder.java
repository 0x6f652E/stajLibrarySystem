package com.ibb.library.config;

import com.ibb.library.entity.User;
import com.ibb.library.repository.UserRepository;
import com.ibb.library.security.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner seedAdmin() {
        return args -> {
            userRepository.findByEmail("admin@lms.local").ifPresentOrElse(
                    u -> {}, // zaten var
                    () -> {
                        User admin = User.builder()
                                .firstName("System")
                                .lastName("Admin")
                                .email("admin@lms.local")
                                .password(passwordEncoder.encode("Admin#123")) // test şifresi
                                .role(Role.ADMIN)
                                .build();
                        userRepository.save(admin);
                    }
            );
        };
    }
}
