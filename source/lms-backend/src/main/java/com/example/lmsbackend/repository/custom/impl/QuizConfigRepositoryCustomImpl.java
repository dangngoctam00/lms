package com.example.lmsbackend.repository.custom.impl;

import com.example.lmsbackend.domain.exam.QuizConfigEntity;
import com.example.lmsbackend.repository.custom.QuizConfigRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static com.example.lmsbackend.constant.AppConstant.FETCH_GRAPH;

@RequiredArgsConstructor
public class QuizConfigRepositoryCustomImpl implements QuizConfigRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<QuizConfigEntity> findFetchById(Long quizId) {
        var cb = entityManager.getCriteriaBuilder();

        var query = cb.createQuery(QuizConfigEntity.class);
        var root = query.from(QuizConfigEntity.class);
        query.select(root);
        query.where(cb.equal(root.get(QuizConfigEntity.Fields.id), quizId));
        var typedQuery = entityManager.createQuery(query);
        typedQuery.setHint(FETCH_GRAPH, entityManager.getEntityGraph("exam-config-quiz"));
        var result = typedQuery.getResultList();
        if (CollectionUtils.isEmpty(result)) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }
}
