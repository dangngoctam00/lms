package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.exam.QuizResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface QuizResultRepository extends JpaRepository<QuizResultEntity, Long> {

    @Query("SELECT q FROM QuizResultEntity q LEFT JOIN FETCH q.studentsResult WHERE q.quiz.id = ?1")
    Optional<QuizResultEntity> findByQuizId(Long quizId);

    @Query("SELECT AVG (q.score) FROM QuizSessionResultEntity q WHERE q.gradedState = 'DONE' AND q.session.quiz.id = ?1")
    Double calculateAverageByQuizId(Long quizId);

    @Query("SELECT COUNT (DISTINCT q.student.id) FROM QuizSessionEntity q WHERE q.quiz.id = ?1")
    Integer getParticipantsByQuizId(Long quizId);

    @Query("SELECT COUNT (DISTINCT q.student.id) FROM QuizSessionEntity q WHERE " +
            "EXISTS (SELECT 1 FROM QuizSessionResultEntity s WHERE s.id = q.id AND s.gradedState = 'DONE' AND s.finalVerdict = 'PASSED')")
    Integer getPassedStudentsByQuizId(Long quizId);
}
