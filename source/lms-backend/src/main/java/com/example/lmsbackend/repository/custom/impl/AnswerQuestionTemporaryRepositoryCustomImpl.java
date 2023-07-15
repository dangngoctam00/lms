package com.example.lmsbackend.repository.custom.impl;

import com.example.lmsbackend.domain.exam.AnswerQuestionTemporaryEntity;
import com.example.lmsbackend.domain.exam.QuizSessionEntity;
import com.example.lmsbackend.domain.exam.base_question.QuestionEntity;
import com.example.lmsbackend.repository.custom.AnswerQuestionTemporaryRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;
import java.util.UUID;

import static com.example.lmsbackend.constant.AppConstant.FETCH_GRAPH;

@RequiredArgsConstructor
public class AnswerQuestionTemporaryRepositoryCustomImpl implements AnswerQuestionTemporaryRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<AnswerQuestionTemporaryEntity> findBySessionAndQuestion(UUID sessionId, Long questionId) {
        var cb = entityManager.getCriteriaBuilder();
        var query = cb.createQuery(AnswerQuestionTemporaryEntity.class);
        var root = query.from(AnswerQuestionTemporaryEntity.class);

        query.select(root);
        query.where(cb.and(
                cb.equal(root.get(AnswerQuestionTemporaryEntity.Fields.question).get(QuestionEntity.Fields.id), questionId),
                cb.equal(root.get(AnswerQuestionTemporaryEntity.Fields.session).get(QuizSessionEntity.Fields.id), sessionId)
        ));

        var typedQuery = entityManager.createQuery(query);
        typedQuery.setHint(FETCH_GRAPH, entityManager.getEntityGraph("answer-question-temporary-session-values-question"));

        var result = typedQuery.getResultList();
        if (CollectionUtils.isEmpty(result)) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }
}
