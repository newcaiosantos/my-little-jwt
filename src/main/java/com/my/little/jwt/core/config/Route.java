package com.my.little.jwt.core.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpMethod;

@Data
@AllArgsConstructor
public class Route {
    private HttpMethod method;
    private String path;
}
