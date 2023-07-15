package com.example.lmsbackend.mapper;

import com.example.lmsbackend.domain.exam.GradeFormulaEntity;
import com.example.lmsbackend.dto.request.grade_formula.CreateGradeFormulaRequest;
import com.example.lmsbackend.dto.request.grade_formula.UpdateGradeFormulaRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GradeFormulaMapper {

    @Mapping(target = "useTags", ignore = true)
    GradeFormulaEntity mapFromCreateGradeFormulaRequest(CreateGradeFormulaRequest request);

    @Mapping(target = "useTags", ignore = true)
    GradeFormulaEntity mapFromUpdateGradeFormulaRequest(UpdateGradeFormulaRequest request);
}
