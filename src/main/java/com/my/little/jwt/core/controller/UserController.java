package com.my.little.jwt.core.controller;

import com.my.little.jwt.core.domain.User;
import com.my.little.jwt.core.domain.http.request.LoginByEmailRequest;
import com.my.little.jwt.core.domain.http.request.LoginByFacebookRequest;
import com.my.little.jwt.core.domain.http.request.LoginByUsernameRequest;
import com.my.little.jwt.core.domain.http.response.LoginResponse;
import com.my.little.jwt.core.domain.http.response.SimpleSuccessResponse;
import com.my.little.jwt.core.service.LoginService;
import com.my.little.jwt.core.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    LoginService loginService;

    @Autowired
    SignUpService signUpService;

    @PostMapping("/sign-up")
    public SimpleSuccessResponse signUp(@RequestBody User user) throws Exception {
        signUpService.signUp(user);
        return new SimpleSuccessResponse();
    }

    @PostMapping("/login/by/username")
    public LoginResponse loginByUsername(@RequestBody LoginByUsernameRequest body) throws Exception {
        String username = body.getUsername();
        String password = body.getPassword();
        String token = loginService.loginByUsernameAndPassword(username, password);
        return new LoginResponse(token);
    }

    @PostMapping("/login/by/email")
    public LoginResponse loginByEmail(@RequestBody LoginByEmailRequest body) throws Exception {
        String email = body.getEmail();
        String password = body.getPassword();
        String token = loginService.loginByEmailAndPassword(email, password);
        return new LoginResponse(token);
    }

    @PostMapping("/login/by/facebook")
    public LoginResponse loginByFacebook(@RequestBody LoginByFacebookRequest body) throws Exception {
        return new LoginResponse(loginService.loginByFacebook(body.getFbToken()));
    }
}
