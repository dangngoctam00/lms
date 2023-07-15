package com.example.lmsbackend.repository.custom;

import com.blazebit.persistence.PagedList;
import com.example.lmsbackend.criteria.BaseSearchCriteria;
import com.example.lmsbackend.domain.StaffEntity;

public interface StaffRepositoryCustom {

    PagedList<StaffEntity> findAllStaffs(BaseSearchCriteria criteria);
}
