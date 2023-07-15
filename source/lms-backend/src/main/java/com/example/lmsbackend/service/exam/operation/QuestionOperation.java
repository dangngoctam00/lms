package com.example.lmsbackend.service.exam.operation;

import com.example.lmsbackend.domain.exam.AnswerQuestionTemporaryEntity;
import com.example.lmsbackend.domain.exam.QuizSessionResultQuestionEntity;
import com.example.lmsbackend.domain.exam.base_question.Question;
import com.example.lmsbackend.domain.exam.base_question.QuestionEntity;
import com.example.lmsbackend.domain.exam.base_question.QuestionSourceEntity;
import com.example.lmsbackend.dto.exam.QuestionAnswerDto;
import com.example.lmsbackend.dto.exam.QuestionDto;

import java.util.UUID;

public interface QuestionOperation {

    QuestionSourceEntity persistQuestion(QuestionDto dto);

    Question getById(Long id);

    QuestionDto updateQuestion(Long id, QuestionDto dto);

    QuestionDto mapToQuestionResponseDto(Question question);

    QuestionDto mapToQuestionResponseDto(QuestionEntity question);

    QuestionDto mapToQuestionResponseDtoIgnoreAnswer(Question question);

    void grade(QuizSessionResultQuestionEntity answerQuestion);

    QuestionAnswerDto mapToQuestionAnswerResponse(Question question, QuizSessionResultQuestionEntity resultForQuestion);

    QuestionDto mapToQuestionAnswerResponse(Question question, AnswerQuestionTemporaryEntity answer, UUID sessionId);

    Question getQuestionByTypeFromQuestionEntity(QuestionEntity entity);
}
