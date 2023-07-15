package com.example.lmsbackend.exceptions;

public class UnsupportedResourceAuthorizationRuleException extends RuntimeException {

    private String type;

    public UnsupportedResourceAuthorizationRuleException(String type) {
        this.type = type;
    }

    @Override
    public String getMessage() {
        return "Dev need to handle authorization for resource type: " + type;
    }
}
