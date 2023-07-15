package com.example.lmsbackend.exceptions.course;

public class ChapterNotFoundException extends RuntimeException {
    private Long id;

    public ChapterNotFoundException(Long id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return String.format("Chapter %s is not found", id);
    }
}
