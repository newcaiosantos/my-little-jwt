package com.my.little.jwt.core.helper;

import com.my.little.jwt.core.config.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class AuthHelper {

    @Value("${app.jwt.key.private}")
    private String privateKeyAsString;

    @Value("${app.jwt.key.public}")
    private String publicKeyAsString;

    @Autowired
    CryptHelper cryptHelper;

    public String buildJWT(String userId, List<String> roles) {
        return Jwts.builder()
                .signWith(cryptHelper.toPrivateKey(privateKeyAsString), SignatureAlgorithm.RS256)
                .setHeaderParam("typ", Constants.Security.TOKEN_TYPE)
                .setIssuer(Constants.Security.TOKEN_ISSUER)
                .setAudience(Constants.Security.TOKEN_AUDIENCE)
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + Constants.Security.TOKEN_EXPIRATION))
                .claim("rol", roles)
                .compact();
    }

    public Jws<Claims> verifyJWT(String jwt) {
        return Jwts.parser()
                .setSigningKey(cryptHelper.toPublicKey(publicKeyAsString))
                .parseClaimsJws(jwt);
    }
}
