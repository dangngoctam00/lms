package com.example.lmsbackend.exceptions.aclass;

public class ClassCodeAlreadyExistException extends RuntimeException {
    private final String code;

    public ClassCodeAlreadyExistException(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return String.format("Mã lớp học %s đã tồn tại", code);
    }
}
