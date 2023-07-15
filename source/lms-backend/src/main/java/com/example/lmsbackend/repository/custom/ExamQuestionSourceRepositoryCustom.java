package com.example.lmsbackend.repository.custom;

import com.example.lmsbackend.domain.exam.ExamQuestionSourceEntity;
import com.example.lmsbackend.dto.exam.QuestionInExamByTypeAndIdDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExamQuestionSourceRepositoryCustom {
    PagedList<QuestionInExamByTypeAndIdDto> getQuestionsIdByExam(Long examId, Integer page, Integer size);

    List<QuestionInExamByTypeAndIdDto> getQuestionsIdAndTypeByExam(UUID sessionId, Long examId);

    Optional<ExamQuestionSourceEntity> findByExamAndQuestion(Long examId, Long questionId);

    int getPageOfQuestion(Long examId, Long questionId, int pageSize);
}
