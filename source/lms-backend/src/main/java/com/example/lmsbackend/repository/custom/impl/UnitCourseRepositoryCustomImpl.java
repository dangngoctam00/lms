package com.example.lmsbackend.repository.custom.impl;

import com.example.lmsbackend.constant.AppConstant;
import com.example.lmsbackend.domain.coursemodel.UnitCourseEntity;
import com.example.lmsbackend.repository.custom.UnitCourseRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@RequiredArgsConstructor
public class UnitCourseRepositoryCustomImpl implements UnitCourseRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<UnitCourseEntity> findUnitFetchTextbookById(Long id) {
        var res = entityManager.createQuery("SELECT u from UnitCourseEntity u WHERE u.id = (:id)", UnitCourseEntity.class)
                .setParameter("id", id)
                .setHint(AppConstant.FETCH_GRAPH, entityManager.getEntityGraph("unit-course"))
                .getResultList();

        if (CollectionUtils.isEmpty(res)) {
            return Optional.empty();
        }
        return Optional.of(res.get(0));
    }

    @Override
    public void deleteAllByUnit(Long id) {
        entityManager.createQuery("DELETE FROM UnitCourseTextBookEntity u WHERE u.id.unitId = (:id)")
                .setParameter("id", id)
                .executeUpdate();
    }
}
