package lms.quiz.mapper;

import lms.quiz.domain.QuizQuestionEntity;
import lms.quiz.dto.QuizQuestionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    QuizQuestionEntity mapToQuestionEntity(QuizQuestionDto dto);

    QuizQuestionDto mapToQuestionDto(QuizQuestionEntity entity);
}
