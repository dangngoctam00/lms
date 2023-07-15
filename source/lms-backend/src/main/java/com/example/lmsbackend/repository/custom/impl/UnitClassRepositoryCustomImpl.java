package com.example.lmsbackend.repository.custom.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.example.lmsbackend.domain.classmodel.UnitClassEntity;
import com.example.lmsbackend.dto.response.course.UnitDto;
import com.example.lmsbackend.enums.AccountTypeEnum;
import com.example.lmsbackend.repository.custom.UnitClassRepositoryCustom;
import com.example.lmsbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static com.example.lmsbackend.constant.AppConstant.FETCH_GRAPH;

@RequiredArgsConstructor
public class UnitClassRepositoryCustomImpl implements UnitClassRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private final CriteriaBuilderFactory criteriaBuilderFactory;

    private final UserService userService;

    @Override
    public Optional<UnitClassEntity> findUnitFetchTextbookById(Long id) {
        var res = entityManager.createQuery("SELECT u from UnitClassEntity u WHERE u.id = (:id)", UnitClassEntity.class)
                .setParameter("id", id)
                .setHint(FETCH_GRAPH, entityManager.getEntityGraph("unit-class"))
                .getResultList();

        if (CollectionUtils.isEmpty(res)) {
            return Optional.empty();
        }
        return Optional.of(res.get(0));
    }

    @Override
    public void deleteAllTextbookByUnit(Long id) {
        entityManager.createQuery("DELETE FROM UnitClassTextBookEntity u WHERE u.id.unitId = (:id)")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public List<UnitClassEntity> findByUnitCourseId(Long unitCourseId) {
        return entityManager.createQuery("SELECT u FROM UnitClassEntity u LEFT JOIN FETCH u.textbooks WHERE u.unitCourse.id = (:id)", UnitClassEntity.class)
                .setParameter("id", unitCourseId)
                .setHint(FETCH_GRAPH, entityManager.getEntityGraph("unit-class"))
                .getResultList();
    }

    @Override
    public void deleteAllTextbookByUnitInCourseUnit(Long unitCourseId) {
        entityManager.createQuery("DELETE FROM UnitClassTextBookEntity u WHERE u.unit.id IN " +
                        "(SELECT unit FROM UnitClassEntity unit WHERE unit.unitCourse.id = (:id))")
                .setParameter("id", unitCourseId)
                .executeUpdate();
    }

    @Override
    public List<UnitDto> findUnitsByIdIn(List<Long> idList) {
        var currentUser = userService.getCurrentUser();
        if (currentUser.getAccountType() == AccountTypeEnum.STUDENT) {
            return entityManager.createQuery("SELECT new com.example.lmsbackend.dto.response.course.UnitDto(u.id, u.title, u.unitCourse.id, u.state) FROM UnitClassEntity u WHERE u.id IN (:idList) AND u.state = 'PUBLIC'")
                    .setParameter("idList", idList)
                    .getResultList();
        }
        return entityManager.createQuery("SELECT new com.example.lmsbackend.dto.response.course.UnitDto(u.id, u.title, u.unitCourse.id, u.state) FROM UnitClassEntity u WHERE u.id IN (:idList)")
                .setParameter("idList", idList)
                .getResultList();
    }
}
