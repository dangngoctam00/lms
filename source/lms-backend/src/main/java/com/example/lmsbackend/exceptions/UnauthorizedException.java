package com.example.lmsbackend.exceptions;

public class UnauthorizedException extends RuntimeException {
    private String message;

    public UnauthorizedException() {
    }

    public UnauthorizedException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
