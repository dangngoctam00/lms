package com.example.lmsbackend.mapper.exam.fill_in_bank_multi_choices;

import com.example.lmsbackend.domain.exam.fill_in_blank_with_choices.FillInBlankMultiChoiceBlankEntity;
import com.example.lmsbackend.dto.exam.fill_in_blank_multi_choices.FillInBlankMultiChoicesBlankAnswerDto;
import com.example.lmsbackend.dto.exam.fill_in_blank_multi_choices.FillInBlankMultiChoicesBlankDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toSet;

@Mapper(componentModel = "spring", uses = {
        FillInBlankMultiChoiceOptionMapper.class
})
public interface FillInBlankMultiChoiceBlankMapper {

    @Mapping(target = "options", source = "dto.answerList")
    FillInBlankMultiChoiceBlankEntity mapToFillInBlankMultiChoiceBlankEntity(FillInBlankMultiChoicesBlankDto dto);

    @Mapping(target = "answerList", source = "entity.options")
    FillInBlankMultiChoicesBlankDto mapToFillInBlankMultiChoiceBlankDto(FillInBlankMultiChoiceBlankEntity entity);

    @Named("Answer")
    FillInBlankMultiChoicesBlankAnswerDto mapToFillInBlankMultiChoiceBlankAnswerDto(FillInBlankMultiChoiceBlankEntity entity);

    @Named("IgnoreAnswer")
    @Mapping(target = "answerList", source = "entity.options")
    @Mapping(target = "correctAnswerKey", ignore = true)
    FillInBlankMultiChoicesBlankDto mapToFillInBlankMultiChoiceBlankDtoIgnoreAnswer(FillInBlankMultiChoiceBlankEntity entity);

    @Named("InOrder")
    default Set<FillInBlankMultiChoiceBlankEntity> mapBlanks(List<FillInBlankMultiChoicesBlankDto> dto) {
        return IntStream.range(0, dto.size())
                .mapToObj(i -> {
                    var item = mapToFillInBlankMultiChoiceBlankEntity(dto.get(i));
                    item.setOrder(i + 1);
                    return item;
                })
                .collect(toSet());
    }
}
