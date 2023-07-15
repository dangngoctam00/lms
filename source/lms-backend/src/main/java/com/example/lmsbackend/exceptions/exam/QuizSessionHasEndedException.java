package com.example.lmsbackend.exceptions.exam;

import java.util.UUID;

public class QuizSessionHasEndedException extends RuntimeException {
    private UUID id;

    public QuizSessionHasEndedException(UUID id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return String.format("Quiz session with id %s has ended", id);
    }
}
