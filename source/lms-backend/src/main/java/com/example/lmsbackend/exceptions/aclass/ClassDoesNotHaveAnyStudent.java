package com.example.lmsbackend.exceptions.aclass;

public class ClassDoesNotHaveAnyStudent extends RuntimeException {
    private Long classId;

    public ClassDoesNotHaveAnyStudent(Long classId) {
        this.classId = classId;
    }

    @Override
    public String getMessage() {
        return String.format("Class %s does not have any student", classId);
    }
}
