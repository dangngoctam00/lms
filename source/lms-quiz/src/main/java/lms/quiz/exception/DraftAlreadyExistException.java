package lms.quiz.exception;

import exception.LmsDomainException;

public class DraftAlreadyExistException extends LmsDomainException {

    public DraftAlreadyExistException(Long examId) {
        super(100L, "Draft for exam " + examId + " has been existed already");
    }
}
