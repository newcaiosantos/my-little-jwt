package com.my.little.jwt.core.domain.http.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorResponse {
    String code;
    String message;
    List<String> problems;
}
