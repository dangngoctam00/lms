package com.example.lmsbackend.service.exam.operation;

import com.example.lmsbackend.domain.exam.AnswerQuestionTemporaryEntity;
import com.example.lmsbackend.domain.exam.AnswerTemporaryEntity;
import com.example.lmsbackend.domain.exam.QuizSessionResultQuestionEntity;
import com.example.lmsbackend.domain.exam.base_question.Question;
import com.example.lmsbackend.domain.exam.base_question.QuestionEntity;
import com.example.lmsbackend.domain.exam.base_question.QuestionType;
import com.example.lmsbackend.domain.exam.fill_in_blank_with_choices.FillInBlankMultiChoiceBlankEntity;
import com.example.lmsbackend.domain.exam.fill_in_blank_with_choices.FillInBlankMultiChoiceOptionEntity;
import com.example.lmsbackend.domain.exam.fill_in_blank_with_choices.FillInBlankMultiChoiceQuestionEntity;
import com.example.lmsbackend.dto.exam.QuestionAnswerDto;
import com.example.lmsbackend.dto.exam.QuestionDto;
import com.example.lmsbackend.dto.exam.fill_in_blank_multi_choices.FillInBlankMultiChoiceQuestionDto;
import com.example.lmsbackend.dto.exam.fill_in_blank_multi_choices.FillInBlankMultiChoicesBlankAnswerDto;
import com.example.lmsbackend.dto.exam.fill_in_blank_multi_choices.FillInBlankMultiChoicesBlankDto;
import com.example.lmsbackend.exceptions.exam.QuestionHasNotBeenAnsweredCompletedException;
import com.example.lmsbackend.exceptions.exam.QuestionNotFoundException;
import com.example.lmsbackend.mapper.exam.fill_in_bank_multi_choices.FillInBlankMultiChoiceBlankMapper;
import com.example.lmsbackend.mapper.exam.fill_in_bank_multi_choices.FillInBlankMultiChoiceOptionMapper;
import com.example.lmsbackend.mapper.exam.fill_in_bank_multi_choices.FillInBlankMultiChoiceQuestionMapper;
import com.example.lmsbackend.repository.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class FillInBlankMultiChoiceQuestionOperation extends AbstractQuestionOperation {

    private final FillInBlankMultiChoiceQuestionMapper mapper;
    private final FillInBlankMultiChoiceBlankMapper blankMapper;
    private final FillInBlankMultiChoiceOptionMapper optionMapper;
    private final FillInBlankMultiChoiceQuestionRepository repository;
    private final FillInBlankMultiChoiceBlankRepository blankRepository;
    private final FillInBlankMultiChoiceOptionRepository optionRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    public FillInBlankMultiChoiceQuestionOperation(QuestionRepository questionRepository,
                                                   QuestionSourceRepository questionSourceRepository,
                                                   FillInBlankMultiChoiceQuestionRepository repository,
                                                   TextbookRepository textbookRepository,
                                                   FillInBlankMultiChoiceQuestionMapper mapper,
                                                   FillInBlankMultiChoiceBlankMapper blankMapper,
                                                   FillInBlankMultiChoiceOptionMapper optionMapper,
                                                   FillInBlankMultiChoiceBlankRepository blankRepository,
                                                   FillInBlankMultiChoiceOptionRepository optionRepository,
                                                   EntityManager entityManager) {
        super(questionRepository, questionSourceRepository, textbookRepository, QuestionType.FILL_IN_BLANK_WITH_CHOICES.getType());
        this.mapper = mapper;
        this.repository = repository;
        this.blankMapper = blankMapper;
        this.optionMapper = optionMapper;
        this.blankRepository = blankRepository;
        this.optionRepository = optionRepository;
        this.entityManager = entityManager;
    }

    @Override
    protected Question mapToEntity(QuestionDto dto) {
        return mapper.mapToFillInBlankMultiChoiceEntity((FillInBlankMultiChoiceQuestionDto) dto);
    }

    @Override
    protected void persistQuestion(QuestionDto dto, QuestionEntity question) {
        var fillInBlankMultiChoiceQuestion = (FillInBlankMultiChoiceQuestionEntity) mapToEntity(dto);
        fillInBlankMultiChoiceQuestion.setQuestion(question);
        question.setFillInBlankMultiChoiceQuestion(fillInBlankMultiChoiceQuestion);

        fillInBlankMultiChoiceQuestion.getBlanks()
                .forEach(blank -> {
                    blank.setQuestion(fillInBlankMultiChoiceQuestion);
                    blank.getOptions()
                            .forEach(option -> option.setBlank(blank));
                });

        repository.save(fillInBlankMultiChoiceQuestion);
    }

    @Override
    public Question getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException(id, QuestionType.FILL_IN_BLANK_WITH_CHOICES.getType()));
    }

    @Override
    protected QuestionDto mapToSpecificQuestionResponseDto(Question question) {
        return mapper.mapToFillInBlankMultiChoiceDto((FillInBlankMultiChoiceQuestionEntity) question);
    }

    @Override
    protected QuestionDto mapToSpecificQuestionResponseDtoIgnoreAnswer(Question question) {
        return mapper.mapToFillInBlankMultiChoiceDtoIgnoreAnswer((FillInBlankMultiChoiceQuestionEntity) question);
    }

    @Override
    protected QuestionDto mapToSpecificQuestionResponseDto(QuestionEntity question) {
        return mapToSpecificQuestionResponseDto(question.getFillInBlankMultiChoiceQuestion());
    }

    @Override
    public void grade(QuizSessionResultQuestionEntity answerQuestion) {
        var answer = answerQuestion.getAnswer();
        if (answer == null) {
            answerQuestion.setEarnedPoint(0.0);
            return;
        }
        var question = (FillInBlankMultiChoiceQuestionEntity) getById(answer.getQuestion().getId());
        var point = question.getQuestion().getPoint();
        var blanks = question.getBlanks();
        var studentAnswers = answer.getValues();
        blanks.forEach(blank -> {
            var studentAnswer = studentAnswers
                    .stream()
                    .filter(s -> Objects.equals(s.getOrder(), blank.getOrder()))
                    .findAny()
                    .orElseThrow(() -> new QuestionHasNotBeenAnsweredCompletedException(answer.getSession().getId(), question.getId()));
            if (StringUtils.isNumeric(studentAnswer.getValue()) && Objects.equals(blank.getCorrectAnswerKey(), Integer.valueOf(studentAnswer.getValue()))) {
                studentAnswer.setIsCorrect(true);
            } else {
                studentAnswer.setIsCorrect(false);
            }
        });
        long numberOfCorrectAnswers = studentAnswers.stream()
                .filter(AnswerTemporaryEntity::getIsCorrect)
                .count();
        answerQuestion.setEarnedPoint((double) point / (blanks.size()) * numberOfCorrectAnswers);
    }

    @Override
    public QuestionAnswerDto mapToQuestionAnswerResponse(Question question, QuizSessionResultQuestionEntity resultForQuestion) {
        var fillInBlankMultiChoice = (FillInBlankMultiChoiceQuestionEntity) question;
        var dto = mapper.mapToFillInBlankMultiChoiceQuestionAnswerDto(fillInBlankMultiChoice);
        Set<AnswerTemporaryEntity> studentAnswers = new HashSet<>();
        if (resultForQuestion.getAnswer() != null) {
            studentAnswers = resultForQuestion.getAnswer().getValues();
        }
        var blanksDto = mapBlanks(fillInBlankMultiChoice, studentAnswers);
        dto.setResultAnswerList(blanksDto);
        dto.setEarnedPoint(resultForQuestion.getEarnedPoint());
        dto.setPoint(fillInBlankMultiChoice.getQuestion().getPoint());
        dto.setDescription(fillInBlankMultiChoice.getQuestion().getDescription());
        dto.setAttachment(fillInBlankMultiChoice.getQuestion().getAttachment());
        return dto;
    }

    @Override
    public QuestionDto updateQuestion(Long id, QuestionDto dto) {
        var fillInBlankMultiChoiceDto = (FillInBlankMultiChoiceQuestionDto) dto;
        var question = (FillInBlankMultiChoiceQuestionEntity) getByIdFromQuestion(id, QuestionEntity.Fields.fillInBlankMultiChoiceQuestion);
        var blanksIdDto = fillInBlankMultiChoiceDto.getBlankList()
                .stream()
                .map(FillInBlankMultiChoicesBlankDto::getId)
                .filter(blankId -> blankId != null)
                .collect(toList());

        entityManager.detach(question);
        var tobeRemovedBlanksId = question.getBlanks()
                .stream()
                .filter(blank -> blanksIdDto.stream()
                        .noneMatch(blankId -> Objects.equals(id, blank.getId())))
                .map(FillInBlankMultiChoiceBlankEntity::getId)
                .collect(toList());

        mapper.mapToFillInBlankMultiChoiceEntity(question, fillInBlankMultiChoiceDto);
        question.getQuestion().setPoint(dto.getPoint());

        question.getBlanks()
                .forEach(blank -> {
                    blank.getOptions()
                            .forEach(option -> option.setBlank(blank));
                    blank.setQuestion(question);
                });

        var managedEntity = entityManager.merge(question);
//        repository.save(managedEntity);
        entityManager.flush();

        entityManager.detach(managedEntity);

        managedEntity.setBlanks(managedEntity.getBlanks().stream()
                .map(blank -> {
                    blank.setOptions(
                            blank.getOptions()
                                    .stream()
                                    .sorted(Comparator.comparingInt(FillInBlankMultiChoiceOptionEntity::getOrder))
                                    .collect(Collectors.toCollection(LinkedHashSet::new))
                    );
                    return blank;
                })
                .sorted(Comparator.comparingInt(FillInBlankMultiChoiceBlankEntity::getOrder))
                .collect(Collectors.toCollection(LinkedHashSet::new)));

        entityManager.flush();
        entityManager.clear();
        entityManager.createQuery("DELETE FROM FillInBlankMultiChoiceBlankEntity f WHERE f.id IN (:ids)")
                .setParameter("ids", tobeRemovedBlanksId)
                .executeUpdate();


        return mapper.mapToFillInBlankMultiChoiceDto(managedEntity);
    }

    @Override
    public Question getQuestionByTypeFromQuestionEntity(QuestionEntity entity) {
        return entity.getFillInBlankMultiChoiceQuestion();
    }

    private List<FillInBlankMultiChoicesBlankAnswerDto> mapBlanks(FillInBlankMultiChoiceQuestionEntity fillInBlankMultiChoice,
                                                                  Set<AnswerTemporaryEntity> studentAnswers) {
        return fillInBlankMultiChoice.getBlanks()
                .stream()
                .map(blank -> {
                    var blankDto = blankMapper.mapToFillInBlankMultiChoiceBlankAnswerDto(blank);
                    var studentAnswerOpt = studentAnswers.stream()
                            .filter(s -> Objects.equals(s.getOrder(), blank.getOrder()))
                            .findFirst();
                    if (studentAnswerOpt.isPresent() && StringUtils.isNumeric(studentAnswerOpt.get().getValue())) {
                        var studentAnswer = studentAnswerOpt.get();
                        blankDto.setStudentAnswerKey(Integer.valueOf(studentAnswer.getValue()));
                        blankDto.setIsCorrect(studentAnswer.getIsCorrect());
                    } else {
                        blankDto.setIsCorrect(false);
                    }
                    blankDto.setCorrectAnswerKey(blank.getCorrectAnswerKey());
                    blankDto.setAnswerList(blank.getOptions()
                            .stream()
                            .map(optionMapper::mapToFillInBlankMultiChoiceOptionDto)
                            .collect(toList()));
                    return blankDto;
                })
                .collect(toList());
    }

    @Override
    public QuestionDto mapToQuestionAnswerResponse(Question question, AnswerQuestionTemporaryEntity answer, UUID sessionId) {
        var dto = mapper.mapToFillInBlankMultiChoiceDtoIgnoreAnswer((FillInBlankMultiChoiceQuestionEntity) question);
        if (answer == null) {
            return dto;
        }
        dto.getBlankList()
                .forEach(blank -> {
                    var opt = answer.getValues()
                            .stream()
                            .filter(x -> Objects.equals(x.getOrder(), blank.getOrder()))
                            .findAny();
                    if (opt.isPresent() && StringUtils.isNumeric(opt.get().getValue())) {
                        blank.setStudentAnswer(Integer.valueOf(opt.get().getValue()));
                    }
                });
        return dto;
    }
}
