package com.example.lmsbackend.repository.custom;

import com.example.lmsbackend.criteria.BaseSearchCriteria;
import com.example.lmsbackend.domain.exam.QuizSessionEntity;
import com.example.lmsbackend.dto.exam.UngradedQuestionDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuizSessionRepositoryCustom {

    Optional<QuizSessionEntity> findFetchQuizExamConfigAnswersById(UUID id);

    Optional<QuizSessionEntity> findFetchResultById(UUID id);

    Optional<QuizSessionEntity> findFetchExam(UUID id);

    PagedList<QuizSessionEntity> findByQuiz_User(Long quizId, Optional<Long> userId, boolean fetchOnlySubmitted, BaseSearchCriteria criteria);

    List<UngradedQuestionDto> findUngradedQuestions(UUID sessionId, Long examId);
}
