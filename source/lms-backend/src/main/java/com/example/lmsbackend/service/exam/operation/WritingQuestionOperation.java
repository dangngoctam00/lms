package com.example.lmsbackend.service.exam.operation;

import com.example.lmsbackend.domain.exam.AnswerQuestionTemporaryEntity;
import com.example.lmsbackend.domain.exam.QuizSessionResultQuestionEntity;
import com.example.lmsbackend.domain.exam.base_question.Question;
import com.example.lmsbackend.domain.exam.base_question.QuestionEntity;
import com.example.lmsbackend.domain.exam.base_question.QuestionType;
import com.example.lmsbackend.domain.exam.writing_question.WritingQuestionEntity;
import com.example.lmsbackend.dto.exam.QuestionAnswerDto;
import com.example.lmsbackend.dto.exam.QuestionDto;
import com.example.lmsbackend.dto.exam.writing.WritingQuestionDto;
import com.example.lmsbackend.exceptions.exam.QuestionNotFoundException;
import com.example.lmsbackend.mapper.exam.writing.WritingQuestionMapper;
import com.example.lmsbackend.repository.QuestionRepository;
import com.example.lmsbackend.repository.QuestionSourceRepository;
import com.example.lmsbackend.repository.TextbookRepository;
import com.example.lmsbackend.repository.WritingQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WritingQuestionOperation extends AbstractQuestionOperation {

    private final WritingQuestionMapper mapper;
    private final WritingQuestionRepository repository;

    @Autowired
    public WritingQuestionOperation(QuestionRepository questionRepository,
                                    QuestionSourceRepository questionSourceRepository,
                                    TextbookRepository textbookRepository,
                                    WritingQuestionMapper mapper,
                                    WritingQuestionRepository repository) {
        super(questionRepository, questionSourceRepository, textbookRepository, QuestionType.WRITING.getType());
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    protected Question mapToEntity(QuestionDto dto) {
        return mapper.mapToWritingQuestionEntity((WritingQuestionDto) dto);
    }

    @Override
    protected void persistQuestion(QuestionDto dto, QuestionEntity question) {
        var writingQuestion = (WritingQuestionEntity) mapToEntity(dto);
        writingQuestion.setQuestion(question);
        question.setWritingQuestion(writingQuestion);
        repository.save(writingQuestion);
    }

    @Override
    public Question getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException(id, QuestionType.WRITING.getType()));
    }

    @Override
    protected QuestionDto mapToSpecificQuestionResponseDto(Question question) {
        return mapper.mapToWritingQuestionDto((WritingQuestionEntity) question);
    }

    @Override
    protected QuestionDto mapToSpecificQuestionResponseDtoIgnoreAnswer(Question question) {
        return mapper.mapToWritingQuestionDto((WritingQuestionEntity) question);
    }

    @Override
    public void grade(QuizSessionResultQuestionEntity answerQuestion) {
        return;
    }

    @Override
    public QuestionAnswerDto mapToQuestionAnswerResponse(Question question, QuizSessionResultQuestionEntity resultForQuestion) {
        var writingQuestion = (WritingQuestionEntity) question;
        var dto = mapper.mapToWritingQuestionAnswerDto(writingQuestion);
        if (resultForQuestion.getAnswer() != null) {
            dto.setAnswer(resultForQuestion.getAnswer()
                    .getValues()
                    .stream()
                    .findAny()
                    .get()
                    .getValue());
        }
        dto.setEarnedPoint(resultForQuestion.getEarnedPoint());
        dto.setPoint(writingQuestion.getQuestion().getPoint());
        dto.setDescription(writingQuestion.getQuestion().getDescription());
        dto.setAttachment(writingQuestion.getQuestion().getAttachment());
        return dto;
    }

    @Override
    protected QuestionDto mapToSpecificQuestionResponseDto(QuestionEntity question) {
        return mapToSpecificQuestionResponseDto(question.getWritingQuestion());
    }

    @Override
    public QuestionDto updateQuestion(Long id, QuestionDto dto) {
        var writingQuestion = (WritingQuestionEntity) getByIdFromQuestion(id, QuestionEntity.Fields.writingQuestion);
        mapper.mapToWritingQuestion(writingQuestion, (WritingQuestionDto) dto);
        writingQuestion.getQuestion().setPoint(dto.getPoint());
        repository.save(writingQuestion);
        return mapToSpecificQuestionResponseDto(writingQuestion);
    }

    @Override
    public Question getQuestionByTypeFromQuestionEntity(QuestionEntity entity) {
        return entity.getWritingQuestion();
    }

    @Override
    public QuestionDto mapToQuestionAnswerResponse(Question question, AnswerQuestionTemporaryEntity answer, UUID sessionId) {
        var dto = mapper.mapToWritingQuestionDto((WritingQuestionEntity) question);
        if (answer != null) {
            var opt = answer.getValues().stream().findFirst();
            opt.ifPresent(answerTemporaryEntity -> dto.setStudentAnswer(answerTemporaryEntity.getValue()));
        }
        return dto;
    }
}
