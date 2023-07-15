package com.example.lmsbackend.repository.custom.impl;

import com.example.lmsbackend.domain.exam.ExamEntity;
import com.example.lmsbackend.domain.exam.base_question.Question;
import com.example.lmsbackend.domain.exam.base_question.QuestionEntity;
import com.example.lmsbackend.domain.exam.base_question.QuestionSourceEntity;
import com.example.lmsbackend.repository.custom.QuestionRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.lmsbackend.constant.AppConstant.FETCH_GRAPH;

@RequiredArgsConstructor
public class QuestionRepositoryCustomImpl implements QuestionRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<QuestionEntity> getQuestionById(Long id) {
        return findFetchById(id, "questionSource-textbook");
    }

    private EntityGraph<?> getGraph(String properties) {
        return entityManager.getEntityGraph(String.format("question-%s", properties));
    }

    @Override
    public Optional<QuestionEntity> findFetchById(Long id, String properties) {
        var cb = entityManager.getCriteriaBuilder();
        var query = cb.createQuery(QuestionEntity.class);
        var root = query.from(QuestionEntity.class);

        query.select(root)
                .where(cb.equal(root.get(QuestionEntity.Fields.id), id));

        var typedQuery = entityManager.createQuery(query);
        typedQuery.setHint(FETCH_GRAPH, getGraph(properties));
        var result = typedQuery.getResultList();
        if (CollectionUtils.isEmpty(result)) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

    @Override
    public List<QuestionEntity> findQuestionsByIdIn(List<Long> idList) {
        var cb = entityManager.getCriteriaBuilder();
        var query = cb.createQuery(QuestionEntity.class);
        var root = query.from(QuestionEntity.class);

        query.select(root)
                .where(root.get(QuestionEntity.Fields.id).in(idList));

        var typedQuery = entityManager.createQuery(query);
        typedQuery.setHint(FETCH_GRAPH, entityManager.getEntityGraph("question-full"));
        return typedQuery.getResultList();
    }

    public void getQuestionByKeyword(Long examId, String keyword, Integer page, Integer size) {
        var cb = entityManager.getCriteriaBuilder();
        var query = cb.createQuery(QuestionEntity.class);
        var root = query.from(QuestionEntity.class);
        var predicates = new ArrayList<Predicate>();

        join(root, predicates, cb, keyword);

        var examPath = root.join(QuestionEntity.Fields.questionSource, JoinType.LEFT)
                .join(QuestionSourceEntity.Fields.exams).get(ExamEntity.Fields.id);


        query.select(root);
        query.where(cb.and(cb.or(predicates.toArray(new Predicate[0])), cb.equal(examPath, examId)));
        var typedQuery = entityManager.createQuery(query);
        typedQuery.setHint(FETCH_GRAPH, entityManager.getEntityGraph("question-questionSource-textbook"));

        // for count query
        var countQuery = cb.createQuery(Long.class);
        var rootCountQuery = countQuery.from(QuestionEntity.class);
        var predicatesCount = new ArrayList<Predicate>();
        join(rootCountQuery, predicatesCount, cb, keyword);

        var examPathCount = rootCountQuery.join(QuestionEntity.Fields.questionSource, JoinType.LEFT)
                .join(QuestionSourceEntity.Fields.exams).get(ExamEntity.Fields.id);

        countQuery.select(cb.count(rootCountQuery));
        entityManager.createQuery(countQuery);
        countQuery.where(cb.and(cb.or(predicates.toArray(new Predicate[0])), cb.equal(examPathCount, examId)));

        var count = entityManager.createQuery(countQuery)
                .getSingleResult();

        var result = typedQuery.getResultList();
    }

    private void join(Root root, List<Predicate> predicates, CriteriaBuilder cb, String keyword) {
        join(root, QuestionEntity.Fields.writingQuestion, predicates, cb, keyword);
        join(root, QuestionEntity.Fields.fillInBlankQuestion, predicates, cb, keyword);
        join(root, QuestionEntity.Fields.fillInBlankDragAndDropQuestion, predicates, cb, keyword);
        join(root, QuestionEntity.Fields.fillInBlankMultiChoiceQuestion, predicates, cb, keyword);
        join(root, QuestionEntity.Fields.multiChoiceQuestion, predicates, cb, keyword);
        join(root, QuestionEntity.Fields.groupQuestion, predicates, cb, keyword);
    }

    private void join(Root root, String path, List<Predicate> predicates, CriteriaBuilder cb, String keyword) {
//        var joinPath = root.join(path, JoinType.LEFT);
//        predicates.add(cb.like(joinPath.get(Question.Fields.description), "%" + keyword + "%"));
    }
}
