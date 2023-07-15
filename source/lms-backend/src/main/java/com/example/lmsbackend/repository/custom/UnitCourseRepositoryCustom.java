package com.example.lmsbackend.repository.custom;

import com.example.lmsbackend.domain.coursemodel.UnitCourseEntity;

import java.util.Optional;

public interface UnitCourseRepositoryCustom {

    Optional<UnitCourseEntity> findUnitFetchTextbookById(Long id);

    void deleteAllByUnit(Long id);
}
