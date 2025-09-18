package com.ibb.library.service;

import com.ibb.library.dto.request.LoginRequest;
import com.ibb.library.dto.request.RegisterRequest;
import com.ibb.library.dto.response.LoginResponse;

public interface AuthService {
    void register(RegisterRequest req);    // basit: sadece kayıt
    LoginResponse login(LoginRequest req); // token döndür
}
