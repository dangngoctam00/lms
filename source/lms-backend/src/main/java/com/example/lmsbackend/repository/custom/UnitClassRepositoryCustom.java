package com.example.lmsbackend.repository.custom;

import com.example.lmsbackend.domain.classmodel.UnitClassEntity;
import com.example.lmsbackend.dto.response.course.UnitDto;

import java.util.List;
import java.util.Optional;

public interface UnitClassRepositoryCustom {

    Optional<UnitClassEntity> findUnitFetchTextbookById(Long id);

    List<UnitClassEntity> findByUnitCourseId(Long unitCourseId);

    void deleteAllTextbookByUnit(Long id);

    void deleteAllTextbookByUnitInCourseUnit(Long courseId);

    List<UnitDto> findUnitsByIdIn(List<Long> idList);
}
