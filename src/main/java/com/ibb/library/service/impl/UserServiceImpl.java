package com.ibb.library.service.impl;

import com.ibb.library.dto.request.UserRequest;
import com.ibb.library.dto.response.UserResponse;
import com.ibb.library.entity.User;
import com.ibb.library.repository.UserRepository;
import com.ibb.library.security.Role;
import com.ibb.library.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private UserResponse toDto(User u) {
        return new UserResponse(
                u.getId(), u.getFirstName(), u.getLastName(),
                u.getEmail(), u.getRole().name()
        );
    }

    @Override
    public UserResponse create(UserRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new IllegalArgumentException("E-posta kullanımda: " + req.email());
        }
        var user = User.builder()
                .firstName(req.firstName())
                .lastName(req.lastName())
                .email(req.email())
                .password(req.password() == null ? null : passwordEncoder.encode(req.password()))
                .role(Role.valueOf(req.role().toUpperCase()))
                .build();
        return toDto(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> list() {
        return userRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse get(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı: " + id));
        return toDto(user);
    }

    @Override
    public UserResponse update(Long id, UserRequest req) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı: " + id));

        // email değişecekse çakışma kontrolü
        if (!user.getEmail().equals(req.email()) && userRepository.existsByEmail(req.email())) {
            throw new IllegalArgumentException("E-posta kullanımda: " + req.email());
        }

        user.setFirstName(req.firstName());
        user.setLastName(req.lastName());
        user.setEmail(req.email());
        if (req.password() != null && !req.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(req.password()));
        }
        user.setRole(Role.valueOf(req.role().toUpperCase()));
        return toDto(userRepository.save(user));
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
