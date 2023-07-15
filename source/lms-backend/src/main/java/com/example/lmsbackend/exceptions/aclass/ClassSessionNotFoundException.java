package com.example.lmsbackend.exceptions.aclass;

public class ClassSessionNotFoundException extends RuntimeException {
    private Long id;

    public ClassSessionNotFoundException(Long id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return String.format("Class session %s is not found", id);
    }
}
