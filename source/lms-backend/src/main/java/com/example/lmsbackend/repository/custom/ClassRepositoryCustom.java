package com.example.lmsbackend.repository.custom;

import com.blazebit.persistence.PagedList;
import com.example.lmsbackend.criteria.BaseSearchCriteria;
import com.example.lmsbackend.domain.UserEntity;
import com.example.lmsbackend.domain.classmodel.ClassEntity;

import java.util.List;
import java.util.Optional;

public interface ClassRepositoryCustom {
    PagedList<ClassEntity> getClasses(BaseSearchCriteria criteria);

    PagedList<ClassEntity> getClassesByCourse(Long courseId, BaseSearchCriteria criteria);

    Optional<ClassEntity> findClassInformationById(Long id);

    Optional<ClassEntity> findFetchById(Long id, String properties);

    Optional<ClassEntity> findFetchPostsById(Long id);

    List<UserEntity> getMembers(Long classId, BaseSearchCriteria criteria);
}
