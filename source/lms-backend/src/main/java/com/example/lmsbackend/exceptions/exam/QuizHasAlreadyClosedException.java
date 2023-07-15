package com.example.lmsbackend.exceptions.exam;

import java.time.LocalDateTime;

public class QuizHasAlreadyClosedException extends RuntimeException {
    private Long id;
    private LocalDateTime closedAt;

    public QuizHasAlreadyClosedException(Long id, LocalDateTime closedAt) {
        this.id = id;
        this.closedAt = closedAt;
    }

    @Override
    public String getMessage() {
        return String.format("Exam instance %s has closed at %s", id, closedAt);
    }
}
