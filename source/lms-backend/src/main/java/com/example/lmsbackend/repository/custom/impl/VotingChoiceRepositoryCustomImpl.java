package com.example.lmsbackend.repository.custom.impl;

import com.example.lmsbackend.repository.custom.VotingChoiceRepositoryCustom;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RequiredArgsConstructor
public class VotingChoiceRepositoryCustomImpl implements VotingChoiceRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public void deleteByIdIn(List<Long> ids) {
        entityManager.createQuery("DELETE FROM VotingChoiceEntity AS v WHERE v.id IN (:ids)")
                .setParameter("ids", ids)
                .executeUpdate();
    }
}
