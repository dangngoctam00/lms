package com.example.lmsbackend.dto.response.grade_formula;

import com.example.lmsbackend.dto.response.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class GetGradeFormulaResponse extends BaseResponse {
    private Long id;
    private String tagTitle;
    private String formula;
    private boolean isPublic;
    private List<Long> useTags;
}
