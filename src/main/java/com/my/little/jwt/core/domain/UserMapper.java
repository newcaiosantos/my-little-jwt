package com.my.little.jwt.core.domain;

import com.my.little.jwt.core.domain.entity.UserEntity;
import com.my.little.jwt.core.domain.enumerators.UserRole;
import com.my.little.jwt.core.domain.enumerators.UserStatus;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    private UserMapper() throws Exception {
        throw new Exception("instance not allowed");
    }

    public static User getUserFromUserEntity(UserEntity userEntity) {
        List<UserRole> roles = Arrays.stream(new Gson().fromJson(userEntity.getRoles(), String[].class))
                .map(roleAsString -> {
                    return UserRole.valueOf(roleAsString.toUpperCase());
                })
                .collect(Collectors.toList());
        UserStatus status = UserStatus.valueOf(userEntity.getStatus().toUpperCase());
        return new User(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getFbId(),
                roles,
                status);
    }

    public static UserEntity getUserEntityFromUser(User user) {
        String roles = new Gson().toJson(
                user.getRoles()
                        .stream()
                        .map(role -> {
                            return role.toString().toLowerCase();
                        })
                        .collect(Collectors.toList()));
        return new UserEntity(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getFbId(),
                roles,
                user.getStatus().toString().toLowerCase()
        );
    }
}
