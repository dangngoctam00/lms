package com.example.lmsbackend.mapper.exam;

import com.example.lmsbackend.domain.exam.ExamEntity;
import com.example.lmsbackend.dto.exam.CreateExamRequestDto;
import com.example.lmsbackend.dto.exam.ExamContentItemDto;
import com.example.lmsbackend.dto.exam.ExamDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        AuditMapper.class
})
public interface ExamMapper {

    @Mapping(target = "textbooks", ignore = true)
    ExamEntity mapToExamEntity(CreateExamRequestDto dto);

    @Mapping(target = "questionList", ignore = true)
    @Mapping(target = "courseId", source = "entity.course.id")
    @Mapping(target = "courseName", source = "entity.course.name")
    ExamDto mapToExamContentDto(ExamEntity entity);

    @Mapping(target = "courseName", source = "entity.course.name")
    ExamContentItemDto mapToExamContentItem(ExamEntity entity);
}