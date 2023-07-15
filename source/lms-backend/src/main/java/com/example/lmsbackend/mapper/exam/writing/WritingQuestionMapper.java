package com.example.lmsbackend.mapper.exam.writing;

import com.example.lmsbackend.domain.exam.writing_question.WritingQuestionEntity;
import com.example.lmsbackend.dto.exam.writing.WritingQuestionAnswerDto;
import com.example.lmsbackend.dto.exam.writing.WritingQuestionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface WritingQuestionMapper {

    WritingQuestionEntity mapToWritingQuestionEntity(WritingQuestionDto dto);

    
    @Mapping(target = "point", source = "entity.question.point")
    @Mapping(target = "attachment", source = "entity.question.attachment")
    @Mapping(target = "note", source = "entity.question.note")
    @Mapping(target = "description", source = "entity.question.description")
    WritingQuestionDto mapToWritingQuestionDto(WritingQuestionEntity entity);

    
    @Mapping(target = "point", source = "entity.question.point")
    @Mapping(target = "attachment", source = "entity.question.attachment")
    @Mapping(target = "note", source = "entity.question.note")
    @Mapping(target = "description", source = "entity.question.description")
    WritingQuestionAnswerDto mapToWritingQuestionAnswerDto(WritingQuestionEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "entity.question.point", source = "dto.point")
    @Mapping(target = "entity.question.attachment", source = "dto.attachment")
    @Mapping(target = "entity.question.description", source = "dto.description")
    @Mapping(target = "entity.question.note", source = "dto.note")
    void mapToWritingQuestion(@MappingTarget WritingQuestionEntity entity, WritingQuestionDto dto);
}
