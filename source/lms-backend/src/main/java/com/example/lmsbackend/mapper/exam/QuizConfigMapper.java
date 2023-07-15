package com.example.lmsbackend.mapper.exam;

import com.example.lmsbackend.domain.exam.QuizConfigEntity;
import com.example.lmsbackend.dto.exam.QuizConfigDto;
import org.apache.commons.lang3.BooleanUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface QuizConfigMapper {

    @Mapping(target = "timeLimit", expression = "java(mapTimeLimit(dto))")
    QuizConfigEntity mapToConfigEntity(QuizConfigDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "quiz", ignore = true)
    @Mapping(target = "timeLimit", expression = "java(mapTimeLimit(source))")
    void mapToConfigEntity(@MappingTarget QuizConfigEntity target, QuizConfigEntity source);

    QuizConfigDto mapToConfigDto(QuizConfigEntity entity);

    default Long mapTimeLimit(QuizConfigEntity source) {
        if (BooleanUtils.isFalse(source.getHaveTimeLimit())) {
            return null;
        }
        return source.getTimeLimit();
    }

    default Long mapTimeLimit(QuizConfigDto source) {
        if (BooleanUtils.isFalse(source.getHaveTimeLimit())) {
            return null;
        }
        return source.getTimeLimit();
    }
}
