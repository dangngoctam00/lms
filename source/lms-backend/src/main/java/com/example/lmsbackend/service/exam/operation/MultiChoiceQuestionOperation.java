package com.example.lmsbackend.service.exam.operation;

import com.example.lmsbackend.domain.exam.AnswerQuestionTemporaryEntity;
import com.example.lmsbackend.domain.exam.AnswerTemporaryEntity;
import com.example.lmsbackend.domain.exam.QuizSessionResultQuestionEntity;
import com.example.lmsbackend.domain.exam.base_question.Question;
import com.example.lmsbackend.domain.exam.base_question.QuestionEntity;
import com.example.lmsbackend.domain.exam.base_question.QuestionType;
import com.example.lmsbackend.domain.exam.multi_choice.MultiChoiceOptionEntity;
import com.example.lmsbackend.domain.exam.multi_choice.MultiChoiceQuestionEntity;
import com.example.lmsbackend.dto.exam.QuestionAnswerDto;
import com.example.lmsbackend.dto.exam.QuestionDto;
import com.example.lmsbackend.dto.exam.multi_choice.MultiChoiceOptionAnswerDto;
import com.example.lmsbackend.dto.exam.multi_choice.MultiChoiceOptionDto;
import com.example.lmsbackend.dto.exam.multi_choice.MultiChoicesQuestionDto;
import com.example.lmsbackend.exceptions.exam.QuestionNotFoundException;
import com.example.lmsbackend.mapper.exam.multi_choice.MultiChoiceOptionMapper;
import com.example.lmsbackend.mapper.exam.multi_choice.MultiChoicesQuestionMapper;
import com.example.lmsbackend.repository.*;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class MultiChoiceQuestionOperation extends AbstractQuestionOperation {

    private final MultiChoicesQuestionMapper mapper;
    private final MultiChoiceOptionMapper optionMapper;
    private final MultiChoiceQuestionRepository repository;
    private final MultiChoiceOptionRepository optionRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    public MultiChoiceQuestionOperation(QuestionRepository questionRepository,
                                        QuestionSourceRepository questionSourceRepository,
                                        MultiChoiceQuestionRepository repository,
                                        TextbookRepository textbookRepository,
                                        MultiChoicesQuestionMapper mapper,
                                        MultiChoiceOptionMapper optionMapper,
                                        EntityManager entityManager,
                                        MultiChoiceOptionRepository optionRepository) {
        super(questionRepository, questionSourceRepository, textbookRepository, QuestionType.MULTI_CHOICES.getType());
        this.mapper = mapper;
        this.repository = repository;
        this.optionMapper = optionMapper;
        this.entityManager = entityManager;
        this.optionRepository = optionRepository;
    }

    @Override
    protected Question mapToEntity(QuestionDto dto) {
        return mapper.mapToMultiChoicesQuestionEntity((MultiChoicesQuestionDto) dto);
    }

    @Override
    protected void persistQuestion(QuestionDto dto, QuestionEntity question) {
        var multiChoicesQuestion = (MultiChoiceQuestionEntity) mapToEntity(dto);
        multiChoicesQuestion.setQuestion(question);
        question.setMultiChoiceQuestion(multiChoicesQuestion);

        multiChoicesQuestion.getOptions()
                .forEach(option -> option.setQuestion(multiChoicesQuestion));

        repository.save(multiChoicesQuestion);
    }

    @Override
    public Question getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException(id, QuestionType.MULTI_CHOICES.getType()));
    }

    @Override
    protected QuestionDto mapToSpecificQuestionResponseDto(Question question) {
        return mapper.mapToMultiChoicesQuestionDto((MultiChoiceQuestionEntity) question);
    }

    @Override
    protected QuestionDto mapToSpecificQuestionResponseDtoIgnoreAnswer(Question question) {
        return mapper.mapToMultiChoiceQuestionDtoIgnoreAnswer((MultiChoiceQuestionEntity) question);
    }

    @Override
    protected QuestionDto mapToSpecificQuestionResponseDto(QuestionEntity question) {
        return mapToSpecificQuestionResponseDto(question.getMultiChoiceQuestion());
    }

    @Override
    public void grade(QuizSessionResultQuestionEntity answerQuestion) {
        var answer = answerQuestion.getAnswer();
        if (answer == null) {
            answerQuestion.setEarnedPoint(0.0);
            return;
        }
        var question = (MultiChoiceQuestionEntity) getById(answer.getQuestion().getId());
        var point = question.getQuestion().getPoint();
        var options = question.getOptions();
        var studentAnswers = answer.getValues();

        studentAnswers.forEach(ans -> {
            var isCorrectAnswer = options.stream()
                    .anyMatch(o -> o.getAnswerKey().equals(Integer.valueOf(ans.getValue())) && o.getIsCorrect());
            ans.setIsCorrect(isCorrectAnswer);
        });
        long numberOfCorrectAnswers = studentAnswers.stream()
                .filter(ans -> BooleanUtils.isTrue(ans.getIsCorrect()))
                .count();
        long numberOfInCorrectAnswers = studentAnswers.size() - numberOfCorrectAnswers;
        var diff = numberOfInCorrectAnswers - numberOfCorrectAnswers;
        if (diff == 0) {
            answerQuestion.setEarnedPoint(0.0);
        } else if (diff > 0) {
            answerQuestion.setEarnedPoint(0.0);
        } else {
            answerQuestion.setEarnedPoint((double) point / (numberOfCorrectAnswers - numberOfInCorrectAnswers));
        }
    }

    @Override
    public QuestionAnswerDto mapToQuestionAnswerResponse(Question question, QuizSessionResultQuestionEntity resultForQuestion) {
        var multiChoiceQuestion = (MultiChoiceQuestionEntity) question;
        List<String> studentAnswersKey = new ArrayList<>();
        var dto = mapper.mapToMultiChoiceQuestionAnswerDto(multiChoiceQuestion);
        if (resultForQuestion.getAnswer() != null) {
            studentAnswersKey = resultForQuestion.getAnswer()
                    .getValues()
                    .stream()
                    .map(AnswerTemporaryEntity::getValue)
                    .collect(toList());
        }
        var optionsDto = mapOptions(multiChoiceQuestion, studentAnswersKey);
        dto.setResultAnswerList(optionsDto);
        dto.setEarnedPoint(resultForQuestion.getEarnedPoint());
        dto.setPoint(multiChoiceQuestion.getQuestion().getPoint());
        dto.setDescription(multiChoiceQuestion.getQuestion().getDescription());
        dto.setAttachment(multiChoiceQuestion.getQuestion().getAttachment());
        return dto;
    }

    @Override
    public QuestionDto updateQuestion(Long id, QuestionDto dto) {
        var multiChoiceQuestionDto = (MultiChoicesQuestionDto) dto;
        var question = (MultiChoiceQuestionEntity) getByIdFromQuestion(id, QuestionEntity.Fields.multiChoiceQuestion);
        var optionsId = multiChoiceQuestionDto.getAnswerList()
                .stream()
                .filter(option -> option.getId() != null)
                .map(MultiChoiceOptionDto::getId)
                .collect(toList());

        entityManager.detach(question);
        var options = question.getOptions();
        var tobeRemovedOptionsId = options.stream()
                .filter(option -> optionsId.stream()
                        .noneMatch(i -> option.getId() == i))
                .map(MultiChoiceOptionEntity::getId)
                .collect(toList());

        mapper.mapToMultiChoicesQuestionEntity(question, multiChoiceQuestionDto);
        question.getQuestion().setPoint(dto.getPoint());

        question.getOptions()
                .forEach(option -> option.setQuestion(question));
        var managedEntity = entityManager.merge(question);

        question.setOptions(
                managedEntity.getOptions().stream()
                        .sorted(Comparator.comparingInt(MultiChoiceOptionEntity::getOrder))
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );

        repository.save(managedEntity);

        optionRepository.deleteAllById(tobeRemovedOptionsId);
        return mapper.mapToMultiChoicesQuestionDto(question);
    }

    @Override
    public Question getQuestionByTypeFromQuestionEntity(QuestionEntity entity) {
        return entity.getMultiChoiceQuestion();
    }

    private List<MultiChoiceOptionAnswerDto> mapOptions(MultiChoiceQuestionEntity multiChoiceQuestion, List<String> studentAnswersKey) {
        return multiChoiceQuestion.getOptions()
                .stream()
                .map(option -> {
                    var optionDto = optionMapper.mapToMultiChoiceOptionAnswerDto(option);
                    optionDto.setIsChosenByStudent(studentAnswersKey.stream().anyMatch(s -> StringUtils.equals(s, optionDto.getAnswerKey().toString())));
                    return optionDto;
                })
                .collect(toList());
    }

    @Override
    public QuestionDto mapToQuestionAnswerResponse(Question question, AnswerQuestionTemporaryEntity studentAnswer, UUID sessionId) {
        var dto = mapper.mapToMultiChoiceQuestionDtoIgnoreAnswer((MultiChoiceQuestionEntity) question);
        dto.setAnswerList(dto.getAnswerList()
                .stream()
                .sorted(Comparator.comparingInt(MultiChoiceOptionDto::getOrder))
                .collect(toList()));
        if (studentAnswer == null) {
            return dto;
        }
        dto.getAnswerList().forEach(answer -> {
            answer.setIsChosenByStudent(studentAnswer.getValues().stream()
                    .anyMatch(s -> Objects.equals(answer.getAnswerKey(), Integer.valueOf(s.getValue()))));
        });
        return dto;
    }
}
