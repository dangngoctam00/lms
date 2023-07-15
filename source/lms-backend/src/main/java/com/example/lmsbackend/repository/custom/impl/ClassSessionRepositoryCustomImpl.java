package com.example.lmsbackend.repository.custom.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.example.lmsbackend.domain.scheduler.ClassSessionEntity;
import com.example.lmsbackend.domain.scheduler.QClassSessionEntity;
import com.example.lmsbackend.repository.custom.ClassSessionRepositoryCustom;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class ClassSessionRepositoryCustomImpl implements ClassSessionRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    private final CriteriaBuilderFactory criteriaBuilderFactory;

    @Override
    public List<ClassSessionEntity> findClassSessions(Long classId, LocalDateTime date, Integer previous, Integer after) {
        var session = QClassSessionEntity.classSessionEntity;
        var afterSessions = new BlazeJPAQuery<ClassSessionEntity>(entityManager, criteriaBuilderFactory)
                .from(session)
                .where(session.startedAt.goe(date)
                        .and(session.classEntity.id.eq(classId)))
                .select(session)
                .orderBy(session.startedAt.asc())
                .orderBy(session.id.asc())
                .fetchPage(0, after);
        if (previous != 0) {
            var previousSessions = new BlazeJPAQuery<ClassSessionEntity>(entityManager, criteriaBuilderFactory)
                    .from(session)
                    .where(session.startedAt.lt(date)
                            .and(session.classEntity.id.eq(classId)))
                    .select(session)
                    .orderBy(session.startedAt.desc())
                    .orderBy(session.id.desc())
                    .fetchPage(0, previous);
            var sortedPreviousSessions = previousSessions.stream()
                    .sorted(Comparator.comparing(ClassSessionEntity::getStartedAt))
                    .collect(toList());
            sortedPreviousSessions.addAll(afterSessions);
            return sortedPreviousSessions;
        }
        return afterSessions;
    }
}
