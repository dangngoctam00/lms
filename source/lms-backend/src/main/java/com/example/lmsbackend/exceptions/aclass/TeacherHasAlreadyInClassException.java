package com.example.lmsbackend.exceptions.aclass;

public class TeacherHasAlreadyInClassException extends RuntimeException {

    private Long classId;
    private Long teacherId;

    public TeacherHasAlreadyInClassException(Long classId, Long teacherId) {
        this.classId = classId;
        this.teacherId = teacherId;
    }

    @Override
    public String getMessage() {
        return String.format("Teacher %s has already in belongs to class %s", teacherId, classId);
    }
}
