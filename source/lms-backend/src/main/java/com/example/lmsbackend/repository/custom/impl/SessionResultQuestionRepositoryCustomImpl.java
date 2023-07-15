package com.example.lmsbackend.repository.custom.impl;

import com.example.lmsbackend.domain.exam.QuizSessionResultEntity;
import com.example.lmsbackend.domain.exam.QuizSessionResultQuestionEntity;
import com.example.lmsbackend.domain.exam.base_question.QuestionEntity;
import com.example.lmsbackend.repository.custom.SessionResultQuestionRepositoryCustom;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

import static com.example.lmsbackend.constant.AppConstant.FETCH_GRAPH;

@RequiredArgsConstructor
public class SessionResultQuestionRepositoryCustomImpl implements SessionResultQuestionRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<QuizSessionResultQuestionEntity> findByQuestionAndSessionResult(List<Long> questionsId, UUID sessionResultId) {
        var cb = entityManager.getCriteriaBuilder();

        var query = cb.createQuery(QuizSessionResultQuestionEntity.class);
        var root = query.from(QuizSessionResultQuestionEntity.class);
        query.select(root);
        query.where(cb.and(
                        root.get(QuizSessionResultQuestionEntity.Fields.question).get(QuestionEntity.Fields.id).in(questionsId)),
                cb.equal(root.get(QuizSessionResultQuestionEntity.Fields.sessionResult).get(QuizSessionResultEntity.Fields.id), sessionResultId));
        var typedQuery = entityManager.createQuery(query);
        typedQuery.setHint(FETCH_GRAPH, entityManager.getEntityGraph("exam-session-result-answer"));
         return typedQuery.getResultList();
    }

    @Override
    public Double calculateTotalGradeByExamSession(UUID sessionId) {
        var cb = entityManager.getCriteriaBuilder();

        var query = cb.createQuery(Double.class);
        var root = query.from(QuizSessionResultQuestionEntity.class);
        query.select(cb.sum(root.get(QuizSessionResultQuestionEntity.Fields.earnedPoint)));
        entityManager.createQuery(query);
        query.where(cb.equal(root.get(QuizSessionResultQuestionEntity.Fields.sessionResult).get(QuizSessionResultEntity.Fields.id), sessionId));
        // trigger flush to sync persistent context with database
        return entityManager.createQuery(query).getSingleResult();
    }
}
