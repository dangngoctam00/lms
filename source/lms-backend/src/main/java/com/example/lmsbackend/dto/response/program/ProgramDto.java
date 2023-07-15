package com.example.lmsbackend.dto.response.program;

import com.example.lmsbackend.dto.request.program.CourseInProgramDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProgramDto {
    private Long id;
    private String name;
    private String code;
    private String description;
    private Boolean isStrict;
    private Boolean isPublished;
    private List<CourseInProgramDto> courses;
}
