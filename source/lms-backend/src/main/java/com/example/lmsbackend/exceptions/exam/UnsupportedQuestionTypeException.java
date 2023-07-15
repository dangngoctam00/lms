package com.example.lmsbackend.exceptions.exam;

public class UnsupportedQuestionTypeException extends RuntimeException {
    private String type;

    public UnsupportedQuestionTypeException(String type) {
        this.type = type;
    }

    @Override
    public String getMessage() {
        return String.format("Question type: %s is unsupported", type);
    }
}
