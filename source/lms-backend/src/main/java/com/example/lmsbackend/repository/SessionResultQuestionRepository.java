package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.exam.QuizSessionResultQuestionEntity;
import com.example.lmsbackend.repository.custom.SessionResultQuestionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;
import java.util.UUID;

public interface SessionResultQuestionRepository extends JpaRepository<QuizSessionResultQuestionEntity, Long>,
        QuerydslPredicateExecutor<QuizSessionResultQuestionEntity>, SessionResultQuestionRepositoryCustom {

    @Query("SELECT u FROM QuizSessionResultQuestionEntity u WHERE u.question.id = ?1 AND u.sessionResult.id = ?2")
    Optional<QuizSessionResultQuestionEntity> findByQuestionAndSession(Long questionId, UUID sessionID);
}
