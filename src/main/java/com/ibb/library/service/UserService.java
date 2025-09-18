package com.ibb.library.service;

import com.ibb.library.dto.request.UserRequest;
import com.ibb.library.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse create(UserRequest req);
    List<UserResponse> list();
    UserResponse get(Long id);
    UserResponse update(Long id, UserRequest req);
    void delete(Long id);
}
