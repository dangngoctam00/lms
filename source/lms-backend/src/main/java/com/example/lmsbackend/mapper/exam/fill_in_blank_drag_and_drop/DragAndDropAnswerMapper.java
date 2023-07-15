package com.example.lmsbackend.mapper.exam.fill_in_blank_drag_and_drop;

import com.example.lmsbackend.domain.exam.fill_in_blank_dnd_question.FillInBlankDragAndDropAnswerEntity;
import com.example.lmsbackend.dto.exam.fill_in_blank_drag_and_drop.FillInBlankDragAndDropAnswerDto;
import org.mapstruct.Mapper;

@Mapper
public interface DragAndDropAnswerMapper {

    FillInBlankDragAndDropAnswerEntity mapToDragAndDropAnswerEntity(FillInBlankDragAndDropAnswerDto dto);

    FillInBlankDragAndDropAnswerDto mapToDragAndDropAnswerDto(FillInBlankDragAndDropAnswerEntity entity);
}
