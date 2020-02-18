package com.my.little.jwt.core.config;

import com.my.little.jwt.core.domain.enumerators.UserRole;
import org.springframework.http.HttpMethod;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Constants {
    private Constants() throws Exception {
        throw new Exception("instance not allowed");
    }
    public static class Encryption {
        public static final String ENCRYPTION_HASH = "hsf9a!@#435dsf$#%13ljasd7sdds";
    }
    public static class Security {
        public static final String TOKEN_HEADER = "Authorization";
        public static final String TOKEN_PREFIX = "Bearer ";
        public static final String TOKEN_TYPE = "JWT";
        public static final String TOKEN_ISSUER = "secure-api";
        public static final String TOKEN_AUDIENCE = "secure-app";
        public static final Long TOKEN_EXPIRATION = 2_592_000_000L; // 30d in milliseconds
        public static final List<Route> PUBLIC_ROUTES = Arrays.asList(
                new Route(HttpMethod.POST, "/user/sign-up"),
                new Route(HttpMethod.POST, "/user/login/by/username"),
                new Route(HttpMethod.POST, "/user/login/by/email"),
                new Route(HttpMethod.POST, "/user/login/by/facebook")
        );
    }
    public static class Facebook {
        public static final String API_BASE_URL = "https://graph.facebook.com";
        public static final String GRAPH_API_VERSION = "6.0";
        public static final List<String> LOGIN_FIELDS = Arrays.asList("id","name","email");
    }
    public static class SignUp{
        public static final List<UserRole> DEFAULT_USER_ROLES = Collections.singletonList(UserRole.DISPLAY);
    }
}
