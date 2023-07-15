package com.example.lmsbackend.exceptions.exam;

public class SessionHasNotEndedException extends RuntimeException {
    private String sessionId;

    public SessionHasNotEndedException(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String getMessage() {
        return String.format("Session %s has not ended", sessionId);
    }
}
