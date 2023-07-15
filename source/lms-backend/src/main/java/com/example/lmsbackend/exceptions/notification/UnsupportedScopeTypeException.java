package com.example.lmsbackend.exceptions.notification;

public class UnsupportedScopeTypeException extends RuntimeException {
    private String scope;

    public UnsupportedScopeTypeException(String scope) {
        this.scope = scope;
    }

    @Override
    public String getMessage() {
        return String.format("Scope type %s is not supported", scope);
    }
}
