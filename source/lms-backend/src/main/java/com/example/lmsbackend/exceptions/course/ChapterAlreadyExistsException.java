package com.example.lmsbackend.exceptions.course;

public class ChapterAlreadyExistsException extends RuntimeException {
    private Long id;

    public ChapterAlreadyExistsException(Long id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return String.format("Chapter %s is already exists", id);
    }
}
