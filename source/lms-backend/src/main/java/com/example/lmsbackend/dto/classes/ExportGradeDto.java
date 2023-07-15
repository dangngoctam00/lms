package com.example.lmsbackend.dto.classes;

import lombok.Data;

import java.util.List;

@Data
public class ExportGradeDto {

    private List<Long> gradeTagsId;
}
