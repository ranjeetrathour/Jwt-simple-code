package com.example.service;

import com.example.LoginResponse;
import com.example.dto.request.LoginRequest;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
}
