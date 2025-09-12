package com.r2m.custom_header.model.exceptions;

import org.springframework.security.core.AuthenticationException;

public class InvalidAuthenticationException extends AuthenticationException {
    public InvalidAuthenticationException(String msg) {
        super(msg);
    }
}
