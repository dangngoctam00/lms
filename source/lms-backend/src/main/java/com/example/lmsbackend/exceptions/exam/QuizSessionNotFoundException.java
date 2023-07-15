package com.example.lmsbackend.exceptions.exam;

public class QuizSessionNotFoundException extends RuntimeException {
    private String id;

    public QuizSessionNotFoundException(String id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return String.format("Quiz session %s is not found", id);
    }
}
