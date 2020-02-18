package com.my.little.jwt.core.config;

import com.my.little.jwt.core.helper.AuthHelper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JwtFilter extends BasicAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    public JwtFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        var authentication = getAuthentication(request);
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private AuthHelper getAuthHelper(HttpServletRequest request) {
        ServletContext servletContext = request.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        assert webApplicationContext != null;
        return webApplicationContext.getBean(AuthHelper.class);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        AuthHelper authHelper = getAuthHelper(request);
        var tokenHeader = request.getHeader(Constants.Security.TOKEN_HEADER);
        if (StringUtils.isNotEmpty(tokenHeader) && tokenHeader.startsWith(Constants.Security.TOKEN_PREFIX)) {
            try {
                String token = tokenHeader.replace(Constants.Security.TOKEN_PREFIX, "");
                var parsedToken = authHelper.verifyJWT(token);
                var userId = parsedToken
                        .getBody()
                        .getSubject();
                var authorities = ((List<?>) parsedToken
                        .getBody()
                        .get("rol"))
                        .stream()
                        .map(authority -> new SimpleGrantedAuthority((String) authority))
                        .collect(Collectors.toList());
                if (StringUtils.isNotEmpty(userId)) {
                    log.info("Authenticated request from user : {}", userId);
                    return new UsernamePasswordAuthenticationToken(userId, null, authorities);
                }
            } catch (ExpiredJwtException exception) {
                log.warn("Request to parse expired JWT : {} failed : {}", tokenHeader, exception.getMessage());
            } catch (UnsupportedJwtException exception) {
                log.warn("Request to parse unsupported JWT : {} failed : {}", tokenHeader, exception.getMessage());
            } catch (MalformedJwtException exception) {
                log.warn("Request to parse invalid JWT : {} failed : {}", tokenHeader, exception.getMessage());
            } catch (SignatureException exception) {
                log.warn("Request to parse JWT with invalid signature : {} failed : {}", tokenHeader, exception.getMessage());
            } catch (IllegalArgumentException exception) {
                log.warn("Request to parse empty or null JWT : {} failed : {}", tokenHeader, exception.getMessage());
            }
        }
        return null;
    }
}