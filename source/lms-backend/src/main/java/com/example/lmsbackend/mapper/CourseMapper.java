package com.example.lmsbackend.mapper;

import com.example.lmsbackend.domain.coursemodel.CourseEntity;
import com.example.lmsbackend.dto.request.course.CourseDto;
import com.example.lmsbackend.dto.response.course.CourseInformationDto;
import com.example.lmsbackend.mapper.exam.AuditMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {
        AuditMapper.class
})
public interface CourseMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    CourseEntity mapToCourseEntity(CourseDto courseDTO);

    CourseDto mapToCourseDto(CourseEntity courseEntity);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void mapToCourse(@MappingTarget CourseEntity entity, CourseDto dto);

    CourseInformationDto mapToCourseInformation(CourseEntity entity);
}
