package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.exam.AnswerQuestionTemporaryEntity;
import com.example.lmsbackend.repository.custom.AnswerQuestionTemporaryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface AnswerQuestionTemporaryRepository extends JpaRepository<AnswerQuestionTemporaryEntity, Long>,
        QuerydslPredicateExecutor<AnswerQuestionTemporaryEntity>, AnswerQuestionTemporaryRepositoryCustom {

    @Query("SELECT a FROM AnswerQuestionTemporaryEntity a WHERE a.session.id IN ?1")
    List<AnswerQuestionTemporaryEntity> findAnswerQuestionBySessionIn(List<UUID> sessions);

    @Query("SELECT a FROM AnswerQuestionTemporaryEntity a LEFT JOIN FETCH a.values WHERE a.session.id = ?1 AND a.question.id IN ?2")
    Set<AnswerQuestionTemporaryEntity> findAnswerQuestionBySessionAndQuestionId(UUID sessionId, List<Long> questionId);

    @Query("SELECT a.question.id FROM AnswerQuestionTemporaryEntity a WHERE a.session.id = ?1")
    Set<Long> findAnsweredQuestionsOfSessions(UUID sessionId);
}
