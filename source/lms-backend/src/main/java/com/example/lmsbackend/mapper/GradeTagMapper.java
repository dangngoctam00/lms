package com.example.lmsbackend.mapper;

import com.example.lmsbackend.domain.exam.GradeTag;
import com.example.lmsbackend.dto.grade_formula.GradeTagDetailsDTO;
import com.example.lmsbackend.dto.request.grade_formula.CreateGradeFormulaRequest;
import com.example.lmsbackend.dto.tag.TagDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GradeTagMapper {
    @Mapping(source = "tagTitle", target = "title")
    @Mapping(source = "isPublic", target = "public")
    GradeTag mapFromCreateGradeFormulaRequest(CreateGradeFormulaRequest request);

    GradeTagDetailsDTO mapToGrateTagDetailsDTO(GradeTag gradeTag);

    TagDTO mapToTagDTO(GradeTag entity);
}
