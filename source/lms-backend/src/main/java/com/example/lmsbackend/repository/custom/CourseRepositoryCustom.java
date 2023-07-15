package com.example.lmsbackend.repository.custom;

import com.blazebit.persistence.PagedList;
import com.example.lmsbackend.criteria.BaseSearchCriteria;
import com.example.lmsbackend.domain.coursemodel.CourseEntity;

import java.util.Optional;

public interface CourseRepositoryCustom {
    PagedList<CourseEntity> findCourses(BaseSearchCriteria criteria);

    Optional<CourseEntity> findAndFetchWithById(Long id, String fetchedProperty);

    Optional<CourseEntity> findFetchClassById(Long id);
}
