package com.example.lmsbackend.service.exam.operation;

import com.example.lmsbackend.domain.exam.AnswerQuestionTemporaryEntity;
import com.example.lmsbackend.domain.exam.AnswerTemporaryEntity;
import com.example.lmsbackend.domain.exam.QuizSessionResultQuestionEntity;
import com.example.lmsbackend.domain.exam.base_question.Question;
import com.example.lmsbackend.domain.exam.base_question.QuestionEntity;
import com.example.lmsbackend.domain.exam.base_question.QuestionType;
import com.example.lmsbackend.domain.exam.fill_in_blank.FillInBlankOptionEntity;
import com.example.lmsbackend.domain.exam.fill_in_blank.FillInBlankQuestionEntity;
import com.example.lmsbackend.dto.exam.QuestionAnswerDto;
import com.example.lmsbackend.dto.exam.QuestionDto;
import com.example.lmsbackend.dto.exam.fill_in_blank.FillInBlankOptionAnswerDto;
import com.example.lmsbackend.dto.exam.fill_in_blank.FillInBlankOptionDto;
import com.example.lmsbackend.dto.exam.fill_in_blank.FillInBlankQuestionDto;
import com.example.lmsbackend.exceptions.exam.QuestionHasNotBeenAnsweredCompletedException;
import com.example.lmsbackend.exceptions.exam.QuestionNotFoundException;
import com.example.lmsbackend.exceptions.exam.UnsupportedMatchStrategyException;
import com.example.lmsbackend.mapper.exam.fill_in_blank.FillInBlankOptionMapper;
import com.example.lmsbackend.mapper.exam.fill_in_blank.FillInBlankQuestionMapper;
import com.example.lmsbackend.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class FillInBlankQuestionOperation extends AbstractQuestionOperation {

    private final FillInBlankQuestionMapper mapper;
    private final FillInBlankOptionMapper blankMapper;
    private final FillInBlankQuestionRepository repository;
    private final FillInBlankOptionRepository blankRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    public FillInBlankQuestionOperation(QuestionRepository questionRepository,
                                        QuestionSourceRepository questionSourceRepository,
                                        FillInBlankQuestionRepository repository,
                                        TextbookRepository textbookRepository,
                                        FillInBlankQuestionMapper mapper,
                                        FillInBlankOptionMapper blankMapper,
                                        EntityManager entityManager,
                                        FillInBlankOptionRepository blankRepository) {
        super(questionRepository, questionSourceRepository, textbookRepository, QuestionType.FILL_IN_BLANK.getType());
        this.mapper = mapper;
        this.repository = repository;
        this.blankMapper = blankMapper;
        this.entityManager = entityManager;
        this.blankRepository = blankRepository;
    }

    @Override
    protected Question mapToEntity(QuestionDto dto) {
        return mapper.mapToFillInBlankQuestionEntity((FillInBlankQuestionDto) dto);
    }

    @Override
    protected void persistQuestion(QuestionDto dto, QuestionEntity question) {
        var fillInBlankQuestion = (FillInBlankQuestionEntity) mapToEntity(dto);
        fillInBlankQuestion.setQuestion(question);
        question.setFillInBlankQuestion(fillInBlankQuestion);

        var blanks = fillInBlankQuestion.getBlanks();
        blanks
                .forEach(blank -> blank.setQuestion(fillInBlankQuestion));

        repository.save(fillInBlankQuestion);
    }

    @Override
    public Question getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException(id, QuestionType.FILL_IN_BLANK.getType()));
    }

    @Override
    protected QuestionDto mapToSpecificQuestionResponseDto(Question question) {
        return mapper.mapToFillInBlankQuestionDto((FillInBlankQuestionEntity) question);
    }

    @Override
    protected QuestionDto mapToSpecificQuestionResponseDtoIgnoreAnswer(Question question) {
        return mapper.mapToFillInBlankQuestionDtoIgnoreAnswer((FillInBlankQuestionEntity) question);
    }

    @Override
    protected QuestionDto mapToSpecificQuestionResponseDto(QuestionEntity question) {
        return mapToSpecificQuestionResponseDto(question.getFillInBlankQuestion());
    }

    @Override
    public void grade(QuizSessionResultQuestionEntity answerQuestion) {
        var answer = answerQuestion.getAnswer();
        if (answer == null) {
            answerQuestion.setEarnedPoint(0.0);
            return;
        }
        var question = (FillInBlankQuestionEntity) getById(answer.getQuestion().getId());
        var point = question.getQuestion().getPoint();
        var blanks = question.getBlanks();
        var studentAnswers = answer.getValues();
        if (blanks.size() != studentAnswers.size()) {
            log.warn("Session {} has question {} that has not been fully answered", answer.getSession().getId(), question.getId());
            throw new QuestionHasNotBeenAnsweredCompletedException(answer.getSession().getId(), question.getId());
        }
        studentAnswers
                .forEach(studentAnswer -> {
                    var blank = blanks.stream()
                            .filter(b -> Objects.equals(b.getOrder(), studentAnswer.getOrder()))
                            .findAny()
                            .orElseThrow(() -> new QuestionHasNotBeenAnsweredCompletedException(answer.getSession().getId(), question.getId()));
                    checkResult(studentAnswer, blank);
                });
        long numberOfCorrectAnswers = studentAnswers.stream()
                .filter(x -> BooleanUtils.isTrue(x.getIsCorrect()))
                .count();
        answerQuestion.setEarnedPoint((double) point / blanks.size() * numberOfCorrectAnswers);
    }

    @Override
    public QuestionAnswerDto mapToQuestionAnswerResponse(Question question, QuizSessionResultQuestionEntity resultForQuestion) {
        var fillInBlankQuestion = (FillInBlankQuestionEntity) question;
        var dto = mapper.mapToFillInBlankQuestionAnswerDto(fillInBlankQuestion);
        Set<AnswerTemporaryEntity> studentAnswers = new HashSet<>();
        if (resultForQuestion.getAnswer() != null) {
            studentAnswers = resultForQuestion.getAnswer().getValues();
        }
        var blanksDto = mapBlanks(fillInBlankQuestion, studentAnswers);
        dto.setResultAnswerList(blanksDto);
        dto.setEarnedPoint(resultForQuestion.getEarnedPoint());
        dto.setPoint(fillInBlankQuestion.getQuestion().getPoint());
        dto.setDescription(fillInBlankQuestion.getQuestion().getDescription());
        dto.setAttachment(fillInBlankQuestion.getQuestion().getAttachment());
        return dto;
    }

    @Override
    public QuestionDto updateQuestion(Long id, QuestionDto dto) {
        var fillInBlankQuestionDto = (FillInBlankQuestionDto) dto;
        var question = (FillInBlankQuestionEntity) getByIdFromQuestion(id, QuestionEntity.Fields.fillInBlankQuestion);
        var blanksIdDto = fillInBlankQuestionDto.getBlankList()
                .stream()
                .map(FillInBlankOptionDto::getId)
                .collect(toList());
        entityManager.detach(question);
        var tobeRemovedBlanksId = question.getBlanks()
                .stream()
                .filter(blank -> blanksIdDto.stream()
                        .noneMatch(blankId -> id == blank.getId()))
                .map(FillInBlankOptionEntity::getId)
                .collect(toList());

        mapper.mapToFillInBlankQuestionEntity(question, fillInBlankQuestionDto);
        question.getQuestion().setPoint(dto.getPoint());
        question.getBlanks()
                .forEach(blank -> blank.setQuestion(question));

        var managedEntity = entityManager.merge(question);
        repository.save(managedEntity);

        question.setBlanks(managedEntity.getBlanks().stream()
                .sorted(Comparator.comparingInt(FillInBlankOptionEntity::getOrder))
                .collect(Collectors.toCollection(LinkedHashSet::new)));

        blankRepository.deleteAllById(tobeRemovedBlanksId);
        return mapper.mapToFillInBlankQuestionDto(question);
    }

    @Override
    public Question getQuestionByTypeFromQuestionEntity(QuestionEntity entity) {
        return entity.getFillInBlankQuestion();
    }

    private List<FillInBlankOptionAnswerDto> mapBlanks(FillInBlankQuestionEntity fillInBlankQuestion,
                                                       Set<AnswerTemporaryEntity> studentAnswers) {
        return fillInBlankQuestion.getBlanks()
                .stream()
                .map(blank -> {
                    var blankAnswer = blankMapper.mapToFillInBlankBlankAnswerDto(blank);
                    var studentAnswerOpt = studentAnswers.stream()
                            .filter(s -> s.getOrder() == blank.getOrder())
                            .findFirst();
                    if (studentAnswerOpt.isPresent()) {
                        var studentAnswer = studentAnswerOpt.get();
                        blankAnswer.setStudentAnswer(studentAnswer.getValue());
                        blankAnswer.setIsCorrect(studentAnswer.getIsCorrect());
                    } else {
                        blankAnswer.setIsCorrect(false);
                    }
                    return blankAnswer;
                })
                .collect(toList());
    }

    private void checkResult(AnswerTemporaryEntity studentAnswer, FillInBlankOptionEntity blank) {
        switch (blank.getMatchStrategy()) {
            case EXACT:
                studentAnswer.setIsCorrect(StringUtils.equals(studentAnswer.getValue(), blank.getExpectedAnswer()));
                break;
            case CONTAIN:
                studentAnswer.setIsCorrect(StringUtils.contains(blank.getExpectedAnswer(), studentAnswer.getValue()));
                break;
            case REGEX:
                studentAnswer.setIsCorrect(studentAnswer.getValue().matches(blank.getExpectedAnswer()));
                break;
            default:
                throw new UnsupportedMatchStrategyException(blank.getMatchStrategy().getType());
        }
    }

    @Override
    public QuestionDto mapToQuestionAnswerResponse(Question question, AnswerQuestionTemporaryEntity answer, UUID sessionId) {
        var fillInBlankQuestion = (FillInBlankQuestionEntity) question;
        var dto = mapper.mapToFillInBlankQuestionDtoIgnoreAnswer(fillInBlankQuestion);
        if (answer == null) {
            return dto;
        }
        dto.getBlankList()
                .forEach(blank -> {
                    var opt = answer.getValues().stream()
                            .filter(v -> Objects.equals(v.getOrder(), blank.getOrder()))
                            .findAny();
                    opt.ifPresent(v -> blank.setStudentAnswer(v.getValue()));
                });

        return dto;
    }
}
