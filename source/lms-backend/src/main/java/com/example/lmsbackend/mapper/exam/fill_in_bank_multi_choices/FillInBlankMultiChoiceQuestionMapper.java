package com.example.lmsbackend.mapper.exam.fill_in_bank_multi_choices;


import com.example.lmsbackend.domain.exam.fill_in_blank_with_choices.FillInBlankMultiChoiceQuestionEntity;
import com.example.lmsbackend.dto.exam.fill_in_blank_multi_choices.FillInBlankMultiChoiceQuestionAnswerDto;
import com.example.lmsbackend.dto.exam.fill_in_blank_multi_choices.FillInBlankMultiChoiceQuestionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {
        FillInBlankMultiChoiceBlankMapper.class
})
public interface FillInBlankMultiChoiceQuestionMapper {

    @Mapping(target = "blanks", source = "dto.blankList", qualifiedByName = "InOrder")
    FillInBlankMultiChoiceQuestionEntity mapToFillInBlankMultiChoiceEntity(FillInBlankMultiChoiceQuestionDto dto);

    @Mapping(target = "blanks", source = "dto.blankList", qualifiedByName = "InOrder")
    @Mapping(target = "entity.question.point", source = "dto.point")
    @Mapping(target = "entity.question.attachment", source = "dto.attachment")
    @Mapping(target = "entity.question.description", source = "dto.description")
    @Mapping(target = "entity.question.note", source = "dto.note")
    void mapToFillInBlankMultiChoiceEntity(@MappingTarget FillInBlankMultiChoiceQuestionEntity entity, FillInBlankMultiChoiceQuestionDto dto);

    @Mapping(target = "blankList", source = "entity.blanks")
    
    @Mapping(target = "point", source = "entity.question.point")
    @Mapping(target = "attachment", source = "entity.question.attachment")
    @Mapping(target = "note", source = "entity.question.note")
    @Mapping(target = "description", source = "entity.question.description")
    FillInBlankMultiChoiceQuestionDto mapToFillInBlankMultiChoiceDto(FillInBlankMultiChoiceQuestionEntity entity);

    @Mapping(target = "blankList", source = "entity.blanks", qualifiedByName = "IgnoreAnswer")
    
    @Mapping(target = "point", source = "entity.question.point")
    @Mapping(target = "attachment", source = "entity.question.attachment")
    @Mapping(target = "note", source = "entity.question.note")
    @Mapping(target = "description", source = "entity.question.description")
    FillInBlankMultiChoiceQuestionDto mapToFillInBlankMultiChoiceDtoIgnoreAnswer(FillInBlankMultiChoiceQuestionEntity entity);

    
    @Mapping(target = "point", source = "entity.question.point")
    @Mapping(target = "attachment", source = "entity.question.attachment")
    @Mapping(target = "note", source = "entity.question.note")
    @Mapping(target = "description", source = "entity.question.description")
    FillInBlankMultiChoiceQuestionAnswerDto mapToFillInBlankMultiChoiceQuestionAnswerDto(FillInBlankMultiChoiceQuestionEntity entity);
}
