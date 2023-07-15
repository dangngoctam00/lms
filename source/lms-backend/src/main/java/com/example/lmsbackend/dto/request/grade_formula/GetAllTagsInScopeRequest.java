package com.example.lmsbackend.dto.request.grade_formula;

import com.example.lmsbackend.domain.exam.GradeTagScope;
import lombok.Data;

@Data
public class GetAllTagsInScopeRequest {
    private GradeTagScope scope;
    private Long scopeId;
}
