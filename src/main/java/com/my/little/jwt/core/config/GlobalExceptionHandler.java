package com.my.little.jwt.core.config;

import com.my.little.jwt.core.domain.http.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse genericException(Exception e) {
        System.out.println("generic exception");
        return new ErrorResponse("GENERIC", e.getMessage(), Collections.singletonList(e.getMessage()));
    }
}
