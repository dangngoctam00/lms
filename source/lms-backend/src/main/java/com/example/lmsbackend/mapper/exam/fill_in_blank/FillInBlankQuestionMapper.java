package com.example.lmsbackend.mapper.exam.fill_in_blank;


import com.example.lmsbackend.domain.exam.fill_in_blank.FillInBlankQuestionEntity;
import com.example.lmsbackend.dto.exam.fill_in_blank.FillInBlankQuestionAnswerDto;
import com.example.lmsbackend.dto.exam.fill_in_blank.FillInBlankQuestionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {
        FillInBlankOptionMapper.class
})
public interface FillInBlankQuestionMapper {

    @Mapping(target = "blanks", source = "dto.blankList", qualifiedByName = "InOrder")
    FillInBlankQuestionEntity mapToFillInBlankQuestionEntity(FillInBlankQuestionDto dto);

    @Mapping(target = "blanks", source = "dto.blankList", qualifiedByName = "InOrder")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "entity.question.point", source = "dto.point")
    @Mapping(target = "entity.question.attachment", source = "dto.attachment")
    @Mapping(target = "entity.question.description", source = "dto.description")
    @Mapping(target = "entity.question.note", source = "dto.note")
    void mapToFillInBlankQuestionEntity(@MappingTarget FillInBlankQuestionEntity entity, FillInBlankQuestionDto dto);

    @Mapping(target = "blankList", source = "entity.blanks")
    
    @Mapping(target = "point", source = "entity.question.point")
    @Mapping(target = "attachment", source = "entity.question.attachment")
    @Mapping(target = "note", source = "entity.question.note")
    @Mapping(target = "description", source = "entity.question.description")
    FillInBlankQuestionDto mapToFillInBlankQuestionDto(FillInBlankQuestionEntity entity);

    @Mapping(target = "blankList", source = "entity.blanks", qualifiedByName = "IgnoreAnswer")
    
    @Mapping(target = "point", source = "entity.question.point")
    @Mapping(target = "attachment", source = "entity.question.attachment")
    @Mapping(target = "note", source = "entity.question.note")
    @Mapping(target = "description", source = "entity.question.description")
    FillInBlankQuestionDto mapToFillInBlankQuestionDtoIgnoreAnswer(FillInBlankQuestionEntity entity);

    
    @Mapping(target = "point", source = "entity.question.point")
    @Mapping(target = "attachment", source = "entity.question.attachment")
    @Mapping(target = "note", source = "entity.question.note")
    @Mapping(target = "description", source = "entity.question.description")
    FillInBlankQuestionAnswerDto mapToFillInBlankQuestionAnswerDto(FillInBlankQuestionEntity entity);
}
