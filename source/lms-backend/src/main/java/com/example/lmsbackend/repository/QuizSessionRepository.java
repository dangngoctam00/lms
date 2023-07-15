package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.classmodel.QuizClassEntity;
import com.example.lmsbackend.domain.exam.QuizSessionEntity;
import com.example.lmsbackend.repository.custom.QuizSessionRepositoryCustom;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuizSessionRepository extends JpaRepository<QuizSessionEntity, UUID>,
        QuerydslPredicateExecutor<QuizSessionEntity>, QuizSessionRepositoryCustom {

    @EntityGraph(attributePaths = {"quiz", "quiz.config", "answers"})
    Optional<QuizSessionEntity> findById(UUID id);


    @Query("SELECT count(q) FROM QuizSessionEntity q WHERE q.quiz.id = ?1 AND q.student.id = ?2")
    Integer countQuizSessionByUser(Long quizId, Long userId);

    @Query("SELECT COALESCE(count(q), 0) FROM QuizSessionEntity q WHERE q.quiz.id = ?1")
    Long countQuizSessionByQuiz(Long quizId);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM QuizSessionEntity c WHERE c.quiz.id = ?1 AND c.student.id = ?2 AND c.submittedAt IS NULL")
    Boolean isOpenedQuizSessionExistingByStudent(Long quizId, Long studentId);

    @Query("SELECT q FROM QuizSessionEntity q " +
            "LEFT JOIN FETCH q.answers " +
            "LEFT JOIN FETCH q.quiz " +
            "WHERE q.id = ?1 AND q.submittedAt IS NULL")
    Optional<QuizSessionEntity> findPotentialActiveSession(UUID id);

    @Query("SELECT q FROM QuizSessionEntity q " +
            "INNER JOIN QuizClassEntity quiz ON quiz.id = q.quiz.id " +
            "WHERE quiz.id = ?1 AND q.submittedAt IS NOT NULL")
    List<QuizSessionEntity> findEndedQuizSessionByQuiz(Long quizId);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM QuizSessionEntity s WHERE s.id = ?1 AND s.student.id = ?2")
    Boolean isSessionCreatedByUser(UUID sessionId, Long userId);

    @Query("SELECT COUNT(s) FROM QuizSessionEntity s WHERE s.student.id = ?1 AND s.quiz.id = ?2")
    long getNumberOfSessions(Long userId, Long quizId);

    @Query("SELECT q.id FROM QuizSessionEntity q " +
            "WHERE q.submittedAt is NULL AND q.quiz.id = ?1 AND q.student.id = ?2")
    Optional<UUID> existingUnEndedQuizSession(Long quizId, Long studentId);

    @EntityGraph(attributePaths = {"quiz", "result"})
    List<QuizSessionEntity> findByQuiz(QuizClassEntity quiz);
}
