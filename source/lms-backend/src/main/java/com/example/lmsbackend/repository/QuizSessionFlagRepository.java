package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.exam.QuizSessionFlagEntity;
import com.example.lmsbackend.domain.exam.QuizSessionFlagPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface QuizSessionFlagRepository extends JpaRepository<QuizSessionFlagEntity, QuizSessionFlagPK> {

    @Query("SELECT q.question.id FROM QuizSessionFlagEntity q WHERE q.session.id = ?1 AND q.question.id IN ?2")
    List<Long> getFlaggedQuestion(UUID sessionId, List<Long> questionsId);
}
