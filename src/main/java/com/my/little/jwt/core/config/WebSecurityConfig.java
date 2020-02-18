package com.my.little.jwt.core.config;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        Constants.Security.PUBLIC_ROUTES.forEach(route -> permit(http, route));
        http.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll();
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(new JwtFilter(authenticationManager()));
        http.sessionManagement().disable();
    }

    private void permit(HttpSecurity http, Route route) {
        try {
            http.authorizeRequests().antMatchers(route.getMethod(), route.getPath()).permitAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
