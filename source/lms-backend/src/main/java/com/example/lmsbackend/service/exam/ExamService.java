package com.example.lmsbackend.service.exam;

import com.example.lmsbackend.config.security.aop.Auth;
import com.example.lmsbackend.criteria.BaseSearchCriteria;
import com.example.lmsbackend.criteria.PaginationCriterion;
import com.example.lmsbackend.domain.StudentEntity;
import com.example.lmsbackend.domain.classmodel.ClassStudentEntity;
import com.example.lmsbackend.domain.classmodel.QuizClassEntity;
import com.example.lmsbackend.domain.coursemodel.CourseEntity;
import com.example.lmsbackend.domain.coursemodel.ExamTextBookEntity;
import com.example.lmsbackend.domain.exam.*;
import com.example.lmsbackend.domain.exam.base_question.QuestionEntity;
import com.example.lmsbackend.domain.exam.base_question.QuestionInGroupEntity;
import com.example.lmsbackend.domain.exam.base_question.QuestionSourceEntity;
import com.example.lmsbackend.domain.exam.base_question.QuestionType;
import com.example.lmsbackend.domain.resource.TextbookEntity;
import com.example.lmsbackend.dto.exam.*;
import com.example.lmsbackend.dto.exam.writing.UpdatePointDto;
import com.example.lmsbackend.enums.FinalVerdict;
import com.example.lmsbackend.enums.GradedState;
import com.example.lmsbackend.enums.PermissionEnum;
import com.example.lmsbackend.exceptions.UnauthorizedException;
import com.example.lmsbackend.exceptions.aclass.ClassDoesNotHaveAnyStudent;
import com.example.lmsbackend.exceptions.aclass.UserIsNotStudentOfClassException;
import com.example.lmsbackend.exceptions.course.CourseNotFoundException;
import com.example.lmsbackend.exceptions.course.QuizNotFoundException;
import com.example.lmsbackend.exceptions.exam.*;
import com.example.lmsbackend.job.QuizJobExecution;
import com.example.lmsbackend.mapper.MapperUtils;
import com.example.lmsbackend.mapper.QuizMapper;
import com.example.lmsbackend.mapper.exam.ExamMapper;
import com.example.lmsbackend.mapper.exam.QuizConfigMapper;
import com.example.lmsbackend.mapper.exam.QuizSessionMapper;
import com.example.lmsbackend.multitenancy.utils.TenantContext;
import com.example.lmsbackend.repository.*;
import com.example.lmsbackend.repository.custom.PagedList;
import com.example.lmsbackend.service.UserService;
import com.example.lmsbackend.service.exam.operation.GroupQuestionOperation;
import com.example.lmsbackend.service.exam.operation.QuestionOperationFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.IntStream;

import static com.example.lmsbackend.mapper.MapperUtils.mapPagedDto;
import static java.util.stream.Collectors.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExamService {

    private final QuestionRepository questionRepository;
    private final QuestionOperationFactory questionOperationFactory;

    private final CourseRepository courseRepository;

    private final ExamRepository examRepository;
    private final ExamQuestionSourceRepository examQuestionSourceRepository;
    private final QuizSessionRepository quizSessionRepository;
    private final AnswerQuestionTemporaryRepository answerQuestionTemporaryRepository;
    private final QuizSessionResultRepository quizSessionResultRepository;
    private final SessionResultQuestionRepository sessionResultQuestionRepository;
    private final QuizResultRepository quizResultRepository;
    private final QuizCourseRepository quizCourseRepository;

    private final UserService userService;

    private final QuizMapper quizMapper;
    private final ExamMapper examMapper;
    private final QuizConfigMapper quizConfigMapper;
    private final QuizSessionMapper quizSessionMapper;

    private final QuizClassRepository quizClassRepository;
    private final GradeTagStudentRepository gradeTagStudentRepository;

    private final JobScheduler jobScheduler;
    private final QuizJobExecution quizJobExecution;
    private final GroupQuestionRepository groupQuestionRepository;
    private final ClassStudentRepository classStudentRepository;
    private final QuizSessionFlagRepository quizSessionFlagRepository;
    private final TextbookRepository textbookRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    @Transactional
    @Auth(permission = PermissionEnum.CREATE_TEST)
    public ExamDto createExamContent(Long resourceId, CreateExamRequestDto dto) {
        var course = courseRepository.findAndFetchWithById(resourceId, CourseEntity.Fields.exams)
                .orElseThrow(() -> new CourseNotFoundException(resourceId));
        var exam = mapExam(dto);
        exam.setCourse(course);
        examRepository.save(exam);
        return examMapper.mapToExamContentDto(exam);
    }

    private ExamEntity mapExam( CreateExamRequestDto dto) {
        var exam = examMapper.mapToExamEntity(dto);
        if (CollectionUtils.isNotEmpty(dto.getTextbooks())) {
            var textbooksId = dto.getTextbooks()
                    .stream()
                    .map(TextbookExamDto::getTextbookId)
                    .collect(toSet());

            var textbooks = textbookRepository.findFetchExams(textbooksId);
            var textbookExam = textbooks
                    .stream()
                    .map(textbook -> buildExamTextbookEntity(dto, exam, textbook))
                    .collect(toSet());
            exam.setTextbooks(textbookExam);
        }
        return exam;
    }

    private ExamTextBookEntity buildExamTextbookEntity(CreateExamRequestDto dto, ExamEntity exam, TextbookEntity textbook) {
        var examTextbook = new ExamTextBookEntity();
        examTextbook.setExam(exam);
        examTextbook.setTextbook(textbook);
        var textbookInExam = dto.getTextbooks()
                .stream()
                .filter(t -> Objects.equals(t.getTextbookId(), textbook.getId()))
                .findAny()
                .orElseThrow(() -> new RuntimeException());
        examTextbook.setNote(textbookInExam.getNote());
        return examTextbook;
    }

    @Transactional
    @Auth(permission = PermissionEnum.UPDATE_TEST)
    public ExamDto updateExam(Long resourceId, UpdateExamRequestDto dto) {
        var exam = examRepository.findById(resourceId)
                .orElseThrow(() -> new ExamNotFoundException(resourceId));

        if (dto.getCourseId() != null && dto.getCourseId() != exam.getCourse().getId()) {
            var course = courseRepository.findAndFetchWithById(dto.getCourseId(), CourseEntity.Fields.exams)
                    .orElseThrow(() -> new CourseNotFoundException(dto.getCourseId()));
            course.removeExam(exam);
            exam.setCourse(course);
        }

        exam.setTitle(dto.getTitle());
        exam.setDescription(dto.getDescription());
        exam.setState(dto.getState());
        examRepository.save(exam);
        return examMapper.mapToExamContentDto(exam);
    }

    @Transactional
    @Auth(permission = PermissionEnum.DELETE_TEST)
    public Long deleteExam(Long resourceId) {
        if (quizCourseRepository.countQuizByExam(resourceId) != 0 || quizClassRepository.countQuizByExam(resourceId) != 0) {
            log.warn("Exam {} is being used, cannot delete", resourceId);
            throw new ExamIsBeingUsedException();
        }
        entityManager.createQuery("DELETE FROM ExamEntity e WHERE e.id = (:id)")
                .setParameter("id", resourceId)
                .executeUpdate();
        return resourceId;
    }

    @Transactional
    @Auth(permission = PermissionEnum.UPDATE_TEST)
    public QuestionDto createQuestion(Long resourceId, QuestionDto dto) {
        var exam = examRepository.findById(resourceId)
                .orElseThrow(() -> new ExamNotFoundException(resourceId));

        var questionOperation = questionOperationFactory.getQuestionOperation(dto.getType());
        var questionSource = questionOperation.persistQuestion(dto);


        var examQuestionSource = mapExamQuestionSource(exam, questionSource, exam.getQuestions().size() + 1);

        examQuestionSourceRepository.save(entityManager.merge(examQuestionSource));

        var response = questionOperation.mapToQuestionResponseDto(questionSource.getQuestion());
        response.setOrder(examQuestionSource.getOrder());
        return response;
    }

    @Transactional
    @Auth(permission = PermissionEnum.UPDATE_TEST)
    public QuestionDto createQuestionGroup(Long resourceId, QuestionDto dto) {
        var exam = examRepository.findById(resourceId)
                .orElseThrow(() -> new ExamNotFoundException(resourceId));
        var operation = questionOperationFactory.getQuestionOperation("GROUP");
        var questionSource = operation.persistQuestion(dto);
        var examQuestionSource = mapExamQuestionSource(exam, questionSource, dto.getOrder());

        examQuestionSourceRepository.save(examQuestionSource);

        var response = operation.mapToQuestionResponseDto(questionSource.getQuestion());
        response.setOrder(examQuestionSource.getOrder());
        return response;
    }

    @Transactional
    @Auth(permission = PermissionEnum.UPDATE_TEST)
    public QuestionDto createQuestionInGroup(Long resourceId, Long groupId, QuestionDto dto) {
        if (!examRepository.existsById(resourceId)) {
            throw new ExamNotFoundException(resourceId);
        }
        var groupQuestion = groupQuestionRepository.findById(groupId)
                .orElseThrow(() -> new QuestionNotFoundException(groupId, "GROUP"));
        var operation = questionOperationFactory.getQuestionOperation(dto.getType());
        var question = operation.persistQuestion(dto).getQuestion();
        groupQuestion.getQuestion().setPoint(groupQuestion.getQuestion().getPoint() + question.getPoint());
        entityManager.flush();
        var questionInGroup = new QuestionInGroupEntity();
        questionInGroup.setGroup(groupQuestion);
        questionInGroup.setQuestion(question);
        questionInGroup.setOrder(groupQuestion.getQuestions().size());

        entityManager.flush();
        var response = operation.mapToQuestionResponseDto(question);
        response.setOrder(dto.getOrder());
        return response;
    }

    @Transactional
    @Auth(permission = PermissionEnum.UPDATE_TEST)
    public Long deleteQuestion(Long resourceId, Long questionId) {
        var question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(questionId));
        var groupQuestionOpt = groupQuestionRepository.findQuestionGroupByQuestionInGroup(questionId);
        if (groupQuestionOpt.isPresent()) {
            var groupQuestion = groupQuestionOpt.get();
            var originalPoint = groupQuestion.getQuestion().getPoint();
            groupQuestion.getQuestion().setPoint(originalPoint - question.getPoint());
        }
        entityManager.createQuery("DELETE FROM QuestionSourceEntity q WHERE q.id = (:questionId)")
                .setParameter("questionId", questionId)
                .executeUpdate();
        return questionId;
    }

    private ExamQuestionSourceEntity mapExamQuestionSource(ExamEntity exam, QuestionSourceEntity questionSource, Integer order) {
        var examQuestionSource = new ExamQuestionSourceEntity();
        examQuestionSource.setExam(exam);
        examQuestionSource.setQuestion(questionSource);
        examQuestionSource.setOrder(order);
        return examQuestionSource;
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_TEST)
    public QuestionDto getQuestionById(Long resourceId, Long questionId) {
        var baseQuestion = questionRepository.getQuestionById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(questionId));
        var questionOperation = questionOperationFactory.getQuestionOperation(baseQuestion.getType());
        var question = questionOperation.getById(questionId);

        return questionOperation.mapToQuestionResponseDto(question);
    }

    public QuestionPagedResponseDto getQuestionsByExam(Long examId, Integer page, Integer size) {
        var questionsByIdAndType = examQuestionSourceRepository.getQuestionsIdByExam(examId, page, size);
        var questionsDto = getQuestionsByIdAndType(questionsByIdAndType);
        return mapQuestionsDtoResponse(questionsByIdAndType, questionsDto);
    }

    private QuestionPagedResponseDto mapQuestionsDtoResponse(PagedList<QuestionInExamByTypeAndIdDto> questionsByIdAndType, List<QuestionDto> questionsDto) {
        var questionPagedResponse = new QuestionPagedResponseDto();
        mapPagedDto(questionPagedResponse, questionsByIdAndType);
        questionPagedResponse.setListData(questionsDto);
        return questionPagedResponse;
    }

    private List<QuestionDto> getQuestionsByIdAndType(PagedList<QuestionInExamByTypeAndIdDto> questionsByIdAndType) {
        return questionsByIdAndType.getListData()
                .stream()
                .map(q -> {
                    var questionOperation = questionOperationFactory.getQuestionOperation(q.getType());
                    var question = questionOperation.getById(q.getId());
                    var dto = questionOperation.mapToQuestionResponseDto(question);
                    dto.setOrder(q.getOrder());
                    return dto;
                })
                .collect(toList());
    }

    public QuestionPagedResponseDto getQuestionsByExamIgnoreAnswer(Long examId, Integer page, Integer size) {
        var questionsByIdAndType = examQuestionSourceRepository.getQuestionsIdByExam(examId, page, size);
        var questionsDto = getQuestionsByIdAndTypeIgnoreAnswer(questionsByIdAndType);
        return mapQuestionsDtoResponse(questionsByIdAndType, questionsDto);
    }

    private List<QuestionDto> getQuestionsByIdAndTypeIgnoreAnswer(PagedList<QuestionInExamByTypeAndIdDto> questionsByIdAndType) {
        return questionsByIdAndType.getListData()
                .stream()
                .map(q -> {
                    var questionOperation = questionOperationFactory.getQuestionOperation(q.getType());
                    var question = questionOperation.getById(q.getId());
                    var dto = questionOperation.mapToQuestionResponseDtoIgnoreAnswer(question);
                    dto.setOrder(q.getOrder());
                    return dto;
                })
                .collect(toList());
    }

    @Transactional
    @Auth(permission = PermissionEnum.DO_QUIZ)
    public QuizSessionDto initExamSession(Long resourceId, Long classId, Integer page, Integer size) {
        var currentUser = userService.getCurrentUser();
        if (!classStudentRepository.isStudentInClass(currentUser.getId(), classId)) {
            log.warn("User {} is not a student of class {}", currentUser.getId(), classId);
            throw new UserIsNotStudentOfClassException(currentUser.getId(), classId);
        }
        var quiz = quizClassRepository.findFetchById(resourceId, String.format("%s-%s", QuizClassEntity.Fields.config, QuizClassEntity.Fields.exam))
                .orElseThrow(() -> new QuizNotFoundInClassException(resourceId, classId));

        var config = quiz.getConfig();

        var validBefore = config.getValidBefore();
        if (validBefore != null && LocalDateTime.now().isAfter(validBefore)) {
            log.warn("Cannot do quiz because it's already closed at {}", validBefore);
            throw new QuizHasAlreadyClosedException(resourceId, validBefore);
        }

        if (isExceedQuizAttempt(currentUser.getId(), quiz)) {
            log.warn("Cannot do quiz you already reach max attempt {}", quiz.getConfig().getMaxAttempt());
            throw new QuizAttemptHasReachLimitException(currentUser.getId(), quiz.getId());
        }

        Optional<UUID> unEndedQuizSession = quizSessionRepository.existingUnEndedQuizSession(resourceId, currentUser.getId());
        if (unEndedQuizSession.isPresent()) {
            log.warn("Cannot do quiz because user has not ended the previous sessions: {}", unEndedQuizSession.get());
            throw new ExistingUnEndedQuizSessionException();
        }

        var session = new QuizSessionEntity();
        session.setId(UUID.randomUUID());
        session.setStartedAt(LocalDateTime.now());
        session.setStudent(entityManager.getReference(StudentEntity.class, currentUser.getId()));
        session.setQuiz(quiz);

        entityManager.createQuery("UPDATE QuizSessionEntity q" +
                        " SET q.lastSession = false " +
                        "WHERE q.quiz.id = (:quizId) AND q.lastSession = true AND q.student.id = (:studentId) ")
                .setParameter("quizId", resourceId)
                .setParameter("studentId", currentUser.getId())
                .executeUpdate();

        quizSessionRepository.save(session);

        var initExamSessionResponseDto = mapToExamSessionResponseDto(session);
        initExamSessionResponseDto.setQuestionList(getQuestionsByExamIgnoreAnswer(quiz.getExam().getId(), page, size));

        if (BooleanUtils.isTrue(config.getHaveTimeLimit()) && config.getTimeLimit() != null) {
            jobScheduler.schedule(session.getStartedAt().plusMinutes(config.getTimeLimit()).plusSeconds(10), () -> quizJobExecution.endExamSession(session.getId(), TenantContext.getTenantId(), userService.getCurrentUserId()));
        }

        return initExamSessionResponseDto;
    }

    private boolean isExceedQuizAttempt(Long studentId, QuizClassEntity quiz) {
        var config = quiz.getConfig();
        return config.getMaxAttempt() != 0 && quizSessionRepository.getNumberOfSessions(studentId, quiz.getId()) >= config.getMaxAttempt();
    }

    @Transactional
    public void endQuizSession(String sessionId, Long userId) {
        var sessionUUID = UUID.fromString(sessionId);
        var session = quizSessionRepository.findFetchQuizExamConfigAnswersById(sessionUUID)
                .orElseThrow(() -> new QuizSessionNotFoundException(sessionId));

        if (!Objects.equals(session.getStudent().getId(), userId)) {
            throw new UnauthorizedException("You doesn't have authorization to end quiz session " + sessionId);
        }

        if (session.getSubmittedAt() != null) {
            log.warn("Quiz session {} has ended", sessionUUID);
            return;
        }

        session.setSubmittedAt(LocalDateTime.now());
        quizSessionRepository.save(session);

        var quiz = session.getQuiz();
        var exam = quiz.getExam();
        var questionsInExam = exam.getQuestions();

        var studentAnswers = session.getAnswers();

        var sessionResult = gradeAfterEndSession(questionsInExam, studentAnswers, session);

        // check here to avoid auto-flush mode
        var havingWritingQuestion = examRepository.isHavingWritingQuestion(exam.getId());
        if (havingWritingQuestion) {
            sessionResult.setGradedState(GradedState.WAITING);
        } else {
            sessionResult.setGradedState(GradedState.DONE);
        }

        sessionResult.setSession(session);

        var totalGrade = calculateTotalGradeSession(sessionResult);

        var config = session.getQuiz().getConfig();

        if (!havingWritingQuestion) {
            determineFinalResult(config, sessionResult, totalGrade);
        }

        sessionResult.setScore(totalGrade);
        quizSessionResultRepository.save(sessionResult);

        saveStudentGrade(userId, quiz, totalGrade);
    }

    private void saveStudentGrade(Long userId, QuizClassEntity quiz, Double totalGrade) {
        var studentGrade = new GradeTagStudentEntity();
        studentGrade.setStudent(entityManager.getReference(StudentEntity.class, userId));
        studentGrade.setTag(quiz.getTag());
        studentGrade.setGrade(totalGrade);
        gradeTagStudentRepository.save(studentGrade);
    }

    private SessionResultDto getSessionResultDto(QuizSessionEntity session, QuizConfigEntity config) {
        var response = quizSessionMapper.mapToSessionResultOverview(session);

        if (BooleanUtils.isTrue(config.getViewPreviousSessions())) {
            return response;
        }
        hideResult(response);
        return response;
    }

    @Transactional
    public void endQuizSession(String sessionId) {
        var currentUser = userService.getCurrentUser();
        endQuizSession(sessionId, currentUser.getId());
    }

    private void hideResult(SessionResultDto response) {
        response.setTotalScore(null);
        response.setFinalVerdict(null);
    }

    private Double calculateTotalGradeSession(QuizSessionResultEntity quizSessionResult) {
        return quizSessionResult.getResultQuestions()
                .stream()
                .filter(x -> x.getAnswer() != null)
                .map(QuizSessionResultQuestionEntity::getEarnedPoint)
                .filter(Objects::nonNull)
                .reduce(0.0, Double::sum);
    }

    private void determineFinalResult(QuizConfigEntity config, QuizSessionResultEntity result, Double totalGrade) {
        if (result.getGradedState() == GradedState.DONE) {
            if (totalGrade >= config.getPassScore()) {
                result.setFinalVerdict(FinalVerdict.PASSED);
            } else {
                result.setFinalVerdict(FinalVerdict.FAILED);
            }
        }
    }

    private QuizSessionResultEntity gradeAfterEndSession(Set<ExamQuestionSourceEntity> questions, Set<AnswerQuestionTemporaryEntity> answers, QuizSessionEntity session) {
        var sessionResult = new QuizSessionResultEntity();
        sessionResult.setGradedState(GradedState.WAITING);
        questions
                .forEach(questionSourceInExam -> {
                    var answerForQuestionOpt = answers.stream()
                            .filter(studentAnswer -> Objects.equals(studentAnswer.getQuestion().getId(), questionSourceInExam.getQuestion().getId()))
                            .findFirst();
                    var resultQuestion = new QuizSessionResultQuestionEntity();
                    QuestionEntity question = questionSourceInExam.getQuestion().getQuestion();
                    resultQuestion.setQuestion(question);
                    resultQuestion.setSessionResult(sessionResult);
                    if (answerForQuestionOpt.isPresent()) {
                        var answerForQuestion = answerForQuestionOpt.get();
                        resultQuestion.setAnswer(answerForQuestion);
                    }
                    var questionOperation = questionOperationFactory.getQuestionOperation(question
                            .getType());
                    if (questionOperation instanceof GroupQuestionOperation) {
                        ((GroupQuestionOperation) questionOperation).grade(resultQuestion, session);
                    } else {
                        questionOperation.grade(resultQuestion);
                    }
                });
        return sessionResult;
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_QUIZ_RESULT)
    public SessionResultDto getQuizSessionResult(String resourceId, Long question, Integer page, Integer size) {
        var sessionId = UUID.fromString(resourceId);
        var session = quizSessionRepository.findFetchResultById(sessionId)
                .orElseThrow(() -> new QuizSessionNotFoundException(resourceId));
        var dto = quizSessionMapper.mapToSessionResultOverview(session);

        if (question != null) {
            page = examQuestionSourceRepository.getPageOfQuestion(session.getQuiz().getExam().getId(), question, size);
        }

        var questionAnswerDto = getQuestionsAnswerBySession(sessionId, page, size);

        questionAnswerDto.setUnGradedQuestions(quizSessionRepository.findUngradedQuestions(sessionId, session.getQuiz().getExam().getId()));

        dto.setQuestions(questionAnswerDto);
        return dto;
    }

    private QuizSessionResponseDto getQuizSessionWhileDoing(UUID sessionId, Integer page, Integer size) {
        var session = quizSessionRepository.findFetchExam(sessionId)
                .orElseThrow(() -> new NoActiveQuizSessionException(sessionId));
        var examId = session.getQuiz().getExam().getId();
        var questionsByIdAndType = examQuestionSourceRepository.getQuestionsIdByExam(examId, page, size);
        var questionsId = questionsByIdAndType
                .getListData()
                .stream()
                .map(CreateQuestionResponseDto::getId)
                .collect(toList());
        var answers = answerQuestionTemporaryRepository.findAnswerQuestionBySessionAndQuestionId(sessionId, questionsId);
        var questionAnswerDto = questionsByIdAndType.getListData()
                .stream()
                .map(questionByIdAndType -> {
                    var questionOperation = questionOperationFactory.getQuestionOperation(questionByIdAndType.getType());
                    var question = questionOperation.getById(questionByIdAndType.getId());
                    var answer = answers
                            .stream()
                            .filter(r -> Objects.equals(r.getQuestion().getId(), question.getId()))
                            .findAny()
                            .orElse(null);
                    var dto = questionOperation.mapToQuestionAnswerResponse(question, answer, sessionId);
                    dto.setType(questionByIdAndType.getType());
                    dto.setOrder(questionByIdAndType.getOrder());
                    return dto;
                })
                .collect(toList());

        var dto = new QuizSessionResponseDto();
        MapperUtils.mapPagedDto(dto, questionsByIdAndType);
        dto.setListData(questionAnswerDto);
        return dto;
    }

    @Transactional(readOnly = true)
    public QuestionAnswerPagedResponseDto getQuestionsAnswerBySession(UUID sessionId, Integer page, Integer size) {
        var session = quizSessionRepository.findFetchExam(sessionId)
                .orElseThrow(() -> new NoActiveQuizSessionException(sessionId));
        var examId = session.getQuiz().getExam().getId();
        var questionsByIdAndType = examQuestionSourceRepository.getQuestionsIdByExam(examId, page, size);
        var questionsId = questionsByIdAndType
                .getListData()
                .stream()
                .map(CreateQuestionResponseDto::getId)
                .collect(toList());
        var resultForQuestions = sessionResultQuestionRepository.findByQuestionAndSessionResult(questionsId, session.getId());
        var questionAnswerDto = questionsByIdAndType.getListData()
                .stream()
                .map(questionByIdAndType -> {
                    var questionOperation = questionOperationFactory.getQuestionOperation(questionByIdAndType.getType());
                    var question = questionOperation.getById(questionByIdAndType.getId());
                    var resultForQuestion = resultForQuestions
                            .stream()
                            .filter(r -> Objects.equals(r.getQuestion().getId(), question.getId()))
                            .findAny()
                            .orElse(null);
                    if (resultForQuestion == null) {
                        resultForQuestion = new QuizSessionResultQuestionEntity();
                        resultForQuestion.setSessionResultWithout(session.getResult());
                    }
                    var dto = questionOperation.mapToQuestionAnswerResponse(question, resultForQuestion);
                    dto.setType(questionByIdAndType.getType());
                    dto.setOrder(questionByIdAndType.getOrder());
                    return dto;
                })
                .collect(toList());

        var dto = new QuestionAnswerPagedResponseDto();
        MapperUtils.mapPagedDto(dto, questionsByIdAndType);
        dto.setListData(questionAnswerDto);
        return dto;
    }

    private boolean isActiveSession(QuizConfigEntity config, QuizSessionEntity session) {
        if (BooleanUtils.isFalse(config.getHaveTimeLimit())) {
            return true;
        }
        if (session.getStartedAt().plusMinutes(config.getTimeLimit()).isAfter(LocalDateTime.now())) {
            return true;
        }
        return false;
    }

    @Transactional
    public  void answerQuestion(String sessionId, Long questionId, AnswerSessionDto answerDto) {
        var sessionUUID = UUID.fromString(sessionId);
        var question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(questionId));

        var potentialActiveSession = quizSessionRepository.findPotentialActiveSession(sessionUUID)
                .orElseThrow(() -> new NoActiveQuizSessionException(sessionUUID));

        var currentUser = userService.getCurrentUser();
        if (!Objects.equals(potentialActiveSession.getStudent().getId(), currentUser.getId())) {
            throw new UnauthorizedException(String.format("User %s doesn't have authorization to answer question %s", currentUser.getId(), questionId));
        }

        var config = potentialActiveSession.getQuiz().getConfig();

        if (!isActiveSession(config, potentialActiveSession)) {
            log.warn("Session has expired, startedAt {}, now {} and the time of quiz is {}", potentialActiveSession.getStartedAt(), LocalDateTime.now(), config.getTimeLimit());
            throw new QuizSessionExpiredException(potentialActiveSession.getStartedAt(), config.getTimeLimit());
        }

        entityManager.createQuery("DELETE FROM AnswerTemporaryEntity a WHERE a.answer.id = (SELECT u.id FROM AnswerQuestionTemporaryEntity u WHERE u.question.id = :questionId AND u.session.id = :sessionId)")
                .setParameter("questionId", questionId)
                .setParameter("sessionId", sessionUUID)
                .executeUpdate();

        var answerQuestionOpt = answerQuestionTemporaryRepository.findBySessionAndQuestion(sessionUUID, questionId);
        AnswerQuestionTemporaryEntity answerQuestion = new AnswerQuestionTemporaryEntity();
        if (answerQuestionOpt.isPresent()) {
            answerQuestion = answerQuestionOpt.get();
        } else {
            answerQuestion.setSession(potentialActiveSession);
            answerQuestion.setQuestion(question);
        }
        answerQuestion.setOptionalStudentNote(answerDto.getOptionalStudentNote());

        AnswerQuestionTemporaryEntity finalAnswerQuestion = answerQuestion;
        var answers = answerDto.getAnswers();
        // TODO: validate answer based on each type of question
        var answersTemporary = IntStream.range(0, answers.size())
                .mapToObj(i -> {
                    var answer = answers.get(i);
                    var answerTemporary = new AnswerTemporaryEntity();
                    answerTemporary.setValue(answer);
                    answerTemporary.setAnswer(finalAnswerQuestion);
                    answerTemporary.setOrder(i + 1);
                    return answerTemporary;
                })
                .collect(toSet());
        answerQuestion.getValues().addAll(answersTemporary);
        answerQuestionTemporaryRepository.save(answerQuestion);
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_TEST)
    public ExamDto getExamContent(Long resourceId, Integer page, Integer size) {
        var exam = examRepository.findById(resourceId)
                .orElseThrow(() -> new ExamNotFoundException(resourceId));
        var examContentDto = examMapper.mapToExamContentDto(exam);
        examContentDto.setCourseName(exam.getCourse().getName());
        var currentUser = userService.getCurrentUser();
        var courseId = exam.getCourse().getId();
        if (classStudentRepository.isStudentInAnyClassOfCourse(courseId, currentUser.getId())) {
            var questionListDto = new QuestionPagedResponseDto();
            MapperUtils.getDefaultPagedDto(questionListDto);
            questionListDto.setListData(new ArrayList<>());
            examContentDto.setQuestionList(questionListDto);
        } else {
            examContentDto.setQuestionList(getQuestionsByExam(resourceId, page, size));
        }

        examContentDto.setTotalGrade(Math.toIntExact(examRepository.totalGradeOfExam(resourceId)));

        return examContentDto;
    }

    public ExamContentPagedDto getExams(BaseSearchCriteria criteria) {
        var examsContent = examRepository.findExamsByCriteria(null, criteria);
        var examContentPagedDto = new ExamContentPagedDto();
        mapPagedDto(examContentPagedDto, examsContent);
        examContentPagedDto.setListData(
                examsContent
                        .getListData()
                        .stream()
                        .map(examMapper::mapToExamContentItem)
                        .collect(toList())
        );
        return examContentPagedDto;
    }

    private QuizSessionDto mapToExamSessionResponseDto(QuizSessionEntity session) {
        // performance
        var exam = session.getQuiz().getExam();
        var config = session.getQuiz().getConfig();
        var dto = new QuizSessionDto();
        dto.setInstanceTitle(exam.getTitle());
        dto.setInstanceDescription(exam.getDescription());

        var configDto = quizConfigMapper.mapToConfigDto(config);
        dto.setConfig(configDto);

        dto.setId(session.getId().toString());
        dto.setTimeStart(session.getStartedAt());
        dto.setTimeSubmitted(session.getSubmittedAt());
        return dto;
    }

    @Transactional(readOnly = true)
    public ExamContentPagedDto getExamsByCourse(Long courseId, BaseSearchCriteria criteria) {
        var examsContent = examRepository.findExamsByCriteria(courseId, criteria);
        var examContentPagedDto = new ExamContentPagedDto();
        mapPagedDto(examContentPagedDto, examsContent);
        examContentPagedDto.setListData(
                examsContent
                        .getListData()
                        .stream()
                        .map(examMapper::mapToExamContentItem)
                        .collect(toList())
        );
        return examContentPagedDto;
    }

    @Transactional(readOnly = true)
    public ExamContentPagedDto searchExamsByCourse(Long courseId, String keyword) {
        var criteria = new BaseSearchCriteria();
        criteria.setKeyword(keyword);
        var examsContent = examRepository.findExamsByCriteria(courseId, criteria);
        var examContentPagedDto = new ExamContentPagedDto();
        mapPagedDto(examContentPagedDto, examsContent);
        examContentPagedDto.setListData(
                examsContent
                        .getListData()
                        .stream()
                        .map(examMapper::mapToExamContentItem)
                        .collect(toList())
        );
        return examContentPagedDto;
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_QUIZ_CLASS)
    public QuizEntryDto getQuizAndQuestionsById(Long classId, Long resourceId, Integer page, Integer size) {
        var quiz = quizClassRepository.findFetchById(resourceId, String.format("%s-%s", QuizClassEntity.Fields.config, QuizClassEntity.Fields.exam))
                .orElseThrow(() -> new QuizNotFoundInClassException(resourceId, classId));

        var config = quiz.getConfig();
        if (config == null) {
            throw new ExamHasNotConfiguredForClassYetException(classId, resourceId);
        }

        var exam = quiz.getExam();

        var quizDto = quizMapper.mapToQuizEntry(quiz);
        quizDto.setConfig(quizConfigMapper.mapToConfigDto(config));
        var questions = getQuestionsByExam(exam.getId(), page, size);
        quizDto.setQuestionList(questions);
        return quizDto;
    }

    @Transactional
    public QuizSessionResponseDto getQuizSession(String sessionId, Long question, Integer page, Integer size) {
        var sessionUUID = UUID.fromString(sessionId);
        var session = quizSessionRepository.findById(sessionUUID)
                .orElseThrow(() -> new NoActiveQuizSessionException(sessionUUID));
        if (question != null) {
            page = examQuestionSourceRepository.getPageOfQuestion(session.getQuiz().getExam().getId(), question, size);
        }

        var currentUser = userService.getCurrentUser();
        if (!Objects.equals(session.getStudent().getId(), currentUser.getId())) {
            throw new UnauthorizedException("You doesn't have authorization to get questions of session " + sessionId);
        }

        var startedAt = session.getStartedAt();
        QuizConfigEntity config = session.getQuiz().getConfig();
        var timeLimit = config.getTimeLimit();
        if (timeLimit != null && startedAt.plusMinutes(timeLimit).isBefore(LocalDateTime.now())) {
            log.warn("Quiz session {} has expired", sessionId);
            if (session.getSubmittedAt() == null) {
                endQuizSession(sessionId, currentUser.getId());
            }
        }

        var quizSessionWhileDoing = getQuizSessionWhileDoing(sessionUUID, page, size);
        quizSessionWhileDoing.setHaveTimeLimit(config.getHaveTimeLimit());
        if (config.getTimeLimit() != null) {
            quizSessionWhileDoing.setRemainingTime(config.getTimeLimit() * 60 - ChronoUnit.SECONDS.between(session.getStartedAt(), LocalDateTime.now()));
        }

        return quizSessionWhileDoing;
    }

    @Transactional
    public List<QuestionInExamByTypeAndIdDto> getAllQuestionsIdAndTypeWhileTakingQuiz(String sessionId, Long examId) {
        return examQuestionSourceRepository.getQuestionsIdAndTypeByExam(UUID.fromString(sessionId), examId);
    }

    @Transactional
    public void flagQuestion(String sessionId, Long questionId) {
        var currentUser = userService.getCurrentUser();
        if (!quizSessionRepository.isSessionCreatedByUser(UUID.fromString(sessionId), currentUser.getId())) {
            throw new UnauthorizedException();
        }
        var flag = new QuizSessionFlagEntity();
        flag.setQuestion(entityManager.getReference(QuestionEntity.class, questionId));
        flag.setSession(entityManager.getReference(QuizSessionEntity.class, UUID.fromString(sessionId)));
        quizSessionFlagRepository.save(flag);
    }

    @Transactional
    public void unFlagQuestion(String sessionId, Long questionId) {
        var currentUser = userService.getCurrentUser();
        if (!quizSessionRepository.isSessionCreatedByUser(UUID.fromString(sessionId), currentUser.getId())) {
            throw new UnauthorizedException();
        }
        var byId = quizSessionFlagRepository.findById(new QuizSessionFlagPK(UUID.fromString(sessionId), questionId));
        byId.ifPresent(quizSessionFlagRepository::delete);
    }

    @Transactional
    public void testGetQuestionJoinTable() {
        // var result = questionRepository.findByIdIn(List.of(1L, 2L, 3L, 4L));
        var result = questionRepository.findQuestionsByIdIn(List.of(1L, 2L, 3L, 4L));
        var x = 5;
    }

    public void testSearchQuestionByExam(Long examId, String keyword, Integer page, Integer size) {
        questionRepository.getQuestionByKeyword(examId, keyword, page, size);
    }

    @Transactional
    @Auth(permission = PermissionEnum.GRADE_QUIZ)
    public void gradeQuestion(Long resourceId, String sessionId, Long questionId, UpdatePointDto dto) {
        // currently, only handle update point for writing question
        if (!StringUtils.equals(dto.getType(), QuestionType.WRITING.getType())) {
            log.warn("Cannot update point for question other than WRITING");
            throw new ScoringManuallyNotSupportedForQuestionTypeException(dto.getType());
        }
        var sessionUUID = UUID.fromString(sessionId);
        var question = questionRepository.findById(questionId)
                .orElseThrow(() -> {
                    log.warn("Question {} is not found", questionId);
                    return new QuestionNotFoundException(questionId);
                });
        if (dto.getPoint() > question.getPoint()) {
            throw new PointInvalidException();
        }
        var sessionResult = quizSessionResultRepository.findById(sessionUUID)
                .orElseThrow(() -> {
                    log.warn("Session {} has not ended", sessionId);
                    return new SessionHasNotEndedException(sessionId);
                });
        var sessionResultQuestion = sessionResult.getResultQuestions()
                .stream()
                .filter(s -> Objects.equals(s.getQuestion().getId(), questionId))
                .findAny()
                .orElseThrow(QuizHasNotScoredForStudentYet::new);
//        var groupQuestionOpt = groupQuestionRepository.findGroupQuestionByQuestionInGroup(questionId);
//        if (groupQuestionOpt.isPresent()) {
//            var groupQuestion = groupQuestionOpt.get();
//            var diff = 0.0;
//            if (sessionResultQuestion.getEarnedPoint() == null) {
//                diff = dto.getPoint();
//            } else {
//                diff = dto.getPoint() - sessionResultQuestion.getEarnedPoint();
//            }
//            var groupResult = sessionResultQuestionRepository.findByQuestionAndSession(groupQuestion.getId(), sessionUUID).get();
//            groupResult.setEarnedPoint(groupResult.getEarnedPoint() + diff);
//        }

        updateScore(dto, sessionResult, sessionResultQuestion);
        if (quizSessionResultRepository.isFullyGraded(sessionUUID)) {
            sessionResult.setGradedState(GradedState.DONE);
            var config = sessionResult.getSession().getQuiz().getConfig();
            if (config.getPassScore() != null) {
                if (sessionResult.getScore() >= config.getPassScore()) {
                    sessionResult.setFinalVerdict(FinalVerdict.PASSED);
                } else {
                    sessionResult.setFinalVerdict(FinalVerdict.FAILED);
                }
            }
        }
        sessionResultQuestionRepository.save(sessionResultQuestion);

        updateScoreByTag(sessionId, sessionResult);
    }

    private void updateScoreByTag(String sessionId, QuizSessionResultEntity sessionResult) {
        var quiz = sessionResult.getSession().getQuiz();
        var tag = quiz.getTag();
        var studentScore = gradeTagStudentRepository.findByTagScopeAndStudent(tag.getTitle(), tag.getScope(), tag.getScopeId(), sessionResult.getSession().getStudent().getId())
                .orElseThrow(() -> new SessionHasNotEndedException(sessionId));
        studentScore.setGrade(sessionResult.getScore());
    }

    private void updateScore(UpdatePointDto dto, QuizSessionResultEntity sessionResult, QuizSessionResultQuestionEntity sessionResultQuestion) {
        var diff = 0.0;
        if (sessionResultQuestion.getEarnedPoint() == null) {
            diff = dto.getPoint();
        } else {
            diff = dto.getPoint() - sessionResultQuestion.getEarnedPoint();
        }

        sessionResultQuestion.setEarnedPoint(dto.getPoint());

        sessionResult.addGrade(diff);
    }


    @Transactional(readOnly = true)
    public SessionResultPagedDto getSessionsResultForStudent(Long quizId) {
        var currentUser = userService.getCurrentUser();
        var result = quizSessionRepository.findByQuiz_User(quizId,
                Optional.ofNullable(currentUser.getId()),
                false,
                new BaseSearchCriteria());
        var quiz = quizClassRepository.findFetchConfigById(quizId)
                .orElseThrow(() -> new QuizNotFoundException(quizId));

        var dto = new SessionResultPagedDto();

        var config = quiz.getConfig();
        if (BooleanUtils.isTrue(config.getViewPreviousSessions()) && (config.getViewPreviousSessionsTime() != null && LocalDateTime.now().isAfter(config.getViewPreviousSessionsTime()))) {
            return mapSessionResult(result.getListData(), quiz, dto);
        } else {
            dto.setListData(
                    result.getListData()
                            .stream()
                            .map(quizSessionMapper::mapToSessionHideResult)
                            .collect(toList()));
            return dto;
        }
    }

    private SessionResultPagedDto mapSessionResult(List<QuizSessionEntity> result, QuizClassEntity quiz, SessionResultPagedDto dto) {
        var totalScore = 0;
        if (CollectionUtils.isNotEmpty(result)) {
            var questionsId = examQuestionSourceRepository.findQuestionsIdInExam(quiz.getExam().getId());
            totalScore = questionRepository.findByIdIn(questionsId)
                    .stream()
                    .mapToInt(QuestionEntity::getPoint)
                    .sum();
        }
        return mapSessionResult(result, dto, totalScore);
    }

    private SessionResultPagedDto mapSessionResult(List<QuizSessionEntity> result, SessionResultPagedDto dto, int totalScore) {
        var sessionsId = result.stream()
                .map(QuizSessionEntity::getId)
                .collect(toList());
        var unGradedSession = quizSessionResultRepository.findUnGradedSession(sessionsId);
        int finalTotalScore1 = totalScore;
        dto.setListData(
                result.stream()
                        .map(r -> mapToQuizSessionResultDto(r, unGradedSession, sessionsId.size(), finalTotalScore1, List.of()))
                        .collect(toList())
        );
        return dto;
    }

    @Transactional(readOnly = true)
    public SessionResultPagedDto getSessionResultOverview(Long quizId, Long userId, BaseSearchCriteria criteria) {
        List<QuizSessionEntity> result;
        PagedList<QuizSessionEntity> resultPaged = null;
        if (criteria.getPagination().getSize() == 0) {
            result = quizSessionRepository.findByQuiz(entityManager.getReference(QuizClassEntity.class, quizId));
        } else {
            resultPaged = quizSessionRepository.findByQuiz_User(quizId, Optional.ofNullable(userId), true, criteria);
            result = resultPaged.getListData();
        }
        var totalScore = 0;
        if (CollectionUtils.isNotEmpty(result)) {
            var questionsId = examQuestionSourceRepository.findQuestionsIdInExam(result.get(0).getQuiz().getExam().getId());
            totalScore = questionRepository.findByIdIn(questionsId)
                    .stream()
                    .mapToInt(QuestionEntity::getPoint)
                    .sum();
        }

        var latestSessionsId = result.stream()
                .collect(groupingBy(x -> x.getStudent().getId(), reducing((a, b) -> {
                    if (b.getSubmittedAt().isAfter(a.getSubmittedAt())) {
                        return b;
                    }
                    return a;
                })))
                .values()
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(QuizSessionEntity::getId)
                .collect(toList());

        var dto = new SessionResultPagedDto();

        if (userId == null) {
            var sessionsId = result.stream()
                    .map(QuizSessionEntity::getId)
                    .collect(toList());

            var ungradedSession = quizSessionResultRepository.findUnGradedSession(sessionsId);

            int finalTotalScore = totalScore;
            var data = result.stream()
                    .map(quizSessionEntity -> mapToQuizSessionResultDto(quizSessionEntity, ungradedSession, 1, finalTotalScore, latestSessionsId))
                    .collect(toList());

            dto.setListData(data);
            if (criteria.getPagination().getSize() != 0) {
                MapperUtils.mapPagedDto(dto, resultPaged);
            }
            return dto;
        } else {
            return mapSessionResult(result, dto, totalScore);
        }
    }

    private SessionResultDto mapToQuizSessionResultDto(QuizSessionEntity session, List<UUID> ungradedQuizSession, Integer attempt, Integer totalScore, List<UUID> lastSessions) {
        var sessionResultDto = quizSessionMapper.mapToSessionResultOverview(session);
        sessionResultDto.setAttempt(attempt);
        sessionResultDto.setTotalScore(totalScore);
        var isUngradedSession = ungradedQuizSession.stream()
                .filter(s -> StringUtils.equals(s.toString(), sessionResultDto.getSessionId()))
                .findAny();
        if (isUngradedSession.isPresent()) {
            sessionResultDto.setGradedState(GradedState.WAITING);
        } else {
            sessionResultDto.setGradedState(GradedState.DONE);
        }
        sessionResultDto.setIsLastSession(session.getLastSession());
        return sessionResultDto;
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_QUIZ_CLASS)
    public QuizEntryDto getQuizDetail(Long classId, Long resourceId) {
        var quiz = quizClassRepository.findFetchById(resourceId, String.format("%s-%s", QuizClassEntity.Fields.config, QuizClassEntity.Fields.exam))
                .orElseThrow(() -> new QuizNotFoundInClassException(resourceId, classId));

        var config = quiz.getConfig();
        var dto = quizMapper.mapToQuizEntry(quiz);
        dto.setTotalGrade(Math.toIntExact(examRepository.totalGradeOfExam(quiz.getExam().getId())));
        dto.setConfig(quizConfigMapper.mapToConfigDto(config));
        return dto;
    }

    public String getQuizResultById(Long quizId) {
        // TODO
        return null;
    }

    @Transactional
    public Long processAndSaveQuizResultById(Long classId, Long resourceId) {
        var students = classStudentRepository.getStudentsByClassId(classId);

        if (CollectionUtils.isEmpty(students)) {
            throw new ClassDoesNotHaveAnyStudent(classId);
        }

        var quiz = quizClassRepository.findFetchById(resourceId, QuizClassEntity.Fields.config)
                .orElseThrow(() -> new QuizNotFoundInClassException(resourceId, classId));
        var tag = quiz.getTag();
        tag.setHasGraded(true);
        tag.setGradedAt(Timestamp.valueOf(LocalDateTime.now()));

        // TODO: pass page and size to fetch all result
        var criteria = new BaseSearchCriteria();
        criteria.setPagination(new PaginationCriterion(0, 0));
        var sessionResult = quizSessionRepository.findByQuiz_User(resourceId, Optional.empty(), true, criteria);

        var resultByStudent = sessionResult.getListData().stream()
                .collect(groupingBy(x -> x.getStudent().getId()));

        var quizResult = new QuizResultEntity();

        var resultForStudent = students.stream()
                .map(student -> {
                    var finalResultForStudent = new QuizResultStudentEntity();
                    finalResultForStudent.setQuizResult(quizResult);
                    var resultList = resultByStudent.get(student.getStudent().getId());
                    // currently, only handle LAST_TIME strategy
                    var resultOpt = resultList.stream()
                            .reduce((a, b) -> {
                                if (a.getSubmittedAt().isBefore(b.getSubmittedAt())) {
                                    return b;
                                }
                                return a;
                            });
                    if (resultOpt.isPresent()) {
                        finalResultForStudent.setResult(resultOpt.get().getResult().getFinalVerdict());
                        finalResultForStudent.setNumberOfAttempt(resultList.size());
                        finalResultForStudent.setGrade(resultOpt.get().getResult().getScore());
                    } else {
                        finalResultForStudent.setResult(FinalVerdict.FAILED);
                        finalResultForStudent.setNumberOfAttempt(0);
                        finalResultForStudent.setGrade(0.0);
                    }

                    // TODO: tag
                    saveGradeTagStudent(tag, student, finalResultForStudent);

                    return finalResultForStudent;
                })
                .collect(toList());


        quizResult.setQuiz(quiz);
        quizResult.setNumberOfParticipants(
                (int) resultForStudent.stream()
                        .filter(r -> r.getNumberOfAttempt() != 0)
                        .count()
        );
        quizResult.setGpa(
                resultForStudent.stream()
                        .mapToDouble(QuizResultStudentEntity::getGrade)
                        .average()
                        .getAsDouble()
        );
        quizResult.setNumberOfPassedStudent(
                (int) resultForStudent.stream()
                        .filter(r -> r.getResult() == FinalVerdict.PASSED)
                        .count()
        );

        quizResultRepository.save(quizResult);
        return quizResult.getId();
    }

    private void saveGradeTagStudent(GradeTag tag, ClassStudentEntity student, QuizResultStudentEntity finalResultForStudent) {
        var gradeTagStudent = new GradeTagStudentEntity();
        gradeTagStudent.setTag(tag);
        gradeTagStudent.setStudent(student.getStudent());
        gradeTagStudent.setGrade(finalResultForStudent.getGrade());
        gradeTagStudentRepository.save(gradeTagStudent);
    }

    @Transactional
    @Auth(permission = PermissionEnum.UPDATE_TEST)
    public Long updateQuestion(Long resourceId, Long questionId, QuestionDto dto) {
        var question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(questionId));
        var point = question.getPoint();
        var questionOperation = questionOperationFactory.getQuestionOperation(dto.getType());
        var response = questionOperation.updateQuestion(questionId, dto);

        var groupOpt = groupQuestionRepository.findGroupByQuestion(questionId);
        if (groupOpt.isPresent()) {
            var group = groupOpt.get();
            var originalPoint = group.getGroup().getQuestion().getPoint();
            group.getGroup().getQuestion().setPoint(originalPoint + dto.getPoint() - point);
        }
        return questionId;
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_QUIZ_RESULT_MANAGEMENT)
    public TeacherQuizResultDto getQuizResultManagement(Long resourceId, BaseSearchCriteria criteria) {
        var dto = new TeacherQuizResultDto();
        dto.setAverageScore(quizResultRepository.calculateAverageByQuizId(resourceId));
        dto.setTotalParticipation(quizResultRepository.getParticipantsByQuizId(resourceId));
        dto.setTotalNumberOfPassedStudent(quizResultRepository.getPassedStudentsByQuizId(resourceId));

        var studentsResult = getSessionResultOverview(resourceId, null, criteria);
        dto.setSessionsResult(studentsResult);
        return dto;
    }

    private List<QuestionEntity> createNewList(QuestionEntity question, List<QuestionEntity> questions) {
        var list = new ArrayList<QuestionEntity>();
        list.add(question);
        list.addAll(questions);
        return list;
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_QUIZ_RESULT_MANAGEMENT)
    public List<QuestionStatistic> statisticQuiz(Long quizId) {
        var quiz = quizClassRepository.findFetchExamById(quizId)
                .orElseThrow(() -> new QuizNotFoundException(quizId));
        var course = quiz.getExam().getCourse();
        var sessions = quizSessionRepository.findEndedQuizSessionByQuiz(quizId);
        var sessionIdList = sessions
                .stream()
                .map(QuizSessionEntity::getId)
                .collect(toList());
        var questionsId = examQuestionSourceRepository.findQuestionsIdInExam(quiz.getExam().getId());
        var questions = questionRepository.findByIdIn(questionsId)
                .stream()
                .map(q -> StringUtils.equals(q.getType(), "GROUP") ? createNewList(q, q.getGroupQuestion()
                        .getQuestions()
                        .stream()
                        .map(QuestionInGroupEntity::getQuestion)
                        .collect(toList())): List.of(q))
                .flatMap(List::stream)
                .collect(toList());

        var answers = answerQuestionTemporaryRepository.findAnswerQuestionBySessionIn(sessionIdList);

        var resultQuestions = quizSessionResultRepository.findResultQuestionBySessionIn(sessionIdList);
        var sessionCount = quizSessionRepository.countQuizSessionByQuiz(quizId);

        var resultByQuestions = resultQuestions.stream()
                .collect(groupingBy(q -> q.getQuestion().getId(), collectingAndThen(toList(), list -> {
                    var questionStatistic = new QuestionStatistic();
                    questionStatistic.setCourseName(course.getName());
                    questionStatistic.setCourseCode(course.getCode());

                    var average = list.stream()
                            .filter(x -> x.getEarnedPoint() != null)
                            .collect(averagingDouble(x -> x == null ? 0.0 : x.getEarnedPoint()));
                    questionStatistic.setAverageScore(average);

                    questionStatistic.setNotAnswered((int) (sessionCount - list.size()));

                    var questionId = list.get(0).getQuestion().getId();
                    questionStatistic.setId(questionId);
                    var question = questions.stream()
                            .filter(q -> Objects.equals(q.getId(), questionId))
                            .findAny()
                            .get();
                    var description = question.getDescription();
                    questionStatistic.setDescription(description);
                    questionStatistic.setType(question.getType());


                    questionStatistic.setNeedGrading(StringUtils.equals(question.getType(), QuestionType.WRITING.getType()));

                    return questionStatistic;
                })));

        resultByQuestions.forEach((questionId, statistic) -> {
            if (StringUtils.equals(statistic.getType(), "GROUP")) {
                statistic.setNotAnswered(null);
            } else {
                statistic.setNotAnswered((int) (sessions.size() - answers.stream()
                        .filter(ans -> Objects.equals(ans.getQuestion().getId(), questionId))
                        .count()));
            }
        });

        return new ArrayList<>(resultByQuestions.values());
    }

    @Transactional(readOnly = true)
    public byte[] statisticsQuiz(Long quizId) throws JRException {
        var pagination = new PaginationCriterion(0, 0);
        var criteria = new BaseSearchCriteria();
        criteria.setPagination(pagination);
        var quizSessionResultStatistic = getSessionResultOverview(quizId, null, criteria).getListData();

        Map<String, Object> parameters = new HashMap<>();

        InputStream transactionReportStream =
                getClass()
                        .getResourceAsStream(
                                "/templates/reports/quiz_session" + ".jrxml");

        JasperReport quizSessionResultJasperReport = JasperCompileManager.compileReport(transactionReportStream);
        JRBeanCollectionDataSource beanColDataSource =
                new JRBeanCollectionDataSource(quizSessionResultStatistic);

        ArrayList<JasperPrint> sheets = new ArrayList<>();

        JasperPrint quizSessionResult =
                JasperFillManager.fillReport(quizSessionResultJasperReport, parameters, beanColDataSource);
        quizSessionResult.setPageHeight(30  + 40 * quizSessionResultStatistic.size());

        sheets.add(quizSessionResult);


        var quizQuestionStatistic = statisticQuiz(quizId);
        transactionReportStream =
                getClass()
                        .getResourceAsStream(
                                "/templates/reports/quiz_question" + ".jrxml");

        JasperReport quizQuestionResultJasperReport = JasperCompileManager.compileReport(transactionReportStream);
        JRBeanCollectionDataSource quizQuestionDataSource =
                new JRBeanCollectionDataSource(quizQuestionStatistic);

        JasperPrint quizQuestionResult =
                JasperFillManager.fillReport(quizQuestionResultJasperReport, parameters, quizQuestionDataSource);
        quizQuestionResult.setPageHeight(30  + 40 * quizQuestionStatistic.size());

        sheets.add(quizQuestionResult);


        byte[] bytes = null;
        var input = SimpleExporterInput.getInstance(sheets);
        try (var byteArray = new ByteArrayOutputStream()) {
            var output = new SimpleOutputStreamExporterOutput(byteArray);
            var exporter = new JRXlsxExporter();
            exporter.setExporterInput(input);
            exporter.setExporterOutput(output);
            exporter.exportReport();
            bytes = byteArray.toByteArray();
            output.close();
        } catch (IOException e) {
            log.error("Exception when preparing export for quiz {}, message: {}", quizId, e);
        }
        return bytes;
    }
}