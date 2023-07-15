package com.example.lmsbackend.mapper.exam.multi_choice;

import com.example.lmsbackend.domain.exam.multi_choice.MultiChoiceOptionEntity;
import com.example.lmsbackend.dto.exam.multi_choice.MultiChoiceOptionAnswerDto;
import com.example.lmsbackend.dto.exam.multi_choice.MultiChoiceOptionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface MultiChoiceOptionMapper {

    MultiChoiceOptionEntity mapToMultiChoiceOptionEntity(MultiChoiceOptionDto dto);

    MultiChoiceOptionDto mapToMultiChoiceOptionDto(MultiChoiceOptionEntity entity);

    @Named("Answer")
    MultiChoiceOptionAnswerDto mapToMultiChoiceOptionAnswerDto(MultiChoiceOptionEntity entity);

    @Named("IgnoreAnswer")
    @Mapping(target = "isCorrect", ignore = true)
    MultiChoiceOptionDto mapToMultiChoiceOptionDtoIgnoreAnswer(MultiChoiceOptionEntity entity);
}
