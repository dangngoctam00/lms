package com.example.lmsbackend.exceptions.exam;

public class ExamHasNotConfiguredForClassYetException extends RuntimeException {
    private Long classId;
    private Long examId;

    public ExamHasNotConfiguredForClassYetException(Long classId, Long examId) {
        this.classId = classId;
        this.examId = examId;
    }

    @Override
    public String getMessage() {
        return String.format("Exam %s has not configured for class %s", examId, classId);
    }
}
