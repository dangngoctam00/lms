package com.example.lmsbackend.repository.custom.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.example.lmsbackend.config.security.aop.PermissionSecurity;
import com.example.lmsbackend.config.security.aop.caching.PermissionCaching;
import com.example.lmsbackend.criteria.BaseSearchCriteria;
import com.example.lmsbackend.criteria.FilterCriterion;
import com.example.lmsbackend.criteria.SortCriterion;
import com.example.lmsbackend.domain.QStaffEntity;
import com.example.lmsbackend.domain.QStudentEntity;
import com.example.lmsbackend.domain.QUserEntity;
import com.example.lmsbackend.domain.UserEntity;
import com.example.lmsbackend.domain.classmodel.ClassEntity;
import com.example.lmsbackend.domain.classmodel.ClassEntity.Fields;
import com.example.lmsbackend.domain.classmodel.QClassEntity;
import com.example.lmsbackend.domain.classmodel.QClassStudentEntity;
import com.example.lmsbackend.domain.classmodel.QClassTeacherEntity;
import com.example.lmsbackend.enums.AccountTypeEnum;
import com.example.lmsbackend.enums.ClassStatus;
import com.example.lmsbackend.enums.ClassType;
import com.example.lmsbackend.enums.PermissionEnum;
import com.example.lmsbackend.repository.custom.ClassRepositoryCustom;
import com.example.lmsbackend.service.UserService;
import com.querydsl.jpa.JPAExpressions;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;

import static com.example.lmsbackend.constant.AppConstant.DESC;
import static com.example.lmsbackend.constant.AppConstant.FETCH_GRAPH;
import static com.example.lmsbackend.constant.LimitValue.LIMIT;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class ClassRepositoryCustomImpl implements ClassRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private final CriteriaBuilderFactory criteriaBuilderFactory;

    private final UserService userService;
    private final PermissionCaching permissionCaching;

    @Override
    public PagedList<ClassEntity> getClasses(BaseSearchCriteria criteria) {
        var classEntity = QClassEntity.classEntity;
        var query = new BlazeJPAQuery<ClassEntity>(entityManager, criteriaBuilderFactory)
                .from(classEntity);
        buildSearch(criteria, classEntity, query);
        buildSort(criteria, query);
        buildFilter(criteria, query);

        buildLimit(classEntity, query);

        return query
                .leftJoin(classEntity.course)
                .fetchJoin()
                .select(classEntity)
                .orderBy(classEntity.id.desc())
                .orderBy(classEntity.createdAt.desc())
                .fetchPage((criteria.getPagination().getPage() - 1) * criteria.getPagination().getSize(), criteria.getPagination().getSize());
    }

    @Override
    public PagedList<ClassEntity> getClassesByCourse(Long courseId, BaseSearchCriteria criteria) {
        var classEntity = QClassEntity.classEntity;
        var query = new BlazeJPAQuery<ClassEntity>(entityManager, criteriaBuilderFactory)
                .from(classEntity);
        buildSearch(criteria, classEntity, query);
        buildSort(criteria, query);
        buildFilter(criteria, query);

        buildLimit(classEntity, query);
        query.where(classEntity.course.id.eq(courseId));
        return query
                .leftJoin(classEntity.course)
                .fetchJoin()
                .select(classEntity)
                .orderBy(classEntity.id.desc())
                .orderBy(classEntity.createdAt.desc())
                .fetchPage((criteria.getPagination().getPage() - 1) * criteria.getPagination().getSize(), criteria.getPagination().getSize());
    }

    @Override
    public List<UserEntity> getMembers(Long classId, BaseSearchCriteria criteria) {
        var user = QUserEntity.userEntity;
        var student = QStudentEntity.studentEntity;
        var teacher = QStaffEntity.staffEntity;
        var classStudent = QClassStudentEntity.classStudentEntity;
        var classTeacher = QClassTeacherEntity.classTeacherEntity;
        var query = new BlazeJPAQuery<ClassEntity>(entityManager, criteriaBuilderFactory)
                .from(user);
        if (StringUtils.isNoneBlank(criteria.getKeyword())) {
            query.where(classStudent.student.lastName.concat(" ").concat(classStudent.student.firstName).containsIgnoreCase(criteria.getKeyword().toLowerCase())
                    .or(classTeacher.teacher.lastName.concat(" ").concat(classTeacher.teacher.firstName).containsIgnoreCase(criteria.getKeyword().toLowerCase())));
        }
        return query.leftJoin(user, student._super).on(student.id.eq(user.id))
                .leftJoin(user, teacher._super).on(teacher.id.eq(user.id))
                .leftJoin(classStudent).on(student.id.eq(classStudent.student.id))
                .leftJoin(classTeacher).on(teacher.id.eq(classTeacher.teacher.id))
                .where(classTeacher.classEntity.id.eq(classId).or(classStudent.classEntity.id.eq(classId)))
                .select(user)
                .orderBy(user.id.desc())
                .createQuery()
                .getResultList();
    }

    private void buildLimit(QClassEntity classEntity, BlazeJPAQuery<ClassEntity> query) {
        var currentUser = userService.getCurrentUser();
        Long currentUserId = userService.getCurrentUserId();
        List<PermissionSecurity> permissions = permissionCaching.getByPermission(currentUserId, PermissionEnum.VIEW_LIST_CLASS);
        if (CollectionUtils.isEmpty(permissions)) {
            return;
        }
        if (currentUser.getAccountType() == AccountTypeEnum.STAFF) {
            boolean isLimitByTeaching = permissions.stream()
                    .anyMatch(permission -> permission.getIsLimitByTeaching() == LIMIT);
            if (isLimitByTeaching) {
                var classTeacherEntity = QClassTeacherEntity.classTeacherEntity;
                var subQuery = JPAExpressions.select(classTeacherEntity.classEntity.id)
                        .from(classTeacherEntity)
                        .where(classTeacherEntity.teacher.id.eq(currentUserId));
                query.where(classEntity.id.in(subQuery));
            }
        } else {
            boolean isLimitByLearning = permissions.stream()
                    .anyMatch(permission -> permission.getIsLimitByTeaching() == LIMIT);
            if (isLimitByLearning) {
                var classStudentEntity = QClassStudentEntity.classStudentEntity;
                var subQuery = JPAExpressions.select(classStudentEntity.classEntity.id)
                        .from(classStudentEntity)
                        .where(classStudentEntity.student.id.eq(currentUserId));
                query.where(classEntity.id.in(subQuery));
            }
        }
    }


    private void buildFilter(BaseSearchCriteria criteria, BlazeJPAQuery<ClassEntity> query) {
        if (CollectionUtils.isNotEmpty(criteria.getFilters())) {
            criteria.getFilters()
                    .forEach(filterCriterion -> buildFilterQuery(filterCriterion, query));
        }
    }

    private void buildSort(BaseSearchCriteria criteria, BlazeJPAQuery<ClassEntity> query) {
        if (CollectionUtils.isNotEmpty(criteria.getSorts())) {
            criteria.getSorts()
                    .forEach(sortCriterion -> buildSortQuery(sortCriterion, query));
        }
    }

    private void buildSearch(BaseSearchCriteria criteria, QClassEntity classEntity, BlazeJPAQuery<ClassEntity> query) {
        if (StringUtils.isNoneBlank(criteria.getKeyword())) {
            query.where(classEntity.name.containsIgnoreCase(criteria.getKeyword())
                    .or(classEntity.code.containsIgnoreCase(criteria.getKeyword())));
        }
    }

    @Override
    public Optional<ClassEntity> findClassInformationById(Long id) {
        var classEntity = QClassEntity.classEntity;
        var query = new BlazeJPAQuery<ClassEntity>(entityManager, criteriaBuilderFactory)
                .from(classEntity)
                .leftJoin(classEntity.course)
                .fetchJoin()
                .where(classEntity.id.eq(id))
                .select(classEntity)
                .fetchFirst();
        return Optional.ofNullable(query);
    }

    @Override
    public Optional<ClassEntity> findFetchById(Long id, String properties) {
        var cb = entityManager.getCriteriaBuilder();
        var query = cb.createQuery(ClassEntity.class);
        var root = query.from(ClassEntity.class);
        query.select(root);
        query.where(cb.equal(root.get(Fields.id), id));
        var typedQuery = entityManager.createQuery(query);
        typedQuery.setHint(FETCH_GRAPH, getGraph(properties));
        var res = (typedQuery.getResultList());
        if (CollectionUtils.isEmpty(res)) {
            return Optional.empty();
        }
        return Optional.of(res.get(0));
    }

    private TypedQuery<ClassEntity> byIdCriteriaBuilder(CriteriaBuilder cb, Long id) {
        var query = cb.createQuery(ClassEntity.class);
        var root = query.from(ClassEntity.class);
        query.select(root);
        query.where(cb.equal(root.get(Fields.id), id));
        return entityManager.createQuery(query);
    }

    private Optional<ClassEntity> byIdGetResult(TypedQuery<ClassEntity> query) {
        var res = (query.getResultList());
        if (CollectionUtils.isEmpty(res)) {
            return Optional.empty();
        }
        return Optional.of(res.get(0));
    }

    private EntityGraph<?> getGraph(String properties) {
        return entityManager.getEntityGraph(String.format("class-%s", properties));
    }

    @Override
    public Optional<ClassEntity> findFetchPostsById(Long id) {
        var cb = entityManager.getCriteriaBuilder();
        var typedQuery = byIdCriteriaBuilder(cb, id);
        typedQuery.setHint(FETCH_GRAPH, entityManager.getEntityGraph("class-posts"));
        return byIdGetResult(typedQuery);
    }

    private void buildSortQuery(SortCriterion sortCriterion, BlazeJPAQuery<ClassEntity> query) {
        var classEntity = QClassEntity.classEntity;
        if (sortCriterion.getField().equals(Fields.name)) {
            if (sortCriterion.getDirection().equals(DESC)) {
                query.orderBy(classEntity.name.desc());
            } else {
                query.orderBy(classEntity.name.asc());
            }
        } else if (sortCriterion.getField().equals(Fields.code)) {
            if (sortCriterion.getDirection().equals(DESC)) {
                query.orderBy(classEntity.code.desc());
            } else {
                query.orderBy(classEntity.code.asc());
            }
        } else if (sortCriterion.getField().equals(Fields.startedAt)) {
            if (sortCriterion.getDirection().equals(DESC)) {
                query.orderBy(classEntity.createdAt.desc());
            } else {
                query.orderBy(classEntity.createdAt.asc());
            }
        }
    }

    private void buildFilterQuery(FilterCriterion filterCriterion, BlazeJPAQuery<ClassEntity> query) {
        var classEntity = QClassEntity.classEntity;
        if (filterCriterion.getField().equals(Fields.status)) {
            query.where(classEntity
                    .status.in(filterCriterion.getValues()
                            .stream()
                            .map(ClassStatus::valueOf)
                            .collect(toList())));
        } else if (filterCriterion.getField().equals(Fields.type)) {
            query.where(classEntity
                    .type.in(filterCriterion.getValues()
                            .stream()
                            .map(ClassType::valueOf)
                            .collect(toList())));
        }
    }
}
