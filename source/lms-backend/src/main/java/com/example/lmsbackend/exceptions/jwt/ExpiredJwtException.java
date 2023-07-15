package com.example.lmsbackend.exceptions.jwt;

public class ExpiredJwtException extends RuntimeException {

    private final String jwt;

    public ExpiredJwtException(String jwt, Throwable ex) {
        super(ex);
        this.jwt = jwt;
    }

    @Override
    public String getMessage() {
        return String.format("Jwt '%s' has expired", jwt);
    }
}
