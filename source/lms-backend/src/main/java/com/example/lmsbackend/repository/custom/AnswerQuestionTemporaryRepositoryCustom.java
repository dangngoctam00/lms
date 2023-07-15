package com.example.lmsbackend.repository.custom;

import com.example.lmsbackend.domain.exam.AnswerQuestionTemporaryEntity;

import java.util.Optional;
import java.util.UUID;

public interface AnswerQuestionTemporaryRepositoryCustom {
    Optional<AnswerQuestionTemporaryEntity> findBySessionAndQuestion(UUID sessionId, Long questionId);
}
