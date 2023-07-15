package com.example.lmsbackend.exceptions.resource;

public class TextbookNotFoundException extends RuntimeException {
    private Long id;

    public TextbookNotFoundException(Long id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return String.format("Textbook %s is not found", id);
    }
}
