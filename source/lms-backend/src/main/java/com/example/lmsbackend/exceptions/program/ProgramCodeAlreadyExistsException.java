package com.example.lmsbackend.exceptions.program;

public class ProgramCodeAlreadyExistsException extends RuntimeException {
    private final String code;

    public ProgramCodeAlreadyExistsException(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        String messageTemplate = "Program code: '%s' alreay exists";
        return String.format(messageTemplate, code);
    }
}
