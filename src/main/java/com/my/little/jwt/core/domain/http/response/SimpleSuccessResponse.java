package com.my.little.jwt.core.domain.http.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleSuccessResponse {
    String message = "success";
}
