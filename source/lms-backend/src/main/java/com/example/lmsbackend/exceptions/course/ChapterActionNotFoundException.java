package com.example.lmsbackend.exceptions.course;

public class ChapterActionNotFoundException extends RuntimeException {
    private Long id;

    public ChapterActionNotFoundException(Long id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return String.format("Chapter action %s is not found", id);
    }
}
