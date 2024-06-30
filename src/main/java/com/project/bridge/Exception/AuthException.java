package com.project.bridge.Exception;

import org.springframework.security.core.AuthenticationException;

public class AuthException extends AuthenticationException {
    public AuthException(String msg) {
        super(msg);
    }
    
    public AuthException() {
        super("unauthorized");
    }
}
