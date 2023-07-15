package com.example.lmsbackend.exceptions.aclass;

public class ClassNotFoundException extends RuntimeException{
    private final Long id;

    public ClassNotFoundException(Long id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Lớp học không tồn tại";
    }
}
