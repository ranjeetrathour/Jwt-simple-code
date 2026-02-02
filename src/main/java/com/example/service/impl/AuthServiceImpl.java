package com.example.service.impl;

import com.example.LoginResponse;
import com.example.crypto.CryptoService;
import com.example.dto.request.LoginRequest;
import com.example.entity.User;
import com.example.exceptions.GenericException;
import com.example.repository.UserRepository;
import com.example.service.AuthService;
import com.example.utility.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final CryptoService cryptoService;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        if (Objects.isNull(loginRequest)) {
            throw new GenericException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Login request must not be null"
            );
        }

        User user = userRepository.findUserByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new GenericException(
                        HttpStatus.NOT_FOUND.value(),
                        "Incorrect username or password"
                ));
        if (!loginRequest.getPassword().equals(cryptoService.decrypt(user.getPassword()))){
            throw new GenericException(
                    HttpStatus.NOT_FOUND.value(),
                    "Incorrect username or password"
            );
        }

        String token = jwtUtils.generateToken(user.getUsername());

        var roles = user.getRoles().stream()
                .map(r -> com.example.Role.valueOf(r.name()))
                .toList();
        return LoginResponse.builder()
                .username(user.getUsername())
                .roles(roles)
                .token(token)
                .build();
    }
}
