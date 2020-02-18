package com.my.little.jwt.core.service;

import com.my.little.jwt.core.config.Constants;
import com.my.little.jwt.core.domain.User;
import com.my.little.jwt.core.domain.UserMapper;
import com.my.little.jwt.core.domain.enumerators.UserStatus;
import com.my.little.jwt.core.helper.CryptHelper;
import com.my.little.jwt.core.helper.I18nHelper;
import com.my.little.jwt.core.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {

    @Autowired
    I18nHelper i18nHelper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CryptHelper cryptHelper;

    public void signUp(User user) throws Exception {
        validateUserFieldsForSignUp(user);
        validateRepositoriesForSignUp(user);
        createInactiveUser(user);
    }

    private void createInactiveUser(User user) {
        user.setId(null);
        user.setPassword(cryptHelper.encodeMD5(user.getPassword()));
        user.setRoles(Constants.SignUp.DEFAULT_USER_ROLES);
        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(UserMapper.getUserEntityFromUser(user));
    }

    private void validateUserFieldsForSignUp(User user) throws Exception {
        if (StringUtils.isBlank(user.getUsername())) {
            throw new Exception(i18nHelper.getString("invalid username"));
        }
        if (StringUtils.isBlank(user.getEmail())) {
            throw new Exception(i18nHelper.getString("invalid email"));
        }
        if (StringUtils.isBlank(user.getName())) {
            throw new Exception(i18nHelper.getString("invalid name"));
        }
        if (StringUtils.isBlank(user.getPassword())) {
            throw new Exception(i18nHelper.getString("invalid password"));
        }
    }

    private void validateRepositoriesForSignUp(User user) throws Exception {
        if (userRepository.countByUsername(user.getUsername()) > 0) {
            throw new Exception(i18nHelper.getString("username already registered"));
        }
        if (userRepository.countByEmail(user.getEmail()) > 0) {
            throw new Exception(i18nHelper.getString("email already registered"));
        }
    }
}