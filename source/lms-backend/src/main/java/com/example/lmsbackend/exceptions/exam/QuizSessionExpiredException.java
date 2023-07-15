package com.example.lmsbackend.exceptions.exam;

import java.time.LocalDateTime;

public class QuizSessionExpiredException extends RuntimeException {
    private LocalDateTime startedAt;
    private Long timeLimit;

    public QuizSessionExpiredException(LocalDateTime startedAt, Long timeLimit) {
        this.startedAt = startedAt;
        this.timeLimit = timeLimit;
    }

    @Override
    public String getMessage() {
        return String.format("Session has expired, startedAt {}, now {} and the time of quiz is {}", startedAt, LocalDateTime.now(), timeLimit);
    }
}
