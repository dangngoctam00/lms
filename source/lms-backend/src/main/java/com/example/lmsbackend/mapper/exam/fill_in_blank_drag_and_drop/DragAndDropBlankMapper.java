package com.example.lmsbackend.mapper.exam.fill_in_blank_drag_and_drop;

import com.example.lmsbackend.domain.exam.fill_in_blank_dnd_question.FillInBlankDragAndDropBlankEntity;
import com.example.lmsbackend.dto.exam.fill_in_blank_drag_and_drop.FillInBlankDragAndDropBlankAnswerDto;
import com.example.lmsbackend.dto.exam.fill_in_blank_drag_and_drop.FillInBlankDragAndDropBlankDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toSet;

@Mapper
public interface DragAndDropBlankMapper {

    FillInBlankDragAndDropBlankEntity mapToDragAndDropBlankEntity(FillInBlankDragAndDropBlankDto dto);

    FillInBlankDragAndDropBlankDto mapToDragAndDropBlankDto(FillInBlankDragAndDropBlankEntity entity);

    @Named("IgnoreAnswer")
    @Mapping(target = "answerKey", ignore = true)
    FillInBlankDragAndDropBlankDto mapToDragAndDropBlankDtoIgnoreAnswer(FillInBlankDragAndDropBlankEntity entity);

    @Named("Answer")
    FillInBlankDragAndDropBlankAnswerDto mapToFillInBlankDragAndDropBlankAnswerDto(FillInBlankDragAndDropBlankEntity entity);

    @Named("InOrder")
    default Set<FillInBlankDragAndDropBlankEntity> mapBlanks(List<FillInBlankDragAndDropBlankDto> dto) {
        return IntStream.range(0, dto.size())
                .mapToObj(i -> {
                    var item = mapToDragAndDropBlankEntity(dto.get(i));
                    item.setOrder(i + 1);
                    return item;
                })
                .collect(toSet());
    }
}
