package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.scheduler.ClassSessionEntity;
import com.example.lmsbackend.repository.custom.ClassSessionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ClassSessionRepository extends JpaRepository<ClassSessionEntity, Long>,
        QuerydslPredicateExecutor<ClassSessionEntity>, ClassSessionRepositoryCustom {

    @Query("SELECT c FROM ClassSessionEntity c " +
            "LEFT JOIN FETCH c.teacher " +
            "LEFT JOIN FETCH c.unit " +
            "LEFT JOIN FETCH c.classEntity " +
            "WHERE c.id = ?1")
    Optional<ClassSessionEntity> findFetchTeacherUnitClass(Long sessionId);

    @Query("SELECT COUNT(c) FROM ClassSessionEntity c WHERE c.classEntity.id = ?1 AND c.isScheduled = ?2")
    Integer countSessionsByClassAndScheduledStatus(Long classId, Boolean isScheduled);

    @Query("SELECT c FROM ClassSessionEntity c " +
            "LEFT JOIN FETCH c.teacher " +
            "LEFT JOIN FETCH c.unit " +
            "LEFT JOIN FETCH c.classEntity " +
            "WHERE c.classEntity.id = ?1")
    List<ClassSessionEntity> findSessionsByClass(Long classId);

    @Query("SELECT c FROM ClassSessionEntity c LEFT JOIN FETCH c.attendancesTime WHERE c.classEntity.id = ?1")
    Set<ClassSessionEntity> findFetchAttendance(Long classId);

    @Query("SELECT c FROM ClassSessionEntity c " +
            "LEFT JOIN FETCH c.teacher " +
            "LEFT JOIN FETCH c.unit " +
            "LEFT JOIN FETCH c.classEntity " +
            "WHERE c.classEntity.id = ?1 AND c.id <> ?2")
    List<ClassSessionEntity> findSessionsByClassExcept(Long classId, Long sessionId);

    // TODO: sort sessions by?
    @Query("SELECT c FROM ClassSessionEntity c " +
            "LEFT JOIN FETCH c.teacher " +
            "LEFT JOIN FETCH c.unit " +
            "WHERE c.classEntity.id = ?1 AND c.isScheduled = ?2 " +
            "ORDER BY c.startedAt DESC")
    List<ClassSessionEntity> findSessionsByClass(Long classId, Boolean isScheduled);

    @Query("SELECT c.id FROM ClassSessionEntity c WHERE c.classEntity.id = ?1")
    List<Long> findSessionsIdByClass(Long classId);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM ClassSessionEntity c " +
            "WHERE c.id = ?1 AND c.createdBy = ?2")
    Boolean isSessionCreatedBy(Long sessionId, String username);

    @Query("SELECT s FROM ClassSessionEntity s LEFT JOIN FETCH s.attendancesTime WHERE s.id = ?1")
    Optional<ClassSessionEntity> findFetchAttendancesById(Long id);

    @Query("SELECT a.strategy FROM ClassSessionEntity a WHERE a.id = ?1")
    Optional<String> findAttendanceStrategy(Long id);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM ClassSessionEntity s WHERE s.classEntity.id = ?1 AND s.startedAt > ?2")
    boolean hasMoreAfterByDate(Long classId, LocalDateTime dateTime);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM ClassSessionEntity s WHERE s.classEntity.id = ?1 AND s.startedAt < ?2")
    boolean hasMorePreviousByDate(Long classId, LocalDateTime dateTime);
}
