package com.example.lmsbackend.exceptions.aclass;

public class CommentNotFoundException extends RuntimeException {
    private Long id;

    public CommentNotFoundException(Long id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return String.format("Comment %s is not found", id);
    }
}
