package com.example.lmsbackend.repository.custom.impl;

import com.example.lmsbackend.domain.classmodel.SessionAttendanceTimeEntity;
import com.example.lmsbackend.repository.custom.SessionAttendanceTimeRepositoryCustom;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.example.lmsbackend.constant.AppConstant.FETCH_GRAPH;

@RequiredArgsConstructor
public class SessionAttendanceTimeRepositoryCustomImpl implements SessionAttendanceTimeRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<SessionAttendanceTimeEntity> findBySessionId(Long sessionId) {
        return entityManager.createQuery("SELECT a FROM SessionAttendanceTimeEntity a WHERE a.session.id = (:id) ORDER BY a.createdAt ASC, a.id ASC")
                .setParameter("id", sessionId)
                .setHint(FETCH_GRAPH, entityManager.getEntityGraph("attendance"))
                .getResultList();
    }
}
