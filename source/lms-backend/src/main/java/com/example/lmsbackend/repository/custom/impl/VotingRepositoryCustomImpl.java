package com.example.lmsbackend.repository.custom.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.example.lmsbackend.domain.classmodel.VotingEntity;
import com.example.lmsbackend.repository.custom.VotingRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static com.example.lmsbackend.constant.AppConstant.FETCH_GRAPH;

@RequiredArgsConstructor
public class VotingRepositoryCustomImpl implements VotingRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    private final CriteriaBuilderFactory criteriaBuilderFactory;

    @Override
    public Optional<VotingEntity> findFetchUserVotingChoicesById(Long id) {
        var graph = entityManager.getEntityGraph("voting");
        return getVoting(id, graph);
    }

    @Override
    public Optional<VotingEntity> findFetchChoicesById(Long id) {
        var graph = entityManager.getEntityGraph("voting-choices");
        return getVoting(id, graph);
    }

    private Optional<VotingEntity> getVoting(Long id, EntityGraph<?> graph) {
        var cb = entityManager.getCriteriaBuilder();

        var query = cb.createQuery(VotingEntity.class);
        var root = query.from(VotingEntity.class);
        query.select(root);
        query.where(cb.equal(root.get(VotingEntity.Fields.id), id));
        var typedQuery = entityManager.createQuery(query);
        typedQuery.setHint(FETCH_GRAPH, graph);
        var result = typedQuery.getResultList();
        if (CollectionUtils.isEmpty(result)) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }
}
