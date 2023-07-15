package com.example.lmsbackend.repository.custom.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.example.lmsbackend.criteria.BaseSearchCriteria;
import com.example.lmsbackend.domain.QStaffEntity;
import com.example.lmsbackend.domain.StaffEntity;
import com.example.lmsbackend.repository.custom.StaffRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static com.example.lmsbackend.constant.AppConstant.DESC;

@RequiredArgsConstructor
public class StaffRepositoryCustomImpl implements StaffRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private final CriteriaBuilderFactory criteriaBuilderFactory;

    @Override
    public PagedList<StaffEntity> findAllStaffs(BaseSearchCriteria criteria) {
        var staff = QStaffEntity.staffEntity;
        var query = new BlazeJPAQuery<StaffEntity>(entityManager, criteriaBuilderFactory)
                .from(staff);
        if (StringUtils.isNoneBlank(criteria.getKeyword())) {
            query.where(staff.lastName.lower().concat(" ").concat(staff.firstName.lower()).containsIgnoreCase(criteria.getKeyword()));
        }
        if (CollectionUtils.isNotEmpty(criteria.getSorts())) {
            var sort = criteria.getSorts().get(0);
            if (StringUtils.equals(sort.getDirection(), DESC)) {
                query.orderBy(staff.firstName.desc());
            } else {
                query.orderBy(staff.firstName.asc());
            }
        }
        var pagination = criteria.getPagination();
        return query
                .orderBy(staff.id.desc())
                .select(staff)
                .fetchPage((pagination.getPage() - 1) * pagination.getSize(), pagination.getSize());
    }
}
