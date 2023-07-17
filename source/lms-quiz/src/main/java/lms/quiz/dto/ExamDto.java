package lms.quiz.dto;

import lms.quiz.enums.ExamContext;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExamDto {

    private Long id;
    private String title;
    private ExamContext context;
    private Long contextId;
    private Boolean isPublished;
    private List<QuizQuestionDto> questions;
}
