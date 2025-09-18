package com.ibb.library.service.impl;

import com.ibb.library.dto.request.LoginRequest;
import com.ibb.library.dto.request.RegisterRequest;
import com.ibb.library.dto.response.LoginResponse;
import com.ibb.library.entity.User;
import com.ibb.library.repository.UserRepository;
import com.ibb.library.security.JwtUtil;
import com.ibb.library.security.Role;
import com.ibb.library.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public void register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new IllegalArgumentException("E-posta kullanımda: " + req.email());
        }
        var user = User.builder()
                .firstName(req.firstName())
                .lastName(req.lastName())
                .email(req.email())
                .password(passwordEncoder.encode(req.password()))
                .role(Role.USER) // kayıt olan herkes USER
                .build();
        userRepository.save(user);
    }

    @Override
    public LoginResponse login(LoginRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password())
        );

        var user = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı"));
        String role = user.getRole().name(); // ADMIN / USER

        String token = jwtUtil.generateToken(
                user.getEmail(),
                Map.of("role", role)
        );
        return new LoginResponse(token);
    }

}
