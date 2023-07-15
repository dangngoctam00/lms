package com.example.lmsbackend.service.exam.operation;

import com.example.lmsbackend.domain.exam.AnswerQuestionTemporaryEntity;
import com.example.lmsbackend.domain.exam.QuizSessionEntity;
import com.example.lmsbackend.domain.exam.QuizSessionResultEntity;
import com.example.lmsbackend.domain.exam.QuizSessionResultQuestionEntity;
import com.example.lmsbackend.domain.exam.base_question.GroupQuestionEntity;
import com.example.lmsbackend.domain.exam.base_question.Question;
import com.example.lmsbackend.domain.exam.base_question.QuestionEntity;
import com.example.lmsbackend.dto.exam.QuestionAnswerDto;
import com.example.lmsbackend.dto.exam.QuestionDto;
import com.example.lmsbackend.dto.exam.group.GroupQuestionDto;
import com.example.lmsbackend.exceptions.exam.QuestionNotFoundException;
import com.example.lmsbackend.mapper.exam.group.GroupQuestionMapper;
import com.example.lmsbackend.repository.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class GroupQuestionOperation extends AbstractQuestionOperation {

    private final GroupQuestionRepository repository;
    private final GroupQuestionMapper mapper;
    private final SessionResultQuestionRepository sessionResultQuestionRepository;

    private final QuestionOperationFactory questionOperationFactory;
    private final AnswerQuestionTemporaryRepository answerQuestionTemporaryRepository;

    private final EntityManager entityManager;

    public GroupQuestionOperation(QuestionRepository questionRepository,
                                  QuestionSourceRepository questionSourceRepository,
                                  GroupQuestionRepository repository,
                                  TextbookRepository textbookRepository,
                                  GroupQuestionMapper mapper,
                                  @Lazy QuestionOperationFactory questionOperationFactory,
                                  SessionResultQuestionRepository sessionResultQuestionRepository,
                                  EntityManager entityManager,
                                  AnswerQuestionTemporaryRepository answerQuestionTemporaryRepository) {
        super(questionRepository, questionSourceRepository, textbookRepository, "GROUP");
        this.repository = repository;
        this.mapper = mapper;
        this.questionOperationFactory = questionOperationFactory;
        this.sessionResultQuestionRepository = sessionResultQuestionRepository;
        this.entityManager = entityManager;
        this.answerQuestionTemporaryRepository = answerQuestionTemporaryRepository;
    }

    @Override
    protected Question mapToEntity(QuestionDto dto) {
        return mapper.mapToGroupQuestionEntity((GroupQuestionDto) dto);
    }

    @Override
    protected void persistQuestion(QuestionDto dto, QuestionEntity question) {
        var groupQuestions = (GroupQuestionEntity) mapToEntity(dto);
        groupQuestions.setQuestion(question);
        question.setGroupQuestion(groupQuestions);

        repository.save(groupQuestions);
    }

    @Override
    protected QuestionDto mapToSpecificQuestionResponseDto(Question question) {
        var groupQuestions = (GroupQuestionEntity) question;
        var questionsIdAndType = groupQuestions.getQuestions()
                .stream()
                .map(q -> new QuestionByIdAndType(q.getQuestion().getId(), q.getQuestion().getType()))
                .collect(toList());
        var questionsDto = questionsIdAndType
                .stream()
                .map(q -> {
                    var questionOperation = questionOperationFactory.getQuestionOperation(q.getType());
                    return questionOperation.mapToQuestionResponseDto(questionOperation.getById(q.getId()));
                })
                .collect(toList());
        var questionGroupDto = mapper.mapToGroupQuestionDto(groupQuestions);
        questionGroupDto.setQuestionList(questionsDto);
        return questionGroupDto;
    }

    @Override
    protected QuestionDto mapToSpecificQuestionResponseDtoIgnoreAnswer(Question question) {
        var groupQuestions = (GroupQuestionEntity) question;
        var questionsIdAndType = groupQuestions.getQuestions()
                .stream()
                .map(q -> new QuestionByIdAndType(q.getQuestion().getId(), q.getQuestion().getType()))
                .collect(toList());
        var questionsDto = questionsIdAndType
                .stream()
                .map(q -> {
                    var questionOperation = questionOperationFactory.getQuestionOperation(q.getType());
                    return questionOperation.mapToQuestionResponseDtoIgnoreAnswer(questionOperation.getById(q.getId()));
                })
                .collect(toList());
        var questionGroupDto = mapper.mapToGroupQuestionDto(groupQuestions);
        questionGroupDto.setQuestionList(questionsDto);
        return questionGroupDto;
    }

    @Override
    protected QuestionDto mapToSpecificQuestionResponseDto(QuestionEntity question) {
        return mapToSpecificQuestionResponseDto(question.getGroupQuestion());
    }

    @Override
    public Question getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException(id, "GROUP"));
    }

    @Override
    public void grade(QuizSessionResultQuestionEntity answerQuestion) {
        var groupQuestions = (GroupQuestionEntity) getById(answerQuestion.getQuestion().getId());
        var questionsInGroup = groupQuestions.getQuestions()
                .stream()
                .map(q -> q.getQuestion())
                .collect(toList());
        var sessionResult = answerQuestion.getSessionResult();
        var session = sessionResult.getSession();
        var studentAnswers = session.getAnswers();
        grade(questionsInGroup, studentAnswers, sessionResult, answerQuestion);
    }

    public void grade(QuizSessionResultQuestionEntity answerQuestion, QuizSessionEntity session) {
        var groupQuestions = (GroupQuestionEntity) getById(answerQuestion.getQuestion().getId());
        var questionsInGroup = groupQuestions.getQuestions()
                .stream()
                .map(q -> q.getQuestion())
                .collect(toList());
        var sessionResult = answerQuestion.getSessionResult();
        var studentAnswers = session.getAnswers();
        grade(questionsInGroup, studentAnswers, sessionResult, answerQuestion);

    }

    private void grade(List<QuestionEntity> questionsInGroup, Set<AnswerQuestionTemporaryEntity> studentAnswers, QuizSessionResultEntity sessionResult, QuizSessionResultQuestionEntity answerQuestion) {
        var resultQuestions = questionsInGroup.stream().map(question -> {
                    var answerForQuestionOpt = studentAnswers.stream()
                            .filter(studentAnswer -> Objects.equals(studentAnswer.getQuestion().getId(), question.getId()))
                            .findFirst();
                    var resultQuestion = new QuizSessionResultQuestionEntity();
                    resultQuestion.setQuestion(question);
                    resultQuestion.setSessionResult(sessionResult);
                    if (answerForQuestionOpt.isPresent()) {
                        var answerForQuestion = answerForQuestionOpt.get();
                        resultQuestion.setAnswer(answerForQuestion);
                    }
                    var questionOperation = questionOperationFactory.getQuestionOperation(question.getType());
                    questionOperation.grade(resultQuestion);
                    return resultQuestion;
                })
                .collect(toList());
        var totalGrade = resultQuestions.stream()
                .map(QuizSessionResultQuestionEntity::getEarnedPoint)
                .filter(Objects::nonNull)
                .reduce(0.0, Double::sum);
        answerQuestion.setEarnedPoint(totalGrade);
    }

    @Override
    public QuestionAnswerDto mapToQuestionAnswerResponse(Question question,
                                                         QuizSessionResultQuestionEntity resultForQuestionGroup) {
        var groupQuestions = (GroupQuestionEntity) question;
        var resultSession = resultForQuestionGroup.getSessionResult();
        var dto = mapper.mapToGroupQuestionAnswerDto(groupQuestions);
        var questionsInGroup = groupQuestions.getQuestions();
        var questionsId = questionsInGroup.stream()
                .map(q -> q.getQuestion().getId())
                .collect(toList());
        var resultForQuestions = sessionResultQuestionRepository.findByQuestionAndSessionResult(questionsId, resultSession.getId());
        var answerList = questionsInGroup.stream()
                .map(questionInGroup -> {
                    var baseQuestion = questionInGroup.getQuestion();
                    var questionOperation = questionOperationFactory.getQuestionOperation(baseQuestion.getType());
                    var specificQuestion = questionOperation.getById(baseQuestion.getId());
                    var resultForQuestion = resultForQuestions
                            .stream()
                            .filter(r -> Objects.equals(r.getQuestion().getId(), baseQuestion.getId()))
                            .findAny()
                            .orElse(null);
                    var questionAnswerDto = questionOperation.mapToQuestionAnswerResponse(specificQuestion, resultForQuestion);
                    questionAnswerDto.setOrder(questionInGroup.getOrder());
                    questionAnswerDto.setType(baseQuestion.getType());
                    return questionAnswerDto;
                })
                .collect(toList());
        dto.setAnswerList(answerList);
        dto.setEarnedPoint(answerList.stream()
                .filter(x -> x.getEarnedPoint() != null)
                .mapToDouble(QuestionAnswerDto::getEarnedPoint)
                .reduce(Double::sum)
                .orElse(0));
        dto.setPoint(groupQuestions.getQuestion().getPoint());
        dto.setDescription(groupQuestions.getQuestion().getDescription());
        dto.setAttachment(groupQuestions.getQuestion().getAttachment());
        return dto;
    }

    @Override
    public QuestionDto updateQuestion(Long id, QuestionDto dto) {
        var groupQuestionDto = (GroupQuestionDto) dto;
        var question = (GroupQuestionEntity) getByIdFromQuestion(id, QuestionEntity.Fields.groupQuestion);

        entityManager.detach(question);

        question.getQuestion().setDescription(groupQuestionDto.getDescription());
        question.getQuestion().setNote(groupQuestionDto.getNote());

        var managedEntity = entityManager.merge(question);
        return mapper.mapToGroupQuestionDto(question);
    }

    @Override
    public Question getQuestionByTypeFromQuestionEntity(QuestionEntity entity) {
        return entity.getGroupQuestion();
    }

    @Override
    public QuestionDto mapToQuestionAnswerResponse(Question question, AnswerQuestionTemporaryEntity studentAnswer, UUID sessionId) {
        var groupQuestions = (GroupQuestionEntity) question;
        var dto = mapper.mapToGroupQuestionDto(groupQuestions);
        var questionsInGroup = groupQuestions.getQuestions();
        var questionsId = questionsInGroup.stream()
                .map(q -> q.getQuestion().getId())
                .collect(toList());
        var answers = answerQuestionTemporaryRepository.findAnswerQuestionBySessionAndQuestionId(sessionId, questionsId);
        var questionList = questionsInGroup.stream()
                .map(questionInGroup -> {
                    var baseQuestion = questionInGroup.getQuestion();
                    var questionOperation = questionOperationFactory.getQuestionOperation(baseQuestion.getType());
                    var specificQuestion = questionOperation.getById(baseQuestion.getId());
                    var answer = answers
                            .stream()
                            .filter(r -> Objects.equals(r.getQuestion().getId(), questionInGroup.getId()))
                            .findAny()
                            .orElse(null);
                    var questionInGroupDto = questionOperation.mapToQuestionAnswerResponse(specificQuestion, answer, sessionId);
                    questionInGroupDto.setType(baseQuestion.getType());
                    questionInGroupDto.setOrder(questionInGroup.getOrder());
                    return questionInGroupDto;
                })
                .collect(toList());

        dto.setQuestionList(questionList);
        return dto;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class QuestionByIdAndType {
        private Long id;
        private String type;
    }
}
