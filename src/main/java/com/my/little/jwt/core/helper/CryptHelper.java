package com.my.little.jwt.core.helper;

import com.my.little.jwt.core.config.Constants;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class CryptHelper {
    public String encodeBCrypt(CharSequence charSequence) {
        return new BCryptPasswordEncoder().encode(charSequence == null ? "" : charSequence);
    }

    public boolean matchesBCrypt(String notEncoded, String encoded) {
        return new BCryptPasswordEncoder().matches(notEncoded == null ? "" : notEncoded, encoded == null ? "" : encoded);
    }

    public String encodeMD5(String text) {
        try {
            String s = String.format("%s%s", Constants.Encryption.ENCRYPTION_HASH, text);
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(s.getBytes(), 0, s.length());
            return new BigInteger(1, m.digest()).toString(16);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean matchesMD5(String notEncoded, String encoded) {
        return encodeMD5(notEncoded).equals(encoded);
    }

    public PrivateKey toPrivateKey(String base64PrivateKey) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public PublicKey toPublicKey(String base64PublicKey) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
