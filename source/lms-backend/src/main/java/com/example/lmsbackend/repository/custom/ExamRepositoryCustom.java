package com.example.lmsbackend.repository.custom;

import com.example.lmsbackend.criteria.BaseSearchCriteria;
import com.example.lmsbackend.domain.exam.ExamEntity;

import java.util.Optional;

public interface ExamRepositoryCustom {
    Optional<ExamEntity> findByIdFetch(Long id, String properties);

    PagedList<ExamEntity> findExamsByCriteria(Long courseId, BaseSearchCriteria criteria);
}
