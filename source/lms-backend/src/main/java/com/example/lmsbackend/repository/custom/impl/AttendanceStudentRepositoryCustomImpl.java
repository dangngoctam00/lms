package com.example.lmsbackend.repository.custom.impl;

import com.example.lmsbackend.domain.classmodel.AttendanceStudentEntity;
import com.example.lmsbackend.repository.custom.AttendanceStudentRepositoryCustom;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;

import static com.example.lmsbackend.constant.AppConstant.FETCH_GRAPH;

@RequiredArgsConstructor
public class AttendanceStudentRepositoryCustomImpl implements AttendanceStudentRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Set<AttendanceStudentEntity> findOfficialAttendanceBySessionIdIn(List<Long> sessionsId) {
        return Set.copyOf(entityManager.createQuery("SELECT a FROM AttendanceStudentEntity a " +
                        "INNER JOIN SessionAttendanceTimeEntity ae ON ae.id = a.attendanceTime.id " +
                        "INNER JOIN ClassSessionEntity aes ON aes.id = ae.session.id " +
                        "WHERE aes.id IN (:sessionsId) " +
                        "AND ae.isOfficial = true", AttendanceStudentEntity.class)
                .setParameter("sessionsId", sessionsId)
                .setHint(FETCH_GRAPH, entityManager.getEntityGraph("attendance-student"))
                .getResultList());
    }

    @Override
    public Set<AttendanceStudentEntity> findOfficialAttendanceByStudentIdAndSessionIdIn(Long studentId, List<Long> sessionsId) {
        return Set.copyOf(entityManager.createQuery("SELECT a FROM AttendanceStudentEntity a " +
                        "INNER JOIN SessionAttendanceTimeEntity ae ON ae.id = a.attendanceTime.id " +
                        "INNER JOIN ClassSessionEntity aes ON aes.id = ae.session.id " +
                        "INNER JOIN ClassStudentEntity m ON m.id = a.student.id " +
                        "WHERE aes.id IN (:sessionsId) " +
                        "AND ae.isOfficial = true AND m.student.id = (:studentId)", AttendanceStudentEntity.class)
                .setParameter("sessionsId", sessionsId)
                .setParameter("studentId", studentId)
                .setHint(FETCH_GRAPH, entityManager.getEntityGraph("attendance-student"))
                .getResultList());
    }
}
