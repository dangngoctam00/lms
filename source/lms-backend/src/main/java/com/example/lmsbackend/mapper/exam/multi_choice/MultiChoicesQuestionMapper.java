package com.example.lmsbackend.mapper.exam.multi_choice;


import com.example.lmsbackend.domain.exam.multi_choice.MultiChoiceQuestionEntity;
import com.example.lmsbackend.dto.exam.multi_choice.MultiChoiceQuestionAnswerDto;
import com.example.lmsbackend.dto.exam.multi_choice.MultiChoicesQuestionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {
        MultiChoiceOptionMapper.class
})
public interface MultiChoicesQuestionMapper {

    @Mapping(target = "options", source = "dto.answerList")
    MultiChoiceQuestionEntity mapToMultiChoicesQuestionEntity(MultiChoicesQuestionDto dto);

    @Mapping(target = "options", source = "dto.answerList")
    @Mapping(target = "entity.question.point", source = "dto.point")
    @Mapping(target = "entity.question.attachment", source = "dto.attachment")
    @Mapping(target = "entity.question.description", source = "dto.description")
    @Mapping(target = "entity.question.note", source = "dto.note")
    void mapToMultiChoicesQuestionEntity(@MappingTarget MultiChoiceQuestionEntity entity, MultiChoicesQuestionDto dto);

    @Mapping(target = "answerList", source = "entity.options")
    
    @Mapping(target = "point", source = "entity.question.point")
    @Mapping(target = "attachment", source = "entity.question.attachment")
    @Mapping(target = "note", source = "entity.question.note")
    @Mapping(target = "description", source = "entity.question.description")
    MultiChoicesQuestionDto mapToMultiChoicesQuestionDto(MultiChoiceQuestionEntity entity);


    @Mapping(target = "answerList", source = "entity.options", qualifiedByName = "IgnoreAnswer")
    
    @Mapping(target = "point", source = "entity.question.point")
    @Mapping(target = "attachment", source = "entity.question.attachment")
    @Mapping(target = "note", source = "entity.question.note")
    @Mapping(target = "description", source = "entity.question.description")
    MultiChoicesQuestionDto mapToMultiChoiceQuestionDtoIgnoreAnswer(MultiChoiceQuestionEntity entity);

    
    MultiChoiceQuestionAnswerDto mapToMultiChoiceQuestionAnswerDto(MultiChoiceQuestionEntity entity);
}
