package com.example.lmsbackend.repository.custom;

import com.example.lmsbackend.domain.CourseProgramEntity;

import java.util.List;

public interface CourseProgramRepositoryCustom {
    List<CourseProgramEntity> getFetchCourseByProgramIdIn(List<Long> programsId);
}
