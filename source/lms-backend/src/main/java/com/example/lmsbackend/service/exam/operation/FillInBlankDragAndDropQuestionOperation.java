package com.example.lmsbackend.service.exam.operation;

import com.example.lmsbackend.domain.exam.AnswerQuestionTemporaryEntity;
import com.example.lmsbackend.domain.exam.AnswerTemporaryEntity;
import com.example.lmsbackend.domain.exam.QuizSessionResultQuestionEntity;
import com.example.lmsbackend.domain.exam.base_question.Question;
import com.example.lmsbackend.domain.exam.base_question.QuestionEntity;
import com.example.lmsbackend.domain.exam.base_question.QuestionType;
import com.example.lmsbackend.domain.exam.fill_in_blank_dnd_question.FillInBlankDragAndDropAnswerEntity;
import com.example.lmsbackend.domain.exam.fill_in_blank_dnd_question.FillInBlankDragAndDropBlankEntity;
import com.example.lmsbackend.domain.exam.fill_in_blank_dnd_question.FillInBlankDragAndDropQuestionEntity;
import com.example.lmsbackend.dto.exam.QuestionAnswerDto;
import com.example.lmsbackend.dto.exam.QuestionDto;
import com.example.lmsbackend.dto.exam.fill_in_blank_drag_and_drop.FillInBlankDragAndDropAnswerDto;
import com.example.lmsbackend.dto.exam.fill_in_blank_drag_and_drop.FillInBlankDragAndDropBlankAnswerDto;
import com.example.lmsbackend.dto.exam.fill_in_blank_drag_and_drop.FillInBlankDragAndDropBlankDto;
import com.example.lmsbackend.dto.exam.fill_in_blank_drag_and_drop.FillInBlankDragAndDropQuestionDto;
import com.example.lmsbackend.exceptions.UnexpectedException;
import com.example.lmsbackend.exceptions.exam.QuestionHasNotBeenAnsweredCompletedException;
import com.example.lmsbackend.exceptions.exam.QuestionNotFoundException;
import com.example.lmsbackend.mapper.exam.fill_in_blank_drag_and_drop.DragAndDropAnswerMapper;
import com.example.lmsbackend.mapper.exam.fill_in_blank_drag_and_drop.DragAndDropBlankMapper;
import com.example.lmsbackend.mapper.exam.fill_in_blank_drag_and_drop.DragAndDropQuestionMapper;
import com.example.lmsbackend.repository.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class FillInBlankDragAndDropQuestionOperation extends AbstractQuestionOperation {

    private final DragAndDropQuestionMapper mapper;
    private final DragAndDropBlankMapper blankMapper;
    private final DragAndDropAnswerMapper answerMapper;
    private final FillInBlankDragAndDropQuestionRepository repository;

    @PersistenceContext
    private final EntityManager entityManager;
    private final FillInBlankDragAndDropAnswerRepository answerRepository;
    private final FillInBlankDragAndDropBlankRepository blankRepository;


    public FillInBlankDragAndDropQuestionOperation(QuestionRepository questionRepository,
                                                   QuestionSourceRepository questionSourceRepository,
                                                   FillInBlankDragAndDropQuestionRepository repository,
                                                   TextbookRepository textbookRepository,
                                                   DragAndDropQuestionMapper mapper,
                                                   DragAndDropBlankMapper blankMapper,
                                                   DragAndDropAnswerMapper answerMapper,
                                                   EntityManager entityManager,
                                                   FillInBlankDragAndDropAnswerRepository answerRepository,
                                                   FillInBlankDragAndDropBlankRepository blankRepository) {
        super(questionRepository, questionSourceRepository, textbookRepository, QuestionType.FILL_IN_BLANK_DRAG_AND_DROP.getType());
        this.mapper = mapper;
        this.repository = repository;
        this.blankMapper = blankMapper;
        this.answerMapper = answerMapper;
        this.entityManager = entityManager;
        this.answerRepository = answerRepository;
        this.blankRepository = blankRepository;
    }

    @Override
    protected Question mapToEntity(QuestionDto dto) {
        return mapper.mapToDragAndDropQuestionEntity((FillInBlankDragAndDropQuestionDto) dto);
    }

    @Override
    protected void persistQuestion(QuestionDto dto, QuestionEntity question) {
        var fillInBlankDragAndDropQuestion = (FillInBlankDragAndDropQuestionEntity) mapToEntity(dto);
        fillInBlankDragAndDropQuestion.setQuestion(question);
        question.setFillInBlankDragAndDropQuestion(fillInBlankDragAndDropQuestion);

        fillInBlankDragAndDropQuestion.getBlanks()
                .forEach(blank -> blank.setQuestion(fillInBlankDragAndDropQuestion));
        fillInBlankDragAndDropQuestion.getAnswers()
                .forEach(answer -> answer.setQuestion(fillInBlankDragAndDropQuestion));

        repository.save(fillInBlankDragAndDropQuestion);
    }

    @Override
    public Question getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException(id, QuestionType.FILL_IN_BLANK_DRAG_AND_DROP.getType()));
    }


    @Override
    protected QuestionDto mapToSpecificQuestionResponseDto(Question question) {
        return mapper.mapToDragAndDropQuestionDto((FillInBlankDragAndDropQuestionEntity) question);
    }

    @Override
    protected QuestionDto mapToSpecificQuestionResponseDtoIgnoreAnswer(Question question) {
        return mapper.mapToDragAndDropQuestionDtoIgnoreAnswer((FillInBlankDragAndDropQuestionEntity) question);
    }

    @Override
    protected QuestionDto mapToSpecificQuestionResponseDto(QuestionEntity question) {
        return mapToSpecificQuestionResponseDto(question.getFillInBlankDragAndDropQuestion());
    }

    @Override
    public void grade(QuizSessionResultQuestionEntity answerQuestion) {
        var answer = answerQuestion.getAnswer();
        if (answer == null) {
            answerQuestion.setEarnedPoint(0.0);
            return;
        }
        var question = (FillInBlankDragAndDropQuestionEntity) getById(answer.getQuestion().getId());
        var point = question.getQuestion().getPoint();
        var blanks = question.getBlanks();
        var answers = question.getAnswers();
        var studentAnswers = answer.getValues();
        blanks.forEach(blank -> {
            var studentAnswer = studentAnswers.stream()
                    .filter(s -> Objects.equals(s.getOrder(), blank.getOrder()))
                    .findAny()
                    .orElseThrow(() -> new QuestionHasNotBeenAnsweredCompletedException(answer.getSession().getId(), question.getId()));
            var expectedAnswer = answers.stream()
                    .filter(a -> Objects.equals(a.getKey(), blank.getAnswerKey()))
                    .findFirst()
                    .orElseThrow(() -> new QuestionHasNotBeenAnsweredCompletedException(answer.getSession().getId(), question.getId()));
            studentAnswer.setIsCorrect(StringUtils.equals(expectedAnswer.getContent(), studentAnswer.getValue()));
        });
        long numberOfCorrectAnswers = studentAnswers.stream()
                .filter(ans -> Objects.equals(ans.getIsCorrect(), true))
                .count();
        long numberOfInCorrectAnswers = studentAnswers.size() - numberOfCorrectAnswers;
        answerQuestion.setEarnedPoint((double) point / blanks.size() * numberOfInCorrectAnswers);
    }

    @Override
    public QuestionAnswerDto mapToQuestionAnswerResponse(Question question,
                                                         QuizSessionResultQuestionEntity resultForQuestion) {
        var dragAndDropQuestion = (FillInBlankDragAndDropQuestionEntity) question;
        Set<AnswerTemporaryEntity> studentAnswersKey = new HashSet<>();
        var dto = mapper.mapToFillInBlankDragAndDropQuestionAnswerDto(dragAndDropQuestion);
        if (resultForQuestion.getAnswer() != null) {
            studentAnswersKey = resultForQuestion.getAnswer().getValues();
        }
        var blanksDto = mapBlanks(dragAndDropQuestion.getBlanks(), dragAndDropQuestion.getAnswers(), studentAnswersKey);
        dto.setResultAnswerList(blanksDto);
        dto.setAnswerList(dragAndDropQuestion.getAnswers()
                .stream()
                .map(answerMapper::mapToDragAndDropAnswerDto)
                .sorted(Comparator.comparing(FillInBlankDragAndDropAnswerDto::getOrder))
                .collect(toList()));
        dto.setEarnedPoint(resultForQuestion.getEarnedPoint());
        dto.setPoint(dragAndDropQuestion.getQuestion().getPoint());
        dto.setDescription(dragAndDropQuestion.getQuestion().getDescription());
        dto.setAttachment(dragAndDropQuestion.getQuestion().getAttachment());
        return dto;
    }

    @Override
    public QuestionDto updateQuestion(Long id, QuestionDto dto) {
        var dragAndDropQuestionDto = (FillInBlankDragAndDropQuestionDto) dto;
        var question = (FillInBlankDragAndDropQuestionEntity) getByIdFromQuestion(id, QuestionEntity.Fields.fillInBlankDragAndDropQuestion);
        var blanksIdDto = dragAndDropQuestionDto.getBlankList()
                .stream()
                .filter(blank -> blank.getId() != null)
                .map(FillInBlankDragAndDropBlankDto::getId)
                .collect(toList());
        var answersIdDto = dragAndDropQuestionDto.getAnswerList()
                .stream()
                .filter(blank -> blank.getId() != null)
                .map(FillInBlankDragAndDropAnswerDto::getId)
                .collect(toList());

        entityManager.detach(question);

        var tobeRemovedBlanksId = question.getBlanks()
                .stream()
                .filter(blank -> blanksIdDto.stream()
                        .noneMatch(blankId -> Objects.equals(blankId, blank.getId())))
                .map(FillInBlankDragAndDropBlankEntity::getId)
                .collect(toList());

        var tobeRemovedAnswersId = question.getAnswers()
                .stream()
                .filter(blank -> answersIdDto.stream()
                        .noneMatch(blankId -> Objects.equals(blankId, blank.getId())))
                .map(FillInBlankDragAndDropAnswerEntity::getId)
                .collect(toList());

        mapper.mapToDragAndDropQuestionEntity(question, dragAndDropQuestionDto);
        question.getQuestion().setPoint(dto.getPoint());

        question.getBlanks()
                .forEach(blank -> blank.setQuestion(question));

        question.getAnswers()
                .forEach(answer -> answer.setQuestion(question));

        var managedEntity = entityManager.merge(question);
        repository.save(managedEntity);

        question.setBlanks(
                managedEntity.getBlanks()
                        .stream()
                        .sorted(Comparator.comparingInt(FillInBlankDragAndDropBlankEntity::getOrder))
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );
        question.setAnswers(managedEntity.getAnswers());

        entityManager.flush();
        entityManager.clear();

        entityManager.createQuery("DELETE FROM FillInBlankDragAndDropBlankEntity b WHERE b.id IN (:ids)")
                .setParameter("ids", tobeRemovedBlanksId)
                .executeUpdate();

        entityManager.createQuery("DELETE FROM FillInBlankDragAndDropAnswerEntity a WHERE a.id IN (:ids)")
                .setParameter("ids", tobeRemovedAnswersId)
                .executeUpdate();

        return mapper.mapToDragAndDropQuestionDto(question);
    }

    @Override
    public Question getQuestionByTypeFromQuestionEntity(QuestionEntity entity) {
        return entity.getFillInBlankDragAndDropQuestion();
    }

    private List<FillInBlankDragAndDropBlankAnswerDto> mapBlanks(Set<FillInBlankDragAndDropBlankEntity> blanks,
                                                                 Set<FillInBlankDragAndDropAnswerEntity> answers,
                                                                 Set<AnswerTemporaryEntity> studentAnswersForBlanks) {
        return blanks.stream()
                .map(blank -> {
                    var blankDto = blankMapper.mapToFillInBlankDragAndDropBlankAnswerDto(blank);
                    var studentAnswerOpt = studentAnswersForBlanks.stream()
                            .filter(s -> s.getOrder() == blank.getOrder())
                            .findAny();
                    if (studentAnswerOpt.isPresent()) {
                        var studentAnswer = studentAnswerOpt.get();
                        var studentAnswerKey = Integer.valueOf(studentAnswer.getValue());
                        blankDto.setStudentAnswer(studentAnswer.getValue());
                        var studentAnswerBasedOnKey = getContent(answers, studentAnswerKey);
                        blankDto.setStudentAnswer(studentAnswerBasedOnKey);
                        blankDto.setIsCorrect(Objects.equals(blank.getAnswerKey(), studentAnswerKey));
                    } else {
                        blankDto.setIsCorrect(false);
                    }
                    var expectedAnswerBasedOnKey = getContent(answers, blank.getAnswerKey());
                    blankDto.setExpectedAnswer(expectedAnswerBasedOnKey);

                    return blankDto;
                })
                .collect(toList());
    }

    private String getContent(Set<FillInBlankDragAndDropAnswerEntity> answers, Integer answerKey) {
        return answers
                .stream()
                .filter(a -> a.getKey() == answerKey)
                .findFirst()
                .orElseThrow(() -> new UnexpectedException())
                .getContent();
    }

    @Override
    public QuestionDto mapToQuestionAnswerResponse(Question question, AnswerQuestionTemporaryEntity answer, UUID sessionId) {
        var dto = mapper.mapToDragAndDropQuestionDtoIgnoreAnswer((FillInBlankDragAndDropQuestionEntity) question);
        if (answer == null) {
            return dto;
        }
        dto.getBlankList()
                .forEach(blank -> {
                    var opt = answer.getValues()
                            .stream()
                            .filter(x -> Objects.equals(x.getOrder(), blank.getOrder()))
                            .findAny();
                    opt.ifPresent(x -> blank.setStudentAnswer(x.getValue()));
                });
        return dto;
    }
}
