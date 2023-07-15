package com.example.lmsbackend.exceptions.course;

public class ScoreTagNotFoundException extends RuntimeException {
    private Long id;
    private String title;

    public ScoreTagNotFoundException(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public String getMessage() {
        return String.format("Score tag %s - %s is not found", id, title);
    }
}
