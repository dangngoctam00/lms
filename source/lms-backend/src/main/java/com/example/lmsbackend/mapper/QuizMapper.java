package com.example.lmsbackend.mapper;

import com.example.lmsbackend.domain.classmodel.QuizClassEntity;
import com.example.lmsbackend.domain.coursemodel.QuizCourseEntity;
import com.example.lmsbackend.dto.exam.QuizEntryDto;
import com.example.lmsbackend.dto.response.course.QuizDto;
import com.example.lmsbackend.mapper.exam.AuditMapper;
import org.mapstruct.*;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring", uses = AuditMapper.class)
public interface QuizMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

    @Mapping(nullValuePropertyMappingStrategy = IGNORE, target = "id", qualifiedByName = "mapQuizId")
    @Mapping(target = "tag", ignore = true)
    QuizCourseEntity mapToQuizEntity(QuizDto dto);

    @Mapping(nullValuePropertyMappingStrategy = IGNORE, target = "id", qualifiedByName = "mapQuizId")
    @Mapping(target = "tag", ignore = true)
    QuizClassEntity mapToQuizClassEntity(QuizDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tag", ignore = true)
    void mapToQuizEntity(@MappingTarget QuizCourseEntity entity, QuizDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tag", ignore = true)
    void mapToQuizEntity(@MappingTarget QuizClassEntity entity, QuizDto dto);

    @Mapping(target = "tag", source = "quizCourseEntity.tag.title")
    @Mapping(target = "examId", source = "quizCourseEntity.exam.id")
    @Mapping(target = "examTitle", source = "quizCourseEntity.exam.title")
    QuizDto mapToQuizDto(QuizCourseEntity quizCourseEntity);

    @Mapping(target = "tag", source = "entity.tag.title")
    @Mapping(target = "isFromCourse", expression = "java(checkBelongsCourse(entity))")
    @Mapping(target = "examId", source = "entity.exam.id")
    @Mapping(target = "examTitle", source = "entity.exam.title")
    QuizDto mapToQuizDto(QuizClassEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tag", ignore = true)
    @Mapping(target = "exam", ignore = true)
    QuizClassEntity mapToQuizClass(QuizCourseEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tag", ignore = true)
    @Mapping(target = "exam", ignore = true)
    void mapToQuizClass(QuizCourseEntity quizCourse, @MappingTarget QuizClassEntity quizClass);

    @Mapping(target = "courseId", source = "entity.exam.course.id")
    @Mapping(target = "courseName", source = "entity.exam.course.name")
    @Mapping(target = "createdBy", source = "entity.createdBy")
    @Mapping(target = "updatedBy", source = "entity.updatedBy")
    QuizEntryDto mapToQuizEntry(QuizClassEntity entity);

    @Named(value = "mapQuizId")
    default Long mapQuizId(Long id) {
        if (id == null || id <= 0) return null;
        return id;
    }

    default Boolean checkBelongsCourse(QuizClassEntity entity) {
        return entity.getQuizCourse() != null;
    }
}
