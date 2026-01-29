package com.example.service;

import com.example.dto.request.LoginRequest;
import com.example.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
}
