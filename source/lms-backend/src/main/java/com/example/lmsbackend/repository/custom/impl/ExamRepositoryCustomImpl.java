package com.example.lmsbackend.repository.custom.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.example.lmsbackend.criteria.BaseSearchCriteria;
import com.example.lmsbackend.domain.exam.ExamEntity;
import com.example.lmsbackend.domain.exam.QExamEntity;
import com.example.lmsbackend.enums.ExamState;
import com.example.lmsbackend.repository.custom.ExamRepositoryCustom;
import com.example.lmsbackend.repository.custom.PagedList;
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
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class ExamRepositoryCustomImpl implements ExamRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    private final CriteriaBuilderFactory criteriaBuilderFactory;

    @Override
    public Optional<ExamEntity> findByIdFetch(Long id, String properties) {
        var graph = getEntityGraph(properties);
        var cb = entityManager.getCriteriaBuilder();

        var query = cb.createQuery(ExamEntity.class);
        var root = query.from(ExamEntity.class);
        var select = query.select(root);
        query.where(cb.equal(root.get(ExamEntity.Fields.id), id));
        var typedQuery = entityManager.createQuery(select);
        if (graph != null) {
            typedQuery.setHint(FETCH_GRAPH, graph);
        }
        var resultList = typedQuery.getResultList();
        if (CollectionUtils.isEmpty(resultList)) {
            return Optional.empty();
        }
        return Optional.of(resultList.get(0));
    }

    @Override
    public PagedList<ExamEntity> findExamsByCriteria(Long courseId, BaseSearchCriteria criteria) {
        var exam = QExamEntity.examEntity;
        var query = new BlazeJPAQuery<ExamEntity>(entityManager, criteriaBuilderFactory)
                .from(exam)
                .leftJoin(exam.course)
                .fetchJoin();
        if (StringUtils.isNoneBlank(criteria.getKeyword())) {
            query.where(exam.title.containsIgnoreCase(criteria.getKeyword())
                    .or(exam.description.containsIgnoreCase(criteria.getKeyword())));
        }

        if (CollectionUtils.isNotEmpty(criteria.getFilters()) && criteria.getFilters().size() == 1) {
            var filterCriterion = criteria.getFilters().get(0)
                    .getValues()
                    .stream()
                    .map(f -> ExamState.valueOf(f))
                    .collect(toList());
            query.where(exam.state.in(filterCriterion));
        }

//        if (CollectionUtils.isNotEmpty(criteria.getFilters()) && criteria.getSorts().size() == 1) {
//            var sortCriterion = criteria.getSorts().get(0);
//            if (sortCriterion.getDirection().equals(DESC)) {
//                query.orderBy(exam.updatedAt.desc());
//            } else {
//                query.orderBy(exam.updatedAt.asc());
//            }
//        }
        if (criteria.getSorts().size() == 1) {
            var sortCriterion = criteria.getSorts().get(0);
            if (sortCriterion.getDirection().equals(DESC)) {
                query.orderBy(exam.updatedAt.desc());
            } else {
                query.orderBy(exam.updatedAt.asc());
            }
        } else {
            query.orderBy(exam.updatedAt.desc());
        }

        if (courseId != null) {
            query.where(exam.course.id.eq(courseId));
        }

        if (criteria.getPagination().getSize() == 0) {
            List<ExamEntity> fetch = query.select(exam)
                    .orderBy(exam.id.desc())
                    .fetch();
            return new PagedList<>(fetch, 0, 0, 0);
        }

        com.blazebit.persistence.PagedList<ExamEntity> examEntities = query.select(exam)
                .orderBy(exam.id.desc())
                .fetchPage((criteria.getPagination().getPage() - 1) * criteria.getPagination().getSize(), criteria.getPagination().getSize());
        return new PagedList<>(examEntities, examEntities.getTotalSize(), (criteria.getPagination().getPage() - 1) * criteria.getPagination().getSize(), criteria.getPagination().getSize());
    }

    private EntityGraph getEntityGraph(String properties) {
        if (StringUtils.isBlank(properties)) {
            return null;
        }
        return entityManager.getEntityGraph(String.format("exam-%s", properties));
    }
}
