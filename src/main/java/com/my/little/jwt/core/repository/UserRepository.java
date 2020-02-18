package com.my.little.jwt.core.repository;

import com.my.little.jwt.core.domain.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Long countByEmail(String email);
    Long countByUsername(String email);
    Long countByFbId(String fbId);

    UserEntity findByEmail(String email);
    UserEntity findByUsername(String username);
    UserEntity findByFbId(String fbId);
}
