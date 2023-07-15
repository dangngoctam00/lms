package com.example.lmsbackend.exceptions.program;

public class ProgramNotFoundException extends RuntimeException {

    private final Long id;

    public ProgramNotFoundException(Long id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        String messageTemplate = "Program '%s' not found";
        return String.format(messageTemplate, id);
    }
}
