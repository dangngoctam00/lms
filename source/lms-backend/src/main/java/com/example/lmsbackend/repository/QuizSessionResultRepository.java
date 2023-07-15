package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.exam.QuizSessionResultEntity;
import com.example.lmsbackend.domain.exam.QuizSessionResultQuestionEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuizSessionResultRepository extends JpaRepository<QuizSessionResultEntity, UUID> {

    @EntityGraph(attributePaths = {"session", "resultQuestions"})
    Optional<QuizSessionResultEntity> findById(UUID id);

    @Query("SELECT DISTINCT s.sessionResult.id FROM QuizSessionResultQuestionEntity s WHERE s.sessionResult.id IN ?1 AND s.earnedPoint IS NULL")
    List<UUID> findUnGradedSession(List<UUID> sessionsId);

    @Query("SELECT CASE WHEN COUNT (s) = 0 THEN true ELSE false END FROM QuizSessionResultQuestionEntity s WHERE s.sessionResult.id = ?1 AND s.earnedPoint IS NULL")
    Boolean isFullyGraded(UUID sessionId);

    @Query("SELECT q FROM QuizSessionResultEntity q " +
            "INNER JOIN QuizSessionEntity session ON session.id = q.id " +
            "INNER JOIN QuizClassEntity quiz ON quiz.id = session.quiz.id " +
            "WHERE quiz.id = ?1")
    List<QuizSessionResultEntity> findQuizSessionResultByQuiz(Long quizId);

    @Query("SELECT q FROM QuizSessionResultQuestionEntity q LEFT JOIN FETCH q.question WHERE q.sessionResult.id IN ?1")
    List<QuizSessionResultQuestionEntity> findResultQuestionBySessionIn(List<UUID> sessions);
}
