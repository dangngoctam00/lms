package com.example.lmsbackend.repository.custom.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.example.lmsbackend.criteria.BaseSearchCriteria;
import com.example.lmsbackend.domain.CourseProgramEntity;
import com.example.lmsbackend.domain.ProgramEntity;
import com.example.lmsbackend.domain.ProgramEntity.Fields;
import com.example.lmsbackend.domain.QCourseProgramEntity;
import com.example.lmsbackend.domain.QProgramEntity;
import com.example.lmsbackend.domain.coursemodel.QCourseEntity;
import com.example.lmsbackend.repository.custom.ProgramRepositoryCustom;
import com.querydsl.core.types.Projections;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.lmsbackend.constant.AppConstant.FETCH_GRAPH;
import static com.example.lmsbackend.repository.custom.utils.BuildJPAQueryCriteria.buildSortQuery;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.set;

@RequiredArgsConstructor
public class ProgramRepositoryCustomImpl implements ProgramRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private final CriteriaBuilderFactory criteriaBuilderFactory;

    @Override
    public Optional<ProgramEntity> getAndFetchCourseById(Long id) {
        var programEntity = QProgramEntity.programEntity;
        var program = new BlazeJPAQuery<ProgramEntity>(entityManager, criteriaBuilderFactory)
                .from(programEntity)
                .leftJoin(programEntity.courses)
                .fetchJoin()
                .where(programEntity.id.eq(id))
                .select(programEntity)
                .fetchFirst();
        return Optional.ofNullable(program);
    }

    @Override
    public List<ProgramEntity> getAndFetchCourseByIdIn(List<Long> id) {
        var programEntity = QProgramEntity.programEntity;
        return new BlazeJPAQuery<ProgramEntity>(entityManager, criteriaBuilderFactory)
                .from(programEntity)
                .leftJoin(programEntity.courses)
                .fetchJoin()
                .where(programEntity.id.in(id))
                .select(programEntity)
                .fetch();
    }

    @Override
    public com.example.lmsbackend.repository.custom.PagedList<ProgramEntity> getPrograms(BaseSearchCriteria criteria) {
        var graph = entityManager.getEntityGraph("program-course");

        var cb = entityManager.getCriteriaBuilder();
        var programQuery = cb.createQuery(ProgramEntity.class);
        var root = programQuery.from(ProgramEntity.class);
        var predicatesAnd = new ArrayList<Predicate>();

        if (StringUtils.isNoneBlank(criteria.getKeyword())) {
            var o = new ArrayList<Predicate>();
            o.add(cb.like(cb.lower(root.get(Fields.name)), "%" + StringUtils.lowerCase(criteria.getKeyword()) + "%"));
            o.add(cb.like(cb.lower(root.get(Fields.code)), "%" + StringUtils.lowerCase(criteria.getKeyword()) + "%"));
            predicatesAnd.add(cb.or(o.toArray(new Predicate[0])));
        }

        if (CollectionUtils.isNotEmpty(criteria.getFilters())) {
            var isPublishedFilter = criteria.getFilters().get(0);
            var o = new ArrayList<Predicate>();
            for (var v : isPublishedFilter.getValues()) {
                o.add(cb.equal(root.get(Fields.isPublished), StringUtils.equals("true", v) ? true : false));
            }
            predicatesAnd.add(cb.or(o.toArray(new Predicate[0])));
        }

        var countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(ProgramEntity.class)));
        entityManager.createQuery(countQuery);
        if (predicatesAnd.size() > 0) {
            countQuery.where(cb.and(predicatesAnd.toArray(new Predicate[0])));
            programQuery.where(cb.and(predicatesAnd.toArray(new Predicate[0])));
        }

        buildSortQuery(criteria.getSorts(), cb, root, programQuery);

        var count = entityManager.createQuery(countQuery).getSingleResult();

        var select = programQuery.select(root);

        var typedProgramQuery = entityManager.createQuery(select);

        typedProgramQuery.setFirstResult((criteria.getPagination().getPage() - 1) * criteria.getPagination().getSize());
        typedProgramQuery.setMaxResults(criteria.getPagination().getSize());
        typedProgramQuery.setHint(FETCH_GRAPH, graph);
        var result = typedProgramQuery.getResultList();
        return new com.example.lmsbackend.repository.custom.PagedList<>(result, count, (criteria.getPagination().getPage() - 1) * criteria.getPagination().getSize(), criteria.getPagination().getSize());
    }

    @Override
    public PagedList<ProgramEntity> getPrograms() {
        var program = QProgramEntity.programEntity;
        var course = QCourseEntity.courseEntity;
        var course_program = QCourseProgramEntity.courseProgramEntity;
        var result = new BlazeJPAQuery<ProgramEntity>(entityManager, criteriaBuilderFactory)
                .from(program)
                .innerJoin(program.courses, course_program)
                .where(program.id.eq(1L))
                .orderBy(program.id.asc())
                .transform(groupBy(program.id).as(
                        Projections.constructor(ProgramEntity.class,
                                program.id,
                                program.name,
                                program.code,
                                program.description,
                                program.isStrict,
                                program.isPublished,
                                program.createdAt,
                                program.updatedAt,
                                set(Projections.constructor(CourseProgramEntity.class, course_program.course, course_program.program, course_program.order)))));
        return null;
    }
}
