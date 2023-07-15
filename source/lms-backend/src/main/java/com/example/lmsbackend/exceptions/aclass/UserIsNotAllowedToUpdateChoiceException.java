package com.example.lmsbackend.exceptions.aclass;

public class UserIsNotAllowedToUpdateChoiceException extends RuntimeException {
    private String username;
    private Long choiceId;

    public UserIsNotAllowedToUpdateChoiceException(String username, Long choiceId) {
        this.username = username;
        this.choiceId = choiceId;
    }

    @Override
    public String getMessage() {
        return String.format("User %s is not allowed to update choice %s", username, choiceId);
    }
}
