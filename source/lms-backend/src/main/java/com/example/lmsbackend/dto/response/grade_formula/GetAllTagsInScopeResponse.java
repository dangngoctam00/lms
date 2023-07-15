package com.example.lmsbackend.dto.response.grade_formula;

import com.example.lmsbackend.dto.response.BaseResponse;
import com.example.lmsbackend.dto.tag.TagDTO;
import lombok.Data;

import java.util.List;

@Data
public class GetAllTagsInScopeResponse extends BaseResponse {
    private List<TagDTO> tags;
}
