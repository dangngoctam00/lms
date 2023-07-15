package com.example.lmsbackend.repository.custom;

import com.blazebit.persistence.PagedList;
import com.example.lmsbackend.criteria.BaseSearchCriteria;
import com.example.lmsbackend.dto.classes.MemberDto;

public interface ClassStudentRepositoryCustom {

    PagedList<MemberDto> getStudents(Long classId, BaseSearchCriteria criteria);
}
