package com.example.lmsbackend.exceptions.exam;

public class ScoringManuallyNotSupportedForQuestionTypeException extends RuntimeException {
    private String type;

    public ScoringManuallyNotSupportedForQuestionTypeException(String type) {
        this.type = type;
    }

    @Override
    public String getMessage() {
        return String.format("Question type: %s is not supported for scoring manually", type);
    }
}
