package com.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenRefreshException extends RuntimeException{

    public TokenRefreshException(String token, String msg) {
        super(String.format("Failed for [%s]: %s", token, msg));
    }

}
