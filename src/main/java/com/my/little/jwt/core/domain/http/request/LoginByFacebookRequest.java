package com.my.little.jwt.core.domain.http.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginByFacebookRequest {
    private String fbToken;
}
