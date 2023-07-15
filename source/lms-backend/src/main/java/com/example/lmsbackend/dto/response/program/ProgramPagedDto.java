package com.example.lmsbackend.dto.response.program;

import com.example.lmsbackend.dto.request.program.CourseInProgramDto;
import com.example.lmsbackend.dto.response.AbstractPagedDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProgramPagedDto extends AbstractPagedDto {

    private List<ProgramInList> listData;

    @Data
    public static class ProgramInList {
        private Long id;
        private String name;
        private String code;
        private Boolean isPublished;
        private LocalDateTime createdAt;
        private List<CourseInProgramDto> courses;
    }
}