package com.example.lmsbackend.mapper;

import com.example.lmsbackend.domain.classmodel.UnitClassEntity;
import com.example.lmsbackend.domain.classmodel.UnitClassTextBookEntity;
import com.example.lmsbackend.domain.coursemodel.UnitCourseEntity;
import com.example.lmsbackend.domain.coursemodel.UnitCourseTextBookEntity;
import com.example.lmsbackend.dto.response.course.UnitDto;
import com.example.lmsbackend.dto.unit.UnitItemDto;
import org.mapstruct.*;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring", uses = TextBookMapper.class)
public interface UnitMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

    @Mapping(nullValuePropertyMappingStrategy = IGNORE, target = "id", qualifiedByName = "mapUnitId")
    @Mapping(target = "textbooks", ignore = true)
    UnitCourseEntity mapToUnitEntity(UnitDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "textbooks", ignore = true)
    void mapToUnitEntity(@MappingTarget UnitCourseEntity entity, UnitDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "textbooks", ignore = true)
    void mapToUnitEntity(@MappingTarget UnitClassEntity entity, UnitDto dto);

    @Mapping(nullValuePropertyMappingStrategy = IGNORE, target = "id", qualifiedByName = "mapUnitId")
    @Mapping(target = "textbooks", ignore = true)
    UnitClassEntity mapToUnitClassEntity(UnitDto dto);

    UnitDto mapToUnitDto(UnitCourseEntity unitCourseEntity);

    @Mapping(target = "textbooks", source = "textbooks", qualifiedByName = "TextbookInUnit")
    @Mapping(target = "isFromCourse", expression = "java(checkBelongsCourse(unitClassEntity))")
    UnitDto mapToUnitDto(UnitClassEntity unitClassEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "textbooks", ignore = true)
    UnitClassEntity mapToUnitClass(UnitCourseEntity entity);

    @Mapping(target = "unit", ignore = true)
    UnitClassTextBookEntity mapToUnitClassTextbook(UnitCourseTextBookEntity unitTextbook);

    UnitItemDto mapToUnitItem(UnitClassEntity entity);

    @Named(value = "mapUnitId")
    default Long mapUnitId(Long id) {
        if (id == null || id <= 0) return null;
        return id;
    }

    default Boolean checkBelongsCourse(UnitClassEntity entity) {
        if (entity.getUnitCourse() != null) {
            return true;
        }
        return false;
    }
}
