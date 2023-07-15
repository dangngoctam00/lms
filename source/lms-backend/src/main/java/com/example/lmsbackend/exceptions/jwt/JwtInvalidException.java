package com.example.lmsbackend.exceptions.jwt;

public class JwtInvalidException extends RuntimeException{
    private final String jwt;

    public JwtInvalidException(String jwt, Throwable ex) {
        super(ex);
        this.jwt = jwt;
    }

    @Override
    public String getMessage() {
        return String.format("Jwt '%s' is invalid", jwt);
    }
}
