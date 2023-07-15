package com.example.lmsbackend.exceptions.exam;

public class QuestionNotExistedInExamException extends RuntimeException {
    private Long questionId;
    private Long examId;

    public QuestionNotExistedInExamException(Long questionId, Long examId) {
        this.questionId = questionId;
        this.examId = examId;
    }

    @Override
    public String getMessage() {
        return String.format("Question %s is not existed in exam %s", questionId, examId);
    }
}
