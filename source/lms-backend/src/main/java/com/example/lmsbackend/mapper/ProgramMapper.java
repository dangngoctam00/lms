package com.example.lmsbackend.mapper;

import com.example.lmsbackend.domain.CourseProgramEntity;
import com.example.lmsbackend.domain.CourseProgramKey;
import com.example.lmsbackend.domain.ProgramEntity;
import com.example.lmsbackend.dto.request.program.CourseInProgramDto;
import com.example.lmsbackend.dto.request.program.CreateProgramDto;
import com.example.lmsbackend.dto.request.program.UpdateProgramDto;
import com.example.lmsbackend.dto.response.program.ProgramDto;
import com.example.lmsbackend.dto.response.program.ProgramPagedDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Mapper(componentModel = "spring", imports = {
        CourseProgramKey.class
})
public interface ProgramMapper {


    ProgramPagedDto.ProgramInList mapToProgramInListDto(ProgramEntity entity);

    @Mapping(target = "courses", ignore = true)
    ProgramEntity mapToProgramEntity(CreateProgramDto dto);

    ProgramDto mapToProgramDto(ProgramEntity entity);

    @Mapping(target = "id", source = "entity.course.id")
    @Mapping(target = "name", source = "entity.course.name")
    @Mapping(target = "code", source = "entity.course.code")
//    @Mapping(target = "courses", qualifiedByName = "mapCoursesInProgram")
    CourseInProgramDto mapToCourseInProgram(CourseProgramEntity entity);

    @Mapping(target = "courses", ignore = true)
    @Mapping(target = "id", ignore = true)
    void mapUpdatedProgram(@MappingTarget ProgramEntity entity, UpdateProgramDto dto);

    @Named("mapCoursesInProgram")
    default List<String> mapCoursesInProgram(Set<CourseProgramEntity> entity) {
        return entity.stream()
                .map(e -> e.getCourse().getName())
                .collect(toList());
    }
}
