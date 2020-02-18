package com.my.little.jwt.core.domain;

import com.my.little.jwt.core.domain.enumerators.UserRole;
import com.my.little.jwt.core.domain.enumerators.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String name;
    private String username;
    private String email;
    private String password;
    private String fbId;
    private List<UserRole> roles;
    private UserStatus status;
}
