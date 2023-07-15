package com.example.lmsbackend.exceptions;

public class InvalidParameterException extends RuntimeException {
    private String parameter;

    public InvalidParameterException(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public String getMessage() {
        return String.format("Invalid parameter: %s", parameter);
    }
}
