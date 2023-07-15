package com.example.lmsbackend.multitenancy.exception;

public class DomainAlreadyExistsException extends RuntimeException {
    private final String domain;

    public DomainAlreadyExistsException(String domain) {
        this.domain = domain;
    }

    @Override
    public String getMessage() {
        String message = "Domain '%s' already exists";
        return String.format(message, domain);
    }
}
