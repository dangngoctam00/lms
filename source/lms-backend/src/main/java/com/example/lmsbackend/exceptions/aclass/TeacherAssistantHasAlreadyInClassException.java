package com.example.lmsbackend.exceptions.aclass;

public class TeacherAssistantHasAlreadyInClassException extends RuntimeException {

    private Long classId;
    private Long teacherAssistantId;

    public TeacherAssistantHasAlreadyInClassException(Long classId, Long teacherAssistantId) {
        this.classId = classId;
        this.teacherAssistantId = teacherAssistantId;
    }

    @Override
    public String getMessage() {
        return String.format("Teacher assistant %s has already in belongs to class %s", teacherAssistantId, classId);
    }
}
