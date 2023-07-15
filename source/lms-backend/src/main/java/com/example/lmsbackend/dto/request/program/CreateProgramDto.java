package com.example.lmsbackend.dto.request.program;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateProgramDto {
    private String name;
    private String code;
    private String description;
    private Boolean isStrict;
    private Boolean isPublished;
    private List<CourseInProgramDto> courses;
}
