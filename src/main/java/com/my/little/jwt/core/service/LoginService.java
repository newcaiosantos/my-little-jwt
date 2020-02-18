package com.my.little.jwt.core.service;

import com.my.little.jwt.core.config.Constants;
import com.my.little.jwt.core.domain.User;
import com.my.little.jwt.core.domain.UserMapper;
import com.my.little.jwt.core.domain.entity.UserEntity;
import com.my.little.jwt.core.domain.enumerators.UserRole;
import com.my.little.jwt.core.domain.enumerators.UserStatus;
import com.my.little.jwt.core.helper.AuthHelper;
import com.my.little.jwt.core.helper.CryptHelper;
import com.my.little.jwt.core.helper.I18nHelper;
import com.my.little.jwt.core.repository.UserRepository;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoginService {

    Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    AuthHelper authHelper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CryptHelper cryptHelper;

    @Autowired
    I18nHelper i18nHelper;

    public String loginByUsernameAndPassword(String username, String password) throws Exception {
        UserEntity user = userRepository.findByUsername(username);
        return loginByUserAndPassword(UserMapper.getUserFromUserEntity(user), password);
    }

    public String loginByEmailAndPassword(String email, String password) throws Exception {
        UserEntity user = userRepository.findByEmail(email);
        return loginByUserAndPassword(UserMapper.getUserFromUserEntity(user), password);
    }

    private String loginByUserAndPassword(User user, String password) throws Exception {
        if (user == null) throw new Exception(i18nHelper.getString("invalid credentials"));
        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new Exception(i18nHelper.getString("inactive user"));
        }
        if (!cryptHelper.matchesMD5(password, user.getPassword())) {
            throw new Exception(i18nHelper.getString("invalid credentials"));
        }
        return buildJWT(user.getId(), user.getRoles());
    }

    private String buildJWT(Integer userId, List<UserRole> userRoles) {
        String userIdAsString = Integer.toString(userId);
        List<String> rolesAsStringList = userRoles
                .stream()
                .map(Enum::toString)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        return authHelper.buildJWT(userIdAsString, rolesAsStringList);
    }

    public String loginByFacebook(String fbToken) throws Exception {
        HttpResponse<JsonNode> response = Unirest.get(buildFacebookURL(fbToken)).asJson();
        if (!response.isSuccess()) {
            logger.error(String.format("[LoginService] http status text: %s", response.getStatusText()));
            throw new Exception(i18nHelper.getString("we had login problems"));
        }
        JSONObject fbUser = response.getBody().getObject();
        System.out.println(fbUser.toString());//TODO remove it and configure our log to output in console too
        logger.debug(String.format("[LoginService] %s", fbUser.toString()));
        String fbUserId = fbUser.getString("id");
        String fbUserName = fbUser.getString("name");
        if (userRepository.countByFbId(fbUserId) > 0) {
            User dbUser = UserMapper.getUserFromUserEntity(userRepository.findByFbId(fbUserId));
            return buildJWT(dbUser.getId(), dbUser.getRoles());
        } else {
            User newUser = new User();
            newUser.setFbId(fbUserId);
            newUser.setName(fbUserName);
            User createdUser = UserMapper.getUserFromUserEntity(createActiveUser(newUser));
            return buildJWT(createdUser.getId(), createdUser.getRoles());
        }
    }

    private UserEntity createActiveUser(User user) {
        user.setId(null);
        user.setRoles(Constants.SignUp.DEFAULT_USER_ROLES);
        user.setStatus(UserStatus.ACTIVE);
        return userRepository.save(UserMapper.getUserEntityFromUser(user));
    }

    private String buildFacebookURL(String facebookAccessToken) {
        return String.format(
                "%s/v%s/me?access_token=%s&fields=%s",
                Constants.Facebook.API_BASE_URL,
                Constants.Facebook.GRAPH_API_VERSION,
                facebookAccessToken,
                String.join(",", Constants.Facebook.LOGIN_FIELDS)
        );
    }
}
