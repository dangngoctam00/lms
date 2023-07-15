package com.example.lmsbackend.exceptions;

public class UnsupportedContentTypeException extends RuntimeException {
    private String type;

    public UnsupportedContentTypeException(String type) {
        this.type = type;
    }

    @Override
    public String getMessage() {
        return String.format("Content type %s is unsupported", type);
    }
}
