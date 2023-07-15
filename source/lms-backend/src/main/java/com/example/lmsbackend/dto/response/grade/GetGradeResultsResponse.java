package com.example.lmsbackend.dto.response.grade;

import com.example.lmsbackend.dto.response.BaseResponse;
import com.example.lmsbackend.dto.tag.TagDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class GetGradeResultsResponse extends BaseResponse {
    private List<TagDTO> tags;
    private List<GradeResult> gradeResults;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class GradeResult {
        private Long studentId;
        private String studentName;
        private String avatar;
        private List<Grade> grades;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Grade {
        private Long tagId;
        private String tagTitle;
        private Double grade;
    }
}
