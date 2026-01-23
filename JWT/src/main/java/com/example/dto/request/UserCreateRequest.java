package com.example.dto.request;

import java.util.List;
import com.example.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserCreateRequest {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private List<Role> roles;
}
