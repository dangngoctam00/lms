package com.example.lmsbackend.exceptions.exam;

public class QuizNotFoundInClassException extends RuntimeException {
    private Long quizId;
    private Long classId;

    public QuizNotFoundInClassException(Long quizId, Long classId) {
        this.quizId = quizId;
        this.classId = classId;
    }

    @Override
    public String getMessage() {
        return String.format("Quiz %s is not found in class %s", quizId, classId);
    }
}
