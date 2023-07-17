package lms.quiz.dto;

import lms.quiz.domain.QuestionData;
import lms.quiz.enums.QuizContext;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizQuestionDto {

    private Long id;
    private QuestionData data;
    private QuizQuestionDto parent;
    private QuizContext context;
    private Long contextId;
    private Long position;
}
