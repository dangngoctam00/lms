package com.example.lmsbackend.dto.internal;

import com.example.lmsbackend.domain.coursemodel.CourseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoursesForPagination {
    private Long total;
    private List<CourseEntity> courses;
}
