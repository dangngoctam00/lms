package com.example.lmsbackend.exceptions.course;

public class QuizNotFoundException extends RuntimeException {
    private Long id;

    public QuizNotFoundException(Long id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return String.format("Quiz %s is not found", id);
    }
}
