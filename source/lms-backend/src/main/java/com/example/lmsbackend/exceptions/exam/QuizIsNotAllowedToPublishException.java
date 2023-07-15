package com.example.lmsbackend.exceptions.exam;

import lombok.Data;

@Data
public class QuizIsNotAllowedToPublishException extends RuntimeException {

    public QuizIsNotAllowedToPublishException(String message) {
        super(message);
    }
}
