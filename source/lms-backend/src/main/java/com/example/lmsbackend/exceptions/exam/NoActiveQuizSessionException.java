package com.example.lmsbackend.exceptions.exam;

import java.util.UUID;

public class NoActiveQuizSessionException extends RuntimeException {
    private UUID id;

    public NoActiveQuizSessionException(UUID id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return String.format("No active quiz session with id %s", id);
    }
}
