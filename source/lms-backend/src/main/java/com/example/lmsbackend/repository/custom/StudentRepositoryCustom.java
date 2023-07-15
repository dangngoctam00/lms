package com.example.lmsbackend.repository.custom;

import com.blazebit.persistence.PagedList;
import com.example.lmsbackend.criteria.BaseSearchCriteria;
import com.example.lmsbackend.domain.StudentEntity;

public interface StudentRepositoryCustom {
    PagedList<StudentEntity> getStudents(BaseSearchCriteria criteria);
}
