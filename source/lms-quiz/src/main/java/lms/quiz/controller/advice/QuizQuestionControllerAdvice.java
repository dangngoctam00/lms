package lms.quiz.controller.advice;

import dto.LmsApiResponse;
import exception.ResourceNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class QuizQuestionControllerAdvice {

    @ExceptionHandler(ResourceNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public LmsApiResponse<Void> handleResourceNotFoundException(ResourceNotFound e) {
        return new LmsApiResponse<>(e.getCode());
    }
}
