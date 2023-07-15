package com.example.lmsbackend.dto.response.course;

import com.example.lmsbackend.dto.response.AbstractPagedDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CoursesPagedDto extends AbstractPagedDto {
    private List<CourseInformationDto> listData;
}
