package com.example.lmsbackend.exceptions.exam;

import java.util.UUID;

public class QuestionHasNotBeenAnsweredCompletedException extends RuntimeException {
    private UUID sessionId;
    private Long questionId;

    public QuestionHasNotBeenAnsweredCompletedException(UUID sessionId, Long questionId) {
        this.sessionId = sessionId;
        this.questionId = questionId;
    }

    @Override
    public String getMessage() {
        return String.format("Session %s has question %s that has not been fully answered", sessionId, questionId);
    }
}
