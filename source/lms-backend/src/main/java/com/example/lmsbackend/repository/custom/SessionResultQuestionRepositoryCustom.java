package com.example.lmsbackend.repository.custom;

import com.example.lmsbackend.domain.exam.QuizSessionResultQuestionEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionResultQuestionRepositoryCustom {
    List<QuizSessionResultQuestionEntity> findByQuestionAndSessionResult(List<Long> questionsId, UUID sessionResultId);

    Double calculateTotalGradeByExamSession(UUID sessionId);
}
