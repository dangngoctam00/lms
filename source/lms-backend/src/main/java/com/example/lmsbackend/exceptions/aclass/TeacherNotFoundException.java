package com.example.lmsbackend.exceptions.aclass;

public class TeacherNotFoundException extends RuntimeException {
    private Long teacherId;

    public TeacherNotFoundException(Long teacherId) {
        this.teacherId = teacherId;
    }

    @Override
    public String getMessage() {
        return String.format("Teacher %s not found", teacherId);
    }
}
