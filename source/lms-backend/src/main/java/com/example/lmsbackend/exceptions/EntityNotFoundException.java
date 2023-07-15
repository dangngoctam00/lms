package com.example.lmsbackend.exceptions;

public class EntityNotFoundException extends RuntimeException {
    private String type;
    private Long id;

    public EntityNotFoundException(String type, Long id) {
        this.type = type;
        this.id = id;
    }

    @Override
    public String getMessage() {
        return String.format("%s %s is not found", type, id);
    }
}
