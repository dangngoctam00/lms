package com.example.lmsbackend.exceptions.aclass;

public class PostNotFoundException extends RuntimeException {
    private Long id;

    public PostNotFoundException(Long id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return String.format("Post %s is not found", id);
    }
}
