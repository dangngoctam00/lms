package com.example.lmsbackend.mapper.exam.fill_in_blank_drag_and_drop;

import com.example.lmsbackend.domain.exam.fill_in_blank_dnd_question.FillInBlankDragAndDropQuestionEntity;
import com.example.lmsbackend.dto.exam.fill_in_blank_drag_and_drop.FillInBlankDragAndDropQuestionAnswerDto;
import com.example.lmsbackend.dto.exam.fill_in_blank_drag_and_drop.FillInBlankDragAndDropQuestionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {
        DragAndDropBlankMapper.class,
        DragAndDropAnswerMapper.class
})
public interface DragAndDropQuestionMapper {

    @Mapping(target = "answers", source = "dto.answerList")
    @Mapping(target = "blanks", source = "dto.blankList", qualifiedByName = "InOrder")
    FillInBlankDragAndDropQuestionEntity mapToDragAndDropQuestionEntity(FillInBlankDragAndDropQuestionDto dto);

    @Mapping(target = "answers", source = "dto.answerList")
    @Mapping(target = "blanks", source = "dto.blankList", qualifiedByName = "InOrder")
    @Mapping(target = "entity.question.point", source = "dto.point")
    @Mapping(target = "entity.question.attachment", source = "dto.attachment")
    @Mapping(target = "entity.question.description", source = "dto.description")
    @Mapping(target = "entity.question.note", source = "dto.note")
    void mapToDragAndDropQuestionEntity(@MappingTarget FillInBlankDragAndDropQuestionEntity entity, FillInBlankDragAndDropQuestionDto dto);

    @Mapping(target = "answerList", source = "entity.answers")
    @Mapping(target = "blankList", source = "entity.blanks")
    
    @Mapping(target = "point", source = "entity.question.point")
    @Mapping(target = "attachment", source = "entity.question.attachment")
    @Mapping(target = "note", source = "entity.question.note")
    @Mapping(target = "description", source = "entity.question.description")
    FillInBlankDragAndDropQuestionDto mapToDragAndDropQuestionDto(FillInBlankDragAndDropQuestionEntity entity);

    @Mapping(target = "answerList", source = "entity.answers")
    @Mapping(target = "blankList", source = "entity.blanks", qualifiedByName = "IgnoreAnswer")
    
    @Mapping(target = "point", source = "entity.question.point")
    @Mapping(target = "attachment", source = "entity.question.attachment")
    @Mapping(target = "note", source = "entity.question.note")
    @Mapping(target = "description", source = "entity.question.description")
    FillInBlankDragAndDropQuestionDto mapToDragAndDropQuestionDtoIgnoreAnswer(FillInBlankDragAndDropQuestionEntity entity);

    
    FillInBlankDragAndDropQuestionAnswerDto mapToFillInBlankDragAndDropQuestionAnswerDto(FillInBlankDragAndDropQuestionEntity entity);
}
