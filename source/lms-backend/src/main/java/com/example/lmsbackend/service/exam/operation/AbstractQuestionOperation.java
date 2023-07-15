package com.example.lmsbackend.service.exam.operation;

import com.example.lmsbackend.domain.exam.base_question.Question;
import com.example.lmsbackend.domain.exam.base_question.QuestionEntity;
import com.example.lmsbackend.domain.exam.base_question.QuestionSourceEntity;
import com.example.lmsbackend.dto.exam.QuestionDto;
import com.example.lmsbackend.exceptions.exam.QuestionNotFoundException;
import com.example.lmsbackend.repository.QuestionRepository;
import com.example.lmsbackend.repository.QuestionSourceRepository;
import com.example.lmsbackend.repository.TextbookRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractQuestionOperation implements QuestionOperation {

    protected final QuestionRepository questionRepository;
    protected final QuestionSourceRepository questionSourceRepository;
    protected final TextbookRepository textbookRepository;
    protected final String questionType;


    protected abstract Question mapToEntity(QuestionDto dto);

    protected abstract void persistQuestion(QuestionDto question, QuestionEntity entity);

    protected abstract QuestionDto mapToSpecificQuestionResponseDto(Question question);

    protected abstract QuestionDto mapToSpecificQuestionResponseDto(QuestionEntity question);

    protected abstract QuestionDto mapToSpecificQuestionResponseDtoIgnoreAnswer(Question question);

    public abstract Question getQuestionByTypeFromQuestionEntity(QuestionEntity entity);

    public QuestionSourceEntity persistQuestion(QuestionDto dto) {
        var questionEntity = new QuestionEntity();
        questionEntity.setType(questionType);

        questionEntity.setPoint(dto.getPoint());
        questionEntity.setDescription(dto.getDescription());
        questionEntity.setAttachment(dto.getAttachment());
        questionEntity.setNote(dto.getNote());

        var questionSource = new QuestionSourceEntity();

        questionSource.setQuestion(questionEntity);

        questionSourceRepository.save(questionSource);

        persistQuestion(dto, questionEntity);

        // set ID here to avoid re-mapping GROUP question
        dto.setId(questionSource.getId());

        return questionSource;
    }

    @Override
    public QuestionDto mapToQuestionResponseDto(Question question) {
        var dto = mapToSpecificQuestionResponseDto(question);
        dto.setType(questionType);
        return dto;
    }

    @Override
    public QuestionDto mapToQuestionResponseDto(QuestionEntity question) {
        var dto = mapToSpecificQuestionResponseDto(question);
        dto.setType(questionType);
        return dto;
    }

    @Override
    public QuestionDto mapToQuestionResponseDtoIgnoreAnswer(Question question) {
        var dto = mapToSpecificQuestionResponseDtoIgnoreAnswer(question);
        dto.setType(questionType);
        return dto;
    }

    protected Question getByIdFromQuestion(Long id, String properties) {
        var question = questionRepository.findFetchById(id, properties)
                .orElseThrow(() -> new QuestionNotFoundException(id));
        return getQuestionByTypeFromQuestionEntity(question);
    }
}