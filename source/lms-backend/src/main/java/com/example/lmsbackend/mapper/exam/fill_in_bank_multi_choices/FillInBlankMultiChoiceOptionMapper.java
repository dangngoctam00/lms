package com.example.lmsbackend.mapper.exam.fill_in_bank_multi_choices;

import com.example.lmsbackend.domain.exam.fill_in_blank_with_choices.FillInBlankMultiChoiceOptionEntity;
import com.example.lmsbackend.dto.exam.fill_in_blank_multi_choices.FillInBlankMultiChoicesOptionDto;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface FillInBlankMultiChoiceOptionMapper {

    FillInBlankMultiChoiceOptionEntity mapToFillInBlankMultiChoiceOptionEntity(FillInBlankMultiChoicesOptionDto dto);

    FillInBlankMultiChoicesOptionDto mapToFillInBlankMultiChoiceOptionDto(FillInBlankMultiChoiceOptionEntity source);
}
