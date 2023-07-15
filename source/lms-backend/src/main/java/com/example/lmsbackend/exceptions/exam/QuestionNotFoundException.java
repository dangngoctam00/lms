package com.example.lmsbackend.exceptions.exam;

public class QuestionNotFoundException extends RuntimeException {
    private Long id;
    private String type;

    public QuestionNotFoundException(Long id) {
        this.id = id;
    }

    public QuestionNotFoundException(Long id, String type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public String getMessage() {
        return String.format("Question %s with type %s not found", id, type);
    }
}
