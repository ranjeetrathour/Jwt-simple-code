package com.example.service.impl;

import com.example.crypto.CryptoService;
import com.example.dto.request.UserCreateRequest;
import com.example.dto.response.UserCreateResponse;
import com.example.entity.User;
import com.example.exceptions.GenericException;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CryptoService cryptoService;

    @Override
    public UserCreateResponse createUser(UserCreateRequest request) {

        if (request == null) {
            throw new GenericException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Request body cannot be null"
            );
        }

        // Build User entity
        /**
         * this can be achieve with mapper
         * TODO use mapper
         */
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(request.getUsername());
        user.setPassword(cryptoService.encrypt(request.getPassword())); // hash later
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRoles(request.getRoles());

        // Save
        User savedUser = userRepository.save(user);

        // Build response
        /**
         * this can be achieve with mapper
         * TODO use mapper
         */
        UserCreateResponse response = new UserCreateResponse();
        response.setId(savedUser.getId());
        response.setUsername(savedUser.getUsername());
        response.setFirstName(savedUser.getFirstName());
        response.setLastName(savedUser.getLastName());
        response.setRoles(savedUser.getRoles());
        return response;
    }
}
