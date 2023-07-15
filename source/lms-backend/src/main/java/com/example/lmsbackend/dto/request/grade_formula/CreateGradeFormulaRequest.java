package com.example.lmsbackend.dto.request.grade_formula;

import com.example.lmsbackend.domain.exam.GradeTagScope;
import lombok.Data;

import java.util.List;

@Data
public class CreateGradeFormulaRequest {
    private String tagTitle;
    private String formula;
    private String expression;
    private GradeTagScope scope;
    private Long scopeId;
    private Boolean isPublic;
    private List<Long> useTags;
}
