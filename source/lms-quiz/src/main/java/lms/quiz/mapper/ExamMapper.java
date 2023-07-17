package lms.quiz.mapper;

import lms.quiz.domain.ExamEntity;
import lms.quiz.dto.ExamDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExamMapper {

    ExamEntity mapToExamEntity(ExamDto dto);

    ExamDto mapToExamDto(ExamEntity entity);
}
