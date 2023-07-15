package com.example.lmsbackend.repository.custom;

import com.example.lmsbackend.criteria.BaseSearchCriteria;
import com.example.lmsbackend.domain.ProgramEntity;

import java.util.List;
import java.util.Optional;

public interface ProgramRepositoryCustom {

    Optional<ProgramEntity> getAndFetchCourseById(Long id);

    List<ProgramEntity> getAndFetchCourseByIdIn(List<Long> id);

    PagedList<ProgramEntity> getPrograms(BaseSearchCriteria criteria);

    com.blazebit.persistence.PagedList<ProgramEntity> getPrograms();
}
