package com.example.lmsbackend.repository.custom.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.example.lmsbackend.config.security.aop.PermissionSecurity;
import com.example.lmsbackend.config.security.aop.caching.PermissionCaching;
import com.example.lmsbackend.criteria.BaseSearchCriteria;
import com.example.lmsbackend.criteria.SortCriterion;
import com.example.lmsbackend.domain.classmodel.QClassEntity;
import com.example.lmsbackend.domain.classmodel.QClassTeacherEntity;
import com.example.lmsbackend.domain.coursemodel.CourseEntity;
import com.example.lmsbackend.domain.coursemodel.CourseEntity.Fields;
import com.example.lmsbackend.domain.coursemodel.QCourseEntity;
import com.example.lmsbackend.enums.PermissionEnum;
import com.example.lmsbackend.repository.custom.CourseRepositoryCustom;
import com.example.lmsbackend.service.UserService;
import com.querydsl.jpa.JPAExpressions;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static com.example.lmsbackend.constant.AppConstant.DESC;
import static com.example.lmsbackend.constant.AppConstant.FETCH_GRAPH;
import static com.example.lmsbackend.constant.LimitValue.LIMIT;

@RequiredArgsConstructor
public class CourseRepositoryCustomImpl implements CourseRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private final CriteriaBuilderFactory criteriaBuilderFactory;

    private final PermissionCaching permissionCaching;

    private final UserService userService;


    @Override
    public PagedList<CourseEntity> findCourses(BaseSearchCriteria criteria) {
        var course = QCourseEntity.courseEntity;
        var classEntity = QClassEntity.classEntity;
        var classTeacher = QClassTeacherEntity.classTeacherEntity;
        var query = new BlazeJPAQuery<CourseEntity>(entityManager, criteriaBuilderFactory)
                .from(course);
        buildSearch(criteria, course, query);
        buildSort(criteria, query);

        buildLimitByTeaching(course, classEntity, classTeacher, query);

        query.orderBy(course.createdAt.desc())
                .orderBy(course.id.desc())
                .select(course);
        var pagination = criteria.getPagination();
        var actualCount = pagination.getSize() != 0 ? pagination.getSize() : 1000;
        return query.fetchPage((pagination.getPage() - 1) * pagination.getSize(), actualCount);
    }

    private void buildSort(BaseSearchCriteria criteria, BlazeJPAQuery<CourseEntity> query) {
        if (CollectionUtils.isNotEmpty(criteria.getSorts())) {
            criteria.getSorts()
                    .forEach(sortCriterion -> buildSortQuery(sortCriterion, query));
        }
    }

    private void buildSearch(BaseSearchCriteria criteria, QCourseEntity course, BlazeJPAQuery<CourseEntity> query) {
        if (StringUtils.isNoneBlank(criteria.getKeyword())) {
            query.where(course.name.containsIgnoreCase(criteria.getKeyword())
                    .or(course.code.containsIgnoreCase(criteria.getKeyword())));
        }
    }

    private void buildLimitByTeaching(QCourseEntity course, QClassEntity classEntity, QClassTeacherEntity classTeacher, BlazeJPAQuery<CourseEntity> query) {
        Long currentUserId = userService.getCurrentUserId();
        List<PermissionSecurity> permissions = permissionCaching.getByPermission(currentUserId, PermissionEnum.VIEW_ALL_COURSE);
        if (CollectionUtils.isEmpty(permissions)) {
            return;
        }
        boolean isLimitByTeaching = permissions.stream()
                .anyMatch(permission -> permission.getIsLimitByTeaching() == LIMIT);
        if (isLimitByTeaching) {
            var subQuery = JPAExpressions.select(classEntity.course.id)
                    .from(classEntity)
                    .innerJoin(classEntity.teachers, classTeacher)
                    .where(classTeacher.teacher.id.eq(currentUserId));
            query.where(course.id.in(subQuery));
        }
    }

    @Override
    public Optional<CourseEntity> findAndFetchWithById(Long id, String fetchedProperty) {
        var graph = getEntityGraph(fetchedProperty);
        var cb = entityManager.getCriteriaBuilder();

        var query = cb.createQuery(CourseEntity.class);
        var root = query.from(CourseEntity.class);
        var select = query.select(root);
        query.where(cb.equal(root.get(Fields.id), id));
        var typedQuery = entityManager.createQuery(select);
        typedQuery.setHint(FETCH_GRAPH, graph);
        var resultList = typedQuery.getResultList();
        if (CollectionUtils.isEmpty(resultList)) {
            return Optional.empty();
        }
        return Optional.of(resultList.get(0));
    }

    @Override
    public Optional<CourseEntity> findFetchClassById(Long id) {
        var res = entityManager.createQuery("SELECT c FROM CourseEntity c WHERE c.id = (:id)", CourseEntity.class)
                .setParameter("id", id)
                .setHint(FETCH_GRAPH, entityManager.getEntityGraph("course-full"))
                .getResultList();
        if (CollectionUtils.isEmpty(res)) {
            return Optional.empty();
        }
        return Optional.of(res.get(0));
    }

    private EntityGraph getEntityGraph(String property) {
        return entityManager.getEntityGraph(String.format("course-%s", property));
    }

    private void buildSortQuery(SortCriterion sortCriterion, BlazeJPAQuery<CourseEntity> query) {
        var course = QCourseEntity.courseEntity;
        if (sortCriterion.getField().equals(Fields.name)) {
            if (sortCriterion.getDirection().equals(DESC)) {
                query.orderBy(course.name.desc());
            } else {
                query.orderBy(course.name.asc());
            }
        } else if (sortCriterion.getField().equals(Fields.code)) {
            if (sortCriterion.getDirection().equals(DESC)) {
                query.orderBy(course.code.desc());
            } else {
                query.orderBy(course.code.asc());
            }
        } else if (sortCriterion.getField().equals(Fields.createdAt)) {
            if (sortCriterion.getDirection().equals(DESC)) {
                query.orderBy(course.createdAt.desc());
            } else {
                query.orderBy(course.createdAt.asc());
            }
        }
    }
}
