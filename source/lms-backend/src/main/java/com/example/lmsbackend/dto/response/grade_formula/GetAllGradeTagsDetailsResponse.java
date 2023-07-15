package com.example.lmsbackend.dto.response.grade_formula;

import com.example.lmsbackend.dto.grade_formula.GradeTagDetailsDTO;
import com.example.lmsbackend.dto.response.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class GetAllGradeTagsDetailsResponse extends BaseResponse {
    private List<GradeTagDetailsDTO> tags;
}
