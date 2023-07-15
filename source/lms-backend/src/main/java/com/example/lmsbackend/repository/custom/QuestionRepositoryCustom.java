package com.example.lmsbackend.repository.custom;

import com.example.lmsbackend.domain.exam.base_question.QuestionEntity;

import java.util.List;
import java.util.Optional;

public interface QuestionRepositoryCustom {
    Optional<QuestionEntity> getQuestionById(Long id);

    void getQuestionByKeyword(Long examId, String keyword, Integer page, Integer size);

    List<QuestionEntity> findQuestionsByIdIn(List<Long> idList);

    Optional<QuestionEntity> findFetchById(Long id, String properties);
}
