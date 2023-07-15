package com.example.lmsbackend.mapper;

import com.example.lmsbackend.domain.classmodel.UnitClassTextBookEntity;
import com.example.lmsbackend.domain.resource.TextbookEntity;
import com.example.lmsbackend.dto.resource.TextbookDto;
import com.example.lmsbackend.dto.response.course.UnitDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Mapper(componentModel = "spring")
public interface TextBookMapper {
    TextbookDto mapToTextBookDto(TextbookEntity entity);

    TextbookEntity mapToTextBookEntity(TextbookDto dto);

    @Mapping(target = "id", ignore = true)
    void mapToTextBookEntity(@MappingTarget TextbookEntity entity, TextbookDto dto);

    @Mapping(target = "name", source = "entity.textbook.name")

    @Mapping(target = "textbookId", source = "entity.id.textbookId")
    UnitDto.TextbookInUnit mapToTextbookInUnitDto(UnitClassTextBookEntity entity);

    @Named("TextbookInUnit")
    default List<UnitDto.TextbookInUnit> mapTextbookInUnit(Set<UnitClassTextBookEntity> entity) {
        return entity.stream()
                .map(this::mapToTextbookInUnitDto)
                .collect(toList());
    }
}
