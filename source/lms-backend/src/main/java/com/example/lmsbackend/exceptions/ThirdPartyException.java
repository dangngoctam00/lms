package com.example.lmsbackend.exceptions;

public class ThirdPartyException extends RuntimeException {
    public ThirdPartyException(String message, Throwable cause) {
        super(message, cause);
    }
}
