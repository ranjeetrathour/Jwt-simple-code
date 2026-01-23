package com.example.service;


import com.example.dto.request.UserCreateRequest;
import com.example.dto.response.UserCreateResponse;

public interface UserService {

    UserCreateResponse createUser(UserCreateRequest userCreateRequest);
}

