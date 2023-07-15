package com.example.lmsbackend.mapper.exam.fill_in_blank;

import com.example.lmsbackend.domain.exam.fill_in_blank.FillInBlankOptionEntity;
import com.example.lmsbackend.dto.exam.fill_in_blank.FillInBlankOptionAnswerDto;
import com.example.lmsbackend.dto.exam.fill_in_blank.FillInBlankOptionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toSet;

@Mapper(componentModel = "spring")
public interface FillInBlankOptionMapper {

    FillInBlankOptionEntity mapToFillInBlankOptionEntity(FillInBlankOptionDto dto);

    FillInBlankOptionDto mapToFillInBlankOptionDto(FillInBlankOptionEntity entity);

    @Named("Answer")
    FillInBlankOptionAnswerDto mapToFillInBlankBlankAnswerDto(FillInBlankOptionEntity entity);

    @Named("IgnoreAnswer")
    @Mapping(target = "expectedAnswer", ignore = true)
    FillInBlankOptionDto mapToFillInBlankOptionDtoIgnoreAnswer(FillInBlankOptionEntity entity);

    @Named("InOrder")
    default Set<FillInBlankOptionEntity> mapBlanks(List<FillInBlankOptionDto> dto) {
        return IntStream.range(0, dto.size())
                .mapToObj(i -> {
                    var item = mapToFillInBlankOptionEntity(dto.get(i));
                    item.setOrder(i + 1);
                    return item;
                })
                .collect(toSet());
    }
}
