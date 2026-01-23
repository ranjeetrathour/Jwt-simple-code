package com.example.dto.response;

import com.example.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserCreateResponse {
    private String id;
    private String username;

    private String firstName;
    private String lastName;

    private List<Role> roles;
}
