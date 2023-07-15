package com.example.lmsbackend.repository.custom.impl;

import com.example.lmsbackend.criteria.BaseSearchCriteria;
import com.example.lmsbackend.criteria.FilterCriterion;
import com.example.lmsbackend.domain.StudentEntity;
import com.example.lmsbackend.domain.UserEntity;
import com.example.lmsbackend.domain.classmodel.QuizClassEntity;
import com.example.lmsbackend.domain.exam.ExamEntity;
import com.example.lmsbackend.domain.exam.QuizSessionEntity;
import com.example.lmsbackend.dto.exam.UngradedQuestionDto;
import com.example.lmsbackend.enums.FinalVerdict;
import com.example.lmsbackend.enums.GradedState;
import com.example.lmsbackend.repository.custom.PagedList;
import com.example.lmsbackend.repository.custom.QuizSessionRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.lmsbackend.constant.AppConstant.*;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class QuizSessionRepositoryCustomImpl implements QuizSessionRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<QuizSessionEntity> findFetchQuizExamConfigAnswersById(UUID id) {
        var graph = getEntityGraph("exam-session-schema-config-answers");
        return getExamSessionSchemaEntity(id, graph);
    }

    @Override
    public Optional<QuizSessionEntity> findFetchResultById(UUID id) {
        var graph = getEntityGraph("session-result");
        return getExamSessionSchemaEntity(id, graph);
    }

    @Override
    public Optional<QuizSessionEntity> findFetchExam(UUID id) {
        var graph = getEntityGraph("session-quiz");
        return getExamSessionSchemaEntity(id, graph);
    }

    private void buildFilter(Root<QuizSessionEntity> rootQuery,
                             Root<QuizSessionEntity> rootCountQuery,
                             List<FilterCriterion> filters,
                             List<Predicate> predicates,
                             List<Predicate> countPredicates) {
        var cb = entityManager.getCriteriaBuilder();
        filters.stream()
                .filter(x -> !StringUtils.equals(x.getField(), QuizSessionEntity.Fields.lastSession))
                .forEach(filter -> {
                    var path = rootQuery.join(QuizSessionEntity.Fields.result, JoinType.INNER)
                            .get(filter.getField());
                    var countPath = rootCountQuery.join(QuizSessionEntity.Fields.result, JoinType.INNER)
                            .get(filter.getField());
                    predicates.addAll(filter.getValues()
                            .stream()
                            .map(v -> cb.equal(path, getFilterValue(v)))
                            .collect(toList()));
                    countPredicates.addAll(filter.getValues()
                            .stream()
                            .map(v -> cb.equal(countPath, getFilterValue(v)))
                            .collect(toList()));
                });
    }

    private Object getFilterValue(String value) {
        switch (value) {
            case "DONE":
                return GradedState.DONE;
            case "WAITING":
                return GradedState.WAITING;
            case "PASSED":
                return FinalVerdict.PASSED;
            case "FAILED":
                return FinalVerdict.FAILED;
            case "true":
                return true;
            default:
                throw new UnsupportedOperationException(value + " is not supported");
        }
    }

    @Override
    public PagedList<QuizSessionEntity> findByQuiz_User(Long quizId, Optional<Long> userId, boolean fetchOnlySubmitted, BaseSearchCriteria criteria) {
        var cb = entityManager.getCriteriaBuilder();

        var query = cb.createQuery(QuizSessionEntity.class);
        var root = query.from(QuizSessionEntity.class);
        var countQuery = cb.createQuery(Long.class);
        var rootCountQuery = countQuery.from(QuizSessionEntity.class);
        var predicates = new ArrayList<Predicate>();
        var countPredicates = new ArrayList<Predicate>();
        buildFilter(root, rootCountQuery, criteria.getFilters(), predicates, countPredicates);

        var lastSessionOpt = criteria.getFilters()
                .stream()
                .filter(x -> StringUtils.equals(x.getField(), QuizSessionEntity.Fields.lastSession))
                .findAny();
        if (lastSessionOpt.isPresent()) {
            predicates.add(cb.equal(root.get(QuizSessionEntity.Fields.lastSession), true));
            countPredicates.add(cb.equal(rootCountQuery.get(QuizSessionEntity.Fields.lastSession), true));
        }

        var quizIdPath = root.join(QuizSessionEntity.Fields.quiz, JoinType.INNER)
                .get(QuizClassEntity.Fields.id);
        var countQuizIdPath = rootCountQuery.join(QuizSessionEntity.Fields.quiz, JoinType.INNER)
                .get(QuizClassEntity.Fields.id);
        predicates.add(cb.equal(quizIdPath, quizId));
        countPredicates.add(cb.equal(countQuizIdPath, quizId));
        if (fetchOnlySubmitted) {
            predicates.add(cb.isNotNull(root.get(QuizSessionEntity.Fields.submittedAt)));
            countPredicates.add(cb.isNotNull(rootCountQuery.get(QuizSessionEntity.Fields.submittedAt)));
        }

        if (StringUtils.isNotBlank(criteria.getKeyword())) {
            var studentPath = root.join(QuizSessionEntity.Fields.student, JoinType.INNER);
            var studentCountPath = rootCountQuery.join(QuizSessionEntity.Fields.student, JoinType.INNER);
            predicates.add(cb.like(cb.lower(cb.concat(cb.concat(studentPath.get(UserEntity.Fields.lastName), cb.literal(" ")), studentPath.get(UserEntity.Fields.firstName))),
                    ("%" + criteria.getKeyword() + "%").toLowerCase()));
            countPredicates.add(cb.like(cb.lower(cb.concat(cb.concat(studentCountPath.get(UserEntity.Fields.lastName), cb.literal(" ")), studentCountPath.get(UserEntity.Fields.firstName))),
                    ("%" + criteria.getKeyword() + "%").toLowerCase()));
        }

        countQuery.select(cb.count(rootCountQuery));
        entityManager.createQuery(countQuery);

        userId.ifPresent(aLong -> predicates.add(cb.equal(root.get(QuizSessionEntity.Fields.student).get(UserEntity.Fields.id), aLong)));
        query.select(root);
        query.where(cb.and(predicates.toArray(new Predicate[0])));
        countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));

        if (CollectionUtils.isNotEmpty(criteria.getSorts())) {
            var sort = criteria.getSorts().get(0);
            if (StringUtils.equals(sort.getDirection(), ASC)) {
                query.orderBy(cb.asc(root.join(QuizSessionEntity.Fields.result, JoinType.INNER)
                        .get(sort.getField())), cb.desc(root.get(QuizSessionEntity.Fields.startedAt)), cb.desc(root.get(QuizSessionEntity.Fields.submittedAt)));
            } else if (StringUtils.equals(sort.getDirection(), DESC)) {
                query.orderBy(cb.desc(root.join(QuizSessionEntity.Fields.result, JoinType.INNER)
                        .get(sort.getField())), cb.desc(root.get(QuizSessionEntity.Fields.startedAt)), cb.desc(root.get(QuizSessionEntity.Fields.submittedAt)));
            }
        } else {
            query.orderBy(cb.desc(root.get(QuizSessionEntity.Fields.startedAt)), cb.desc(root.get(QuizSessionEntity.Fields.submittedAt)));
        }

        var count = entityManager.createQuery(countQuery)
                .getSingleResult();
        var typedQuery = entityManager.createQuery(query);
        var page = criteria.getPagination().getPage();
        var size = criteria.getPagination().getSize();
        if (size != null && size != 0) {
            typedQuery.setFirstResult((page - 1) * size);
            typedQuery.setMaxResults(size);
        }
        typedQuery.setHint(FETCH_GRAPH, entityManager.getEntityGraph("session-result-quiz"));
        return new PagedList<>(typedQuery.getResultList(), count, (page - 1) * size, size);
    }

    private Optional<QuizSessionEntity> getExamSessionSchemaEntity(UUID id, EntityGraph<?> graph) {
        var cb = entityManager.getCriteriaBuilder();

        var query = cb.createQuery(QuizSessionEntity.class);
        var root = query.from(QuizSessionEntity.class);
        query.select(root);
        query.where(cb.equal(root.get(ExamEntity.Fields.id), id));
        var typedQuery = entityManager.createQuery(query);
        typedQuery.setHint(FETCH_GRAPH, graph);
        var resultList = typedQuery.getResultList();
        if (CollectionUtils.isEmpty(resultList)) {
            return Optional.empty();
        }
        return Optional.of(resultList.get(0));
    }

    @Override
    public List<UngradedQuestionDto> findUngradedQuestions(UUID sessionId, Long examId) {
        var unGradedQuestionNotInGroup = entityManager.createNativeQuery("select eqs.question_source_id, eqs.sort_index from question q inner join question_source qs on qs.id = q.id\n" +
                        "inner join exam_question_source eqs on qs.id = eqs.question_source_id\n" +
                        "left outer join quiz_session_result_question qsrq on q.id = qsrq.question_id where q.type = 'WRITING' AND qsrq.earned_point is null AND qsrq.session_result_id = :sessionId")
                .setParameter("sessionId", sessionId)
                .getResultList();
        var unGradedQuestionInGroup = entityManager.createNativeQuery("select eqs.question_source_id, eqs.sort_index\n" +
                "from group_question_has_question gq\n" +
                "         inner join question q on gq.group_id = q.id\n" +
                "            inner join question q2 on q2.id = gq.id\n" +
                "         inner join question_source qs on qs.id = q.id\n" +
                "         inner join exam_question_source eqs on qs.id = eqs.question_source_id\n" +
                "         left outer join quiz_session_result_question qsrq on q2.id = qsrq.question_id\n" +
                "where q2.type = 'WRITING'\n" +
                "  AND qsrq.earned_point is null\n" +
                "  AND qsrq.session_result_id = :sessionId")
                .setParameter("sessionId", sessionId)
                .getResultList();
        unGradedQuestionNotInGroup.addAll(unGradedQuestionInGroup);
        if (CollectionUtils.isEmpty(unGradedQuestionNotInGroup)) {
            return List.of();
        }
        var query = "select question_source.id, count(question_source.id) from exam_question_source, question_source where ";
        query = query + unGradedQuestionNotInGroup.stream()
                .map(x -> "(question_source.id = " + ((Object[]) unGradedQuestionNotInGroup.get(0))[0] + " and exam_question_source.sort_index <= " + ((Object[]) unGradedQuestionNotInGroup.get(0))[1] + ") and exam_question_source.exam_id = " + examId)
                .collect(Collectors.joining(" or ")) + " group by question_source.id";
        return (List<UngradedQuestionDto>) entityManager.createNativeQuery(query)
                .getResultList()
                .stream()
                .map(r -> {
                    var dto = new UngradedQuestionDto();
                    dto.setQuestionId(((Number) ((Object[]) r)[0]).longValue());
                    dto.setOrder(((Number) ((Object[]) r)[1]).intValue());
                    return dto;
                })
                .collect(toList());
    }

    private EntityGraph<?> getEntityGraph(String graph) {
        return entityManager.getEntityGraph(graph);
    }
}
