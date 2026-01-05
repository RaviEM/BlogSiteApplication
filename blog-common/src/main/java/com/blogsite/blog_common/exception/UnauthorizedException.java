package com.blogsite.blog_common.exception;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public static UnauthorizedException invalidCredentials() {
        return new UnauthorizedException("Invalid email or password");
    }

    public static UnauthorizedException tokenExpired() {
        return new UnauthorizedException("Token has expired");
    }

    public static UnauthorizedException accessDenied() {
        return new UnauthorizedException("Access denied");
    }
}