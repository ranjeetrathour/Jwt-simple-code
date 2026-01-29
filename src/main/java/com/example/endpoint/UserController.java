package com.example.endpoint;

import com.example.dto.request.UserCreateRequest;
import com.example.dto.response.UserCreateResponse;
import com.example.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserCreateResponse> createUser(
            @RequestBody UserCreateRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

}
