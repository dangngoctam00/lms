package com.example.lmsbackend.exceptions.aclass;

import java.time.LocalDateTime;

public class FinishedTimeNotValidException extends RuntimeException {
    private LocalDateTime finishedAt;

    public FinishedTimeNotValidException(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    @Override
    public String getMessage() {
        return String.format("Finished time: %s must be before started time", finishedAt);
    }
}
