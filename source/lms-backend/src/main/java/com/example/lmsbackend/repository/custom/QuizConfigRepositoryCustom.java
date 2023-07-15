package com.example.lmsbackend.repository.custom;

import com.example.lmsbackend.domain.exam.QuizConfigEntity;

import java.util.Optional;

public interface QuizConfigRepositoryCustom {
    Optional<QuizConfigEntity> findFetchById(Long quizId);
}
