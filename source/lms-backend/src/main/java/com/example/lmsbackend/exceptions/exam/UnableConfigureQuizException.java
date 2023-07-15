package com.example.lmsbackend.exceptions.exam;

public class UnableConfigureQuizException extends RuntimeException {

    public UnableConfigureQuizException() {
    }

    public UnableConfigureQuizException(String message) {
        super(message);
    }
}
