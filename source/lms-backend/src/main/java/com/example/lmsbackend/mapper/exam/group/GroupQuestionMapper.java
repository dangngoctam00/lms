package com.example.lmsbackend.mapper.exam.group;

import com.example.lmsbackend.domain.exam.base_question.GroupQuestionEntity;
import com.example.lmsbackend.dto.exam.group.GroupQuestionAnswerDto;
import com.example.lmsbackend.dto.exam.group.GroupQuestionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GroupQuestionMapper {

    GroupQuestionEntity mapToGroupQuestionEntity(GroupQuestionDto dto);

    
    @Mapping(target = "point", source = "entity.question.point")
    @Mapping(target = "attachment", source = "entity.question.attachment")
    @Mapping(target = "note", source = "entity.question.note")
    @Mapping(target = "description", source = "entity.question.description")
    GroupQuestionDto mapToGroupQuestionDto(GroupQuestionEntity entity);

    
    @Mapping(target = "point", source = "entity.question.point")
    @Mapping(target = "attachment", source = "entity.question.attachment")
    @Mapping(target = "note", source = "entity.question.note")
    @Mapping(target = "description", source = "entity.question.description")
    GroupQuestionAnswerDto mapToGroupQuestionAnswerDto(GroupQuestionEntity entity);
}
