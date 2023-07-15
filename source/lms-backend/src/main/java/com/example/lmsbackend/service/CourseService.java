package com.example.lmsbackend.service;

import com.example.lmsbackend.config.security.aop.Auth;
import com.example.lmsbackend.constant.AppConstant;
import com.example.lmsbackend.criteria.BaseSearchCriteria;
import com.example.lmsbackend.domain.course.ChapterActivityCourseEntity;
import com.example.lmsbackend.domain.course.ChapterActivityCourseKey;
import com.example.lmsbackend.domain.course.CourseTextbookEntity;
import com.example.lmsbackend.domain.course.CourseTextbookKey;
import com.example.lmsbackend.domain.coursemodel.*;
import com.example.lmsbackend.domain.exam.GradeTagScope;
import com.example.lmsbackend.domain.resource.TextbookEntity;
import com.example.lmsbackend.dto.classes.ClassDto;
import com.example.lmsbackend.dto.classes.ClassPagedDto;
import com.example.lmsbackend.dto.request.course.CourseDto;
import com.example.lmsbackend.dto.request.course.ReorderActivityDto;
import com.example.lmsbackend.dto.request.course.ReorderChapterDto;
import com.example.lmsbackend.dto.resource.TextbookDto;
import com.example.lmsbackend.dto.response.course.*;
import com.example.lmsbackend.dto.textbook.AddTextbookToCourseRequest;
import com.example.lmsbackend.enums.ActionType;
import com.example.lmsbackend.enums.PermissionEnum;
import com.example.lmsbackend.exceptions.aclass.UnitNotFoundException;
import com.example.lmsbackend.exceptions.course.*;
import com.example.lmsbackend.exceptions.exam.ExamNotFoundException;
import com.example.lmsbackend.mapper.*;
import com.example.lmsbackend.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;

import static java.util.stream.Collectors.*;

@Service
@Slf4j
public class CourseService {

    private CourseRepository courseRepository;
    private ChapterCourseRepository chapterCourseRepository;
    private TextbookRepository textbookRepository;


    private CourseMapper courseMapper;
    private ChapterMapper chapterMapper;
    private QuizMapper quizMapper;
    private UnitMapper unitMapper;
    private EntityManager entityManager;
    private UnitCourseRepository unitCourseRepository;
    private QuizCourseRepository quizCourseRepository;
    private ChapterActivityCourseRepository chapterActivityCourseRepository;
    private ClassRepository classRepository;
    private GradeTagRepository gradeTagRepository;
    private UtilsService utilsService;
    private ExamRepository examRepository;
    private CourseTextbookRepository courseTextbookRepository;

    private ClassService classService;
    private TextBookMapper textBookMapper;

    @Autowired
    public CourseService(CourseRepository courseRepository,
                         ChapterCourseRepository chapterCourseRepository,
                         TextbookRepository textbookRepository,
                         CourseMapper courseMapper,
                         ChapterMapper chapterMapper,
                         QuizMapper quizMapper,
                         UnitMapper unitMapper,
                         EntityManager entityManager,
                         UnitCourseRepository unitCourseRepository,
                         QuizCourseRepository quizCourseRepository,
                         ChapterActivityCourseRepository chapterActivityCourseRepository,
                         ClassRepository classRepository,
                         GradeTagRepository gradeTagRepository,
                         UtilsService utilsService,
                         ExamRepository examRepository,
                         @Lazy ClassService classService,
                         CourseTextbookRepository courseTextbookRepository,
                         TextBookMapper textBookMapper) {
        this.courseRepository = courseRepository;
        this.chapterCourseRepository = chapterCourseRepository;
        this.textbookRepository = textbookRepository;
        this.courseMapper = courseMapper;
        this.chapterMapper = chapterMapper;
        this.quizMapper = quizMapper;
        this.unitMapper = unitMapper;
        this.entityManager = entityManager;
        this.unitCourseRepository = unitCourseRepository;
        this.quizCourseRepository = quizCourseRepository;
        this.chapterActivityCourseRepository = chapterActivityCourseRepository;
        this.classRepository = classRepository;
        this.gradeTagRepository = gradeTagRepository;
        this.utilsService = utilsService;
        this.examRepository = examRepository;
        this.classService = classService;
        this.courseTextbookRepository = courseTextbookRepository;
        this.textBookMapper = textBookMapper;
    }

    @Transactional
    @Auth(permission = PermissionEnum.CREATE_COURSE)
    public void createCourse(CourseDto request) {
        var course = courseMapper.mapToCourseEntity(request);
        validateCourseCodeExists(request);
        setLearningContent(course);
        courseRepository.save(course);
    }

    private void setLearningContent(CourseEntity course) {
        var content = new CourseLearningContentEntity();
        content.setCourse(course);
    }

    private void validateCourseCodeExists(CourseDto request) {
        var existingCourseOpt = courseRepository.findByCode(request.getCode());
        if (existingCourseOpt.isPresent()) {
            log.warn("Course code {} is already existing", request.getCode());
            throw new CourseCodeAlreadyExistException(request.getCode());
        }
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_ALL_COURSE)
    @Cacheable("courses")
    public CoursesPagedDto findCourses(BaseSearchCriteria criteria) {
        var coursesPaged = courseRepository.findCourses(criteria);
        var coursesDto = new CoursesPagedDto();
        MapperUtils.mapPagedDto(coursesDto, coursesPaged);
        coursesDto.setListData(
                coursesPaged
                        .stream()
                        .map(courseMapper::mapToCourseInformation)
                        .collect(toList())
        );
        return coursesDto;
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_DETAIL_COURSE)
    @Cacheable("course")
    public CourseDto findCourseById(Long resourceId) {
        var course = courseRepository.findById(resourceId)
                .orElseThrow(() -> new CourseNotFoundException(resourceId));
        var courseDto = courseMapper.mapToCourseDto(course);
        return courseDto;
    }

    @Transactional
    @Auth(permission = PermissionEnum.UPDATE_COURSE)
    public CourseDto updateCourse(Long id, CourseDto dto) {
        var course = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException(id));
        courseMapper.mapToCourse(course, dto);
        return dto;
    }

    private UnitCourseEntity mapUnit(UnitDto dto) {
        var unit = unitMapper.mapToUnitEntity(dto);
        if (CollectionUtils.isNotEmpty(dto.getTextbooks())) {
            var textbooksId = extractTextbookId(dto);

            var textbooks = textbookRepository.findFetchUnitCourse(textbooksId);
            var textBookUnit = textbooks
                    .stream()
                    .map(textbook -> buildUnitTextbookEntity(dto, unit, textbook))
                    .collect(toSet());
            unit.setTextbooks(textBookUnit);
        }
        return unit;
    }

    private UnitCourseTextBookEntity buildUnitTextbookEntity(UnitDto dto, UnitCourseEntity unit, TextbookEntity textbook) {
        var unitTextBook = new UnitCourseTextBookEntity();
        unitTextBook.setUnit(unit);
        unitTextBook.setTextbook(textbook);
        var textbookInUnit = dto.getTextbooks()
                .stream()
                .filter(t -> t.getTextbookId() == textbook.getId())
                .findAny()
                .orElseThrow(() -> new RuntimeException());
        unitTextBook.setNote(textbookInUnit.getNote());
        return unitTextBook;
    }

    public static Set<Long> extractTextbookId(UnitDto dto) {
        return dto.getTextbooks()
                .stream()
                .map(UnitDto.TextbookInUnit::getTextbookId)
                .collect(toSet());
    }

    @Transactional
    @Auth(permission = PermissionEnum.CREATE_LEARNING_CONTENT)
    public ChapterDto createChapter(Long resourceId, ChapterDto dto) {
        var learningContent = courseRepository.findLearningContentById(resourceId)
                .orElseThrow(() -> new CourseNotFoundException(resourceId));

        var chapter = chapterMapper.mapToChapterEntity(dto);

        var currentMaxSortIndex = chapterCourseRepository.findMaxSortIndex(resourceId).orElse(0) + 1;
        chapter.setOrder(currentMaxSortIndex);
        chapter.setCourseContent(learningContent);
        chapterCourseRepository.save(chapter);

        classService.cascadeAddChapter(chapter, resourceId);

        return chapterMapper.mapToChapterDto(chapter);
    }

    @Transactional
    @Auth(permission = PermissionEnum.UPDATE_LEARNING_CONTENT)
    public ChapterDto updateChapter(Long resourceId, Long chapterId, ChapterDto dto) {
        var chapter = chapterCourseRepository.findById(chapterId)
                .orElseThrow(() -> new ChapterNotFoundException(chapterId));
        chapter.setTitle(dto.getTitle());

        classService.cloneChapterOnUpdated(chapter);

        return chapterMapper.mapToChapterDto(chapter);
    }

    @Transactional
    @Auth(permission = PermissionEnum.DELETE_LEARNING_CONTENT)
    public Long deleteChapter(Long resourceId, Long chapterId) {
        var learningContent = courseRepository.findLearningContentById(resourceId)
                .orElseThrow(() -> new CourseNotFoundException(resourceId));

        var chapterOpt = learningContent.getChapters()
                .stream()
                .filter(c -> Objects.equals(c.getId(), chapterId))
                .findAny();

        if (chapterOpt.isEmpty()) {
            return chapterId;
        }

        classService.cloneChapterOnDeleted(chapterId);

        var chapterActions = chapterActivityCourseRepository.findChapterActionByChapter(chapterId)
                .stream()
                .collect(groupingBy(ChapterActivityCourseKey::getActivityType, mapping(ChapterActivityCourseKey::getActivityId, toList())));

        learningContent.getChapters().remove(chapterOpt.get());
        chapterCourseRepository.deleteById(chapterId);
        entityManager.createQuery("DELETE FROM QuizCourseEntity q WHERE q.id IN (:ids)")
                .setParameter("ids", chapterActions.getOrDefault(ActionType.QUIZ.name(), new ArrayList<>()))
                .executeUpdate();
        entityManager.createQuery("DELETE FROM UnitCourseEntity q WHERE q.id IN (:ids)")
                .setParameter("ids", chapterActions.getOrDefault(ActionType.UNIT.name(), new ArrayList<>()))
                .executeUpdate();
        entityManager.createQuery("DELETE FROM ChapterActivityCourseEntity c WHERE c.chapter.id = (:chapterId)")
                .setParameter("chapterId", chapterId)
                .executeUpdate();

        return chapterId;
    }

    @Transactional
    @Auth(permission = PermissionEnum.CREATE_LEARNING_CONTENT)
    public UnitDto createUnit(Long resourceId, Long chapterId, UnitDto dto) {
        var chapter = chapterCourseRepository.findFetchActionsById(chapterId)
                .orElseThrow(() -> new ChapterNotFoundException(chapterId));

        var unit = persistUnit(resourceId, dto);

        classService.cascadeAddUnit(unit, chapterId, resourceId);

        var sortIndex = buildChapterSortIndex(chapter, unit.getId(), ActionType.UNIT.name(), resourceId);

        dto.setId(unit.getId());
        dto.setOrder(sortIndex.getOrder());
        return dto;
    }

    private ChapterActivityCourseEntity buildChapterSortIndex(ChapterCourseEntity chapter, Long actionId, String actionType, Long courseId) {
        var chapterSortIndex = new ChapterActivityCourseEntity();
        chapterSortIndex.setId(new ChapterActivityCourseKey());
        chapterSortIndex.getId().setActivityId(actionId);
        chapterSortIndex.getId().setActivityType(actionType);
        var currentMaxSortIndex = courseRepository.findActionMaxSortIndexInChapter(chapter.getId()).orElse(0) + 1;
        chapterSortIndex.setOrder(currentMaxSortIndex);
        chapterSortIndex.setChapter(chapter);
        return chapterSortIndex;
    }

    private UnitCourseEntity persistUnit(Long courseId, UnitDto dto) {
        var unit = mapUnit(dto);
        unit.setCourse(entityManager.getReference(CourseEntity.class, courseId));
        return unitCourseRepository.save(unit);
    }

    @Transactional
    @Auth(permission = PermissionEnum.CREATE_LEARNING_CONTENT)
    public QuizDto createQuiz(Long resourceId, Long chapterId, QuizDto dto) {
        var chapter = chapterCourseRepository.findFetchActionsById(chapterId)
                .orElseThrow(() -> new ChapterNotFoundException(chapterId));
        var quiz = persistQuiz(dto, resourceId);
        classService.cascadeAddQuiz(quiz, chapterId, resourceId);

        var sortIndex = buildChapterSortIndex(chapter, quiz.getId(), ActionType.QUIZ.name(), resourceId);

        dto.setId(quiz.getId());
        dto.setOrder(sortIndex.getOrder());
        return dto;
    }

    private QuizCourseEntity persistQuiz(QuizDto dto, Long courseId) {
        var quiz = quizMapper.mapToQuizEntity(dto);
        if (dto.getExamId() != null) {
            var exam = examRepository.findFetchQuizCourseById(dto.getExamId())
                    .orElseThrow(() -> new ExamNotFoundException(dto.getExamId()));
            quiz.setExam(exam);
        }
        quiz.setCourse(entityManager.getReference(CourseEntity.class, courseId));
        quiz = quizCourseRepository.save(quiz);
        var tag = utilsService.persistScoreTag(dto.getTag(), courseId, GradeTagScope.COURSE);
        quiz.setTag(tag);
        entityManager.flush();
        return quiz;
    }


    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_LEARNING_CONTENT)
    @Cacheable("learning_content_in_course")
    public List<ChapterDto> getLearningContent(Long resourceId) {
        if (!courseRepository.existsById(resourceId)) {
            throw new CourseNotFoundException(resourceId);
        }
        var chapters = chapterCourseRepository.findFetchActionsByLearningContent(resourceId);
        var actions = chapters.stream()
                .map(ChapterCourseEntity::getActions)
                .flatMap(Set::stream)
                .collect(groupingBy(x -> x.getId().getActivityType()));

        var unitActions = actions.getOrDefault(ActionType.UNIT.name(), new ArrayList<>());
        var units = unitCourseRepository.findUnitsDtoByIdIn(unitActions
                .stream()
                .map(x -> x.getId().getActivityId())
                .collect(toList()));
        var quizActions = actions.getOrDefault(ActionType.QUIZ.name(), new ArrayList<>());
        var quizzes = quizCourseRepository.findQuizzesDtoByIdIn(quizActions
                .stream()
                .map(x -> x.getId().getActivityId())
                .collect(toList()));
        return chapters.stream()
                .map(chapter -> {
                    var dto = chapterMapper.mapToChapterDto(chapter);
                    mapUnits(actions, units, chapter, dto);
                    mapQuizzes(actions, quizzes, chapter, dto);
                    return dto;
                })
                .collect(toList());
    }

    private List<ChapterDto> getDetailLearningContent(Long resourceId) {
        if (!courseRepository.existsById(resourceId)) {
            throw new CourseNotFoundException(resourceId);
        }
        var chapters = chapterCourseRepository.findFetchActionsByLearningContent(resourceId);
        var actions = chapters.stream()
                .map(ChapterCourseEntity::getActions)
                .flatMap(Set::stream)
                .collect(groupingBy(x -> x.getId().getActivityType()));

        var unitActions = actions.getOrDefault(ActionType.UNIT.name(), new ArrayList<>());
        var units = unitCourseRepository.findFetchTextbookUnitsDtoByIdIn(unitActions
                .stream()
                .map(x -> x.getId().getActivityId())
                .collect(toList()))
                .stream()
                .collect(groupingBy(UnitCourseEntity::getId, collectingAndThen(toList(), list -> {
                    var unit = list.get(0);
                    var unitDto = new UnitDto();
                    unitDto.setId(unit.getId());
                    unitDto.setTitle(unit.getTitle());
                    unitDto.setTextbooks(
                            unit.getTextbooks()
                                    .stream()
                                    .map(u -> {
                                        var d = new UnitDto.TextbookInUnit();
                                        d.setTextbookId(u.getId().getTextbookId());
                                        return d;
                                    })
                                    .collect(toList())
                    );
                    return unitDto;
                })))
                .values();
        var quizActions = actions.getOrDefault(ActionType.QUIZ.name(), new ArrayList<>());
        var quizzes = quizCourseRepository.findQuizByIdIn(quizActions
                .stream()
                .map(x -> x.getId().getActivityId())
                .collect(toList()))
                .stream()
                .collect(groupingBy(QuizCourseEntity::getId, collectingAndThen(toList(), list -> {
                    var quiz = list.get(0);
                    var quizDto = new QuizDto();
                    quizDto.setId(quiz.getId());
                    quizDto.setTitle(quiz.getTitle());
                    if (quiz.getExam() == null) {
                        return quizDto;
                    }
                    var textbooks = quiz.getExam().getTextbooks();
                    quizDto.setTextbooks(
                            textbooks
                                    .stream()
                                    .map(u -> {
                                        var d = new TextbookRef();
                                        d.setTextbookId(u.getId().getTextbookId());
                                        return d;
                                    })
                                    .collect(toList())
                    );
                    return quizDto;
                })))
                .values();
        return chapters.stream()
                .map(chapter -> {
                    var dto = chapterMapper.mapToChapterDto(chapter);
                    mapUnits(actions, units, chapter, dto);
                    mapQuizzes(actions, quizzes, chapter, dto);
                    return dto;
                })
                .collect(toList());
    }

    @Transactional(readOnly = true)
    @Cacheable("learning_content_in_course_by_textbook")
    public List<ChapterGroupByTextbookDto> getLearningContentGroupByTextbook(Long courseId) {
        var chapters = getDetailLearningContent(courseId);
        var textbooks = getTextbooksInCourse(courseId, StringUtils.EMPTY);
        var groupByTextbook = textbooks.stream()
                .map(textbook -> {
                    var dto = new ChapterGroupByTextbookDto();
                    dto.setTextbookId(textbook.getId());
                    dto.setTextbookName(textbook.getName());
                    var chaptersDto = chapters.stream()
                            .map(chapter -> {
                                var chapterDto = new ChapterDto();
                                chapterDto.setId(chapter.getId());
                                chapterDto.setOrder(chapter.getOrder());
                                chapterDto.setTitle(chapter.getTitle());
                                var units = chapter.getUnits();
                                var quizzes = chapter.getQuizzes();
                                chapterDto.setUnits(units.stream()
                                        .filter(unit -> unit.getTextbooks()
                                                .stream()
                                                .anyMatch(t -> Objects.equals(t.getTextbookId(), textbook.getId())))
                                        .collect(toList()));
                                chapterDto.setQuizzes(quizzes.stream()
                                        .filter(quiz -> quiz.getTextbooks()
                                                .stream()
                                                .anyMatch(t -> Objects.equals(t.getTextbookId(), textbook.getId())))
                                        .collect(toList()));
                                return chapterDto;
                            })
                            .collect(toList());
                    dto.setChapters(
                            chaptersDto.stream()
                                    .filter(c -> CollectionUtils.isNotEmpty(c.getUnits()) || CollectionUtils.isNotEmpty(c.getQuizzes()))
                                    .collect(toList())
                    );
                    return dto;
                })
                .collect(toList());
        var notTextbookChapters = chapters.stream()
                .map(chapter -> {
                    var chapterDto = new ChapterDto();
                    chapterDto.setId(chapter.getId());
                    chapterDto.setOrder(chapter.getOrder());
                    chapterDto.setTitle(chapter.getTitle());
                    var units = chapter.getUnits();
                    var quizzes = chapter.getQuizzes();
                    chapterDto.setUnits(units.stream()
                            .filter(unit -> CollectionUtils.isEmpty(unit.getTextbooks()))
                            .collect(toList()));
                    chapterDto.setQuizzes(quizzes.stream()
                            .filter(quiz -> CollectionUtils.isEmpty(quiz.getTextbooks()))
                            .collect(toList()));
                    if (CollectionUtils.isNotEmpty(chapterDto.getUnits()) || CollectionUtils.isNotEmpty(chapterDto.getQuizzes())) {
                        return chapterDto;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(toList());
        if (CollectionUtils.isNotEmpty(notTextbookChapters)) {
            var chapterTextbookDto = new ChapterGroupByTextbookDto();
            chapterTextbookDto.setTextbookId(-1L);
            chapterTextbookDto.setTextbookName("Không thuộc giáo trình nào");
            chapterTextbookDto.setChapters(notTextbookChapters);
            groupByTextbook.add(chapterTextbookDto);
        }
        return groupByTextbook;
    }

    private void mapQuizzes(Map<String, List<ChapterActivityCourseEntity>> actions, Collection<QuizDto> quizzes, ChapterCourseEntity chapter, ChapterDto dto) {
        var quizzesIdByChapter = actions.getOrDefault(ActionType.QUIZ.name(), new ArrayList<>())
                .stream()
                .filter(action -> Objects.equals(action.getChapter().getId(), chapter.getId()))
                .collect(toList());
        var quizzesByChapter = new ArrayList<QuizDto>();
        quizzes.forEach(quiz -> {
            var actionOpt = quizzesIdByChapter.stream()
                    .filter(x -> Objects.equals(x.getId().getActivityId(), quiz.getId()))
                    .findAny();
            if (actionOpt.isPresent()) {
                quiz.setOrder(actionOpt.get().getOrder());
                quizzesByChapter.add(quiz);
            }
        });
        dto.setQuizzes(quizzesByChapter.stream()
                .sorted(Comparator.comparingInt(QuizDto::getOrder))
                .collect(toList()));
    }

    private void mapUnits(Map<String, List<ChapterActivityCourseEntity>> actions, Collection<UnitDto> units, ChapterCourseEntity chapter, ChapterDto dto) {
        var unitsIdByChapter = actions.getOrDefault(ActionType.UNIT.name(), new ArrayList<>())
                .stream()
                .filter(action -> Objects.equals(action.getChapter().getId(), chapter.getId()))
                .collect(toList());
        var unitsByChapter = new ArrayList<UnitDto>();
        units.forEach(unit -> {
            var actionOpt = unitsIdByChapter.stream()
                    .filter(x -> Objects.equals(x.getId().getActivityId(), unit.getId()))
                    .findAny();
            if (actionOpt.isPresent()) {
                unit.setOrder(actionOpt.get().getOrder());
                unitsByChapter.add(unit);
            }
        });
        dto.setUnits(unitsByChapter.stream()
                .sorted(Comparator.comparingInt(UnitDto::getOrder))
                .collect(toList()));
    }

    public List<UnitCourseEntity> findUnitsInCourse(Long courseId) {
        var action = chapterActivityCourseRepository.findChapterActionByCourse(courseId, ActionType.UNIT.name());
        var unitsId = action.stream()
                .map(ChapterActivityCourseEntity::getId)
                .map(ChapterActivityCourseKey::getActivityId)
                .collect(toList());
        var units = unitCourseRepository.findAllById(unitsId);
        return units.stream()
                .map(unit -> {
                    var matchAction = action.stream()
                            .filter(a -> a.getId().getActivityId() == unit.getId())
                            .findAny()
                            .get();
                    unit.setOrder(matchAction.getOrder());
                    return unit;
                })
                .collect(toList());
    }


    public List<QuizCourseEntity> findQuizzesInCourse(Long courseId) {
        var action = chapterActivityCourseRepository.findChapterActionByCourse(courseId, ActionType.QUIZ.name());
        var quizzesId = action.stream()
                .map(ChapterActivityCourseEntity::getId)
                .map(ChapterActivityCourseKey::getActivityId)
                .collect(toList());
        var quizzes = quizCourseRepository.findAllById(quizzesId);
        return quizzes.stream()
                .map(quiz -> {
                    var matchAction = action.stream()
                            .filter(a -> a.getId().getActivityId() == quiz.getId())
                            .findAny()
                            .get();
                    quiz.setOrder(matchAction.getOrder());
                    return quiz;
                })
                .collect(toList());
    }

    @Transactional
    @Auth(permission = PermissionEnum.DELETE_COURSE)
    public Long deleteCourse(Long resourceId) {
        CourseEntity course = courseRepository.findById(resourceId)
                .orElseThrow(() -> new CourseNotFoundException(resourceId));
        if (!course.getClasses().isEmpty()) {
            throw new CourseHasClassOpenedException();
        }
        var actions = chapterActivityCourseRepository.findChapterActionByCourse(resourceId)
                .stream()
                .collect(groupingBy(x -> x.getId().getActivityType(), mapping(x -> x.getId().getActivityId(), toList())));
        entityManager.createQuery("DELETE FROM QuizCourseEntity q WHERE q.id IN (:ids)")
                .setParameter("ids", actions.getOrDefault(ActionType.QUIZ.name(), new ArrayList<>()))
                .executeUpdate();
        entityManager.createQuery("DELETE FROM UnitCourseEntity q WHERE q.id IN (:ids)")
                .setParameter("ids", actions.getOrDefault(ActionType.UNIT.name(), new ArrayList<>()))
                .executeUpdate();
        courseRepository.deleteById(resourceId);
//        classService.cascadeDeleteClass(resourceId);
        return resourceId;
    }

    @Transactional(readOnly = true)
    public List<ClassDto> findOngoingClass(Long id) {
        return classRepository.findOngoingClassByCourse(id);
    }

    @Transactional
    @Auth(permission = PermissionEnum.DELETE_LEARNING_CONTENT)
    public Long deleteQuiz(Long resourceId, Long quizId) {
        deleteChapterAction(quizId, ActionType.QUIZ.name());
        classService.cascadeDeleteQuiz(resourceId, quizId);
        var tagId = entityManager.createQuery("SELECT q.tag.id FROM QuizCourseEntity q WHERE q.id = :quizId")
                .setParameter("quizId", quizId)
                .getResultList();

        entityManager.createQuery("DELETE FROM QuizCourseEntity q WHERE q.id IN (:ids)")
                .setParameter("ids", List.of(quizId))
                .executeUpdate();

        if (CollectionUtils.isNotEmpty(tagId)) {
            entityManager.createQuery("DELETE FROM GradeTag g WHERE g.id = :tagId")
                    .setParameter("tagId", tagId.get(0))
                    .executeUpdate();
        }

        return quizId;
    }

    @Transactional
    @Auth(permission = PermissionEnum.DELETE_LEARNING_CONTENT)
    public Long deleteUnit(Long resourceId, Long unitId) {
        deleteChapterAction(unitId, ActionType.UNIT.name());
        classService.cascadeDeleteUnit(resourceId, unitId);
        entityManager.createQuery("DELETE FROM UnitCourseEntity q WHERE q.id IN (:ids)")
                .setParameter("ids", List.of(unitId))
                .executeUpdate();
        return unitId;
    }

    private void deleteChapterAction(Long id, String type) {
        entityManager.createQuery("DELETE FROM ChapterActivityCourseEntity c WHERE c.id.activityId = (:id) AND c.id.activityType = (:type)")
                .setParameter("id", id)
                .setParameter("type", type)
                .executeUpdate();
    }

    @Transactional
    @Auth(permission = PermissionEnum.UPDATE_LEARNING_CONTENT)
    public QuizDto updateQuiz(Long resourceId, Long quizId, QuizDto dto) {
        var quiz = quizCourseRepository.findFetchTagById(quizId)
                .orElseThrow(() -> new QuizNotFoundException(quizId));
        quizMapper.mapToQuizEntity(quiz, dto);
        quiz.removeExam();
        if (dto.getExamId() != null) {
            var exam = examRepository.findFetchQuizCourseById(dto.getExamId())
                    .orElseThrow(() -> new ExamNotFoundException(dto.getExamId()));
            quiz.setExam(exam);
        }
        var tag = quiz.getTag();
        tag.setTitle(dto.getTag());
        classService.cloneQuizOnUpdated(quiz, resourceId);
        return quizMapper.mapToQuizDto(quiz);
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_LEARNING_CONTENT)
    @Cacheable("quiz_in_course")
    public QuizDto findQuiz(Long resourceId, Long quizId) {
        return quizMapper.mapToQuizDto(quizCourseRepository.findById(quizId)
                .orElseThrow(() -> new QuizNotFoundException(quizId)));
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_LEARNING_CONTENT)
    @Cacheable("unit_in_course")
    public UnitDto getUnit(Long resourceId, Long unitId) {
        return unitMapper.mapToUnitDto(unitCourseRepository.findById(unitId)
                .orElseThrow(() -> new UnitNotFoundException(unitId)));
    }

    @Transactional
    @Auth(permission = PermissionEnum.UPDATE_LEARNING_CONTENT)
    public UnitDto updateUnit(Long resourceId, Long unitId, UnitDto dto) {
        unitCourseRepository.deleteAllByUnit(unitId);
        var unit = unitCourseRepository.findUnitFetchTextbookById(unitId)
                .orElseThrow(() -> new UnitNotFoundException(unitId));
        entityManager.detach(unit);

        unitMapper.mapToUnitEntity(unit, dto);

        if (CollectionUtils.isNotEmpty(dto.getTextbooks())) {
            var textbooksId = dto.getTextbooks()
                    .stream()
                    .map(UnitDto.TextbookInUnit::getTextbookId)
                    .collect(toSet());
            setTextbook(dto, unit, textbooksId);
        }
        var managedUnit = entityManager.merge(unit);

        entityManager.flush();

        classService.cloneUnitOnUpdated(unit);

        return unitMapper.mapToUnitDto(managedUnit);
    }

    private void setTextbook(UnitDto dto, UnitCourseEntity unit, Set<Long> textbooksId) {
        var textbooks = textbookRepository.findByIdIn(textbooksId);
        var textBookUnit = textbooks
                .stream()
                .map(textbook -> {
                    var unitTextBook = new UnitCourseTextBookEntity();
                    unitTextBook.setUnit(unit);
                    unitTextBook.setTextbook(textbook);
                    var textbookInUnit = dto.getTextbooks()
                            .stream()
                            .filter(t -> t.getTextbookId() == textbook.getId())
                            .findAny()
                            .orElseThrow(() -> new RuntimeException());
                    unitTextBook.setNote(textbookInUnit.getNote());
                    return unitTextBook;
                })
                .collect(toSet());
        unit.setTextbooks(textBookUnit);
    }

    @Transactional
    @Auth(permission = PermissionEnum.UPDATE_LEARNING_CONTENT)
    public void reorderChapters(Long resourceId, ReorderChapterDto dto) {
        if (classRepository.countCreatedOngoingClassByCourse(resourceId) > 0) {
            throw new DragAndDropIsNotAllowedException();
        }

        var fromId = dto.getFrom().getId();
        var fromSortIndex = dto.getFrom().getOrder();
        var toSortIndex = dto.getTo().getOrder();
        var chapter = chapterCourseRepository.findById(fromId)
                .orElseThrow(() -> new ChapterNotFoundException(fromId));
        chapter.setOrder(-1);
        entityManager.flush();
        if (fromSortIndex < toSortIndex) {
            var chapters = chapterCourseRepository.findByCourseInRange(resourceId, fromSortIndex, toSortIndex);
            chapters.forEach(c -> c.setOrder(c.getOrder() - 1));
            chapter.setOrder(toSortIndex);
        } else if (fromSortIndex > toSortIndex) {
            var chapters = chapterCourseRepository.findByCourseInRange(resourceId, toSortIndex - 1, fromSortIndex - 1);
            chapters.forEach(c -> c.setOrder(c.getOrder() + 1));
            chapter.setOrder(toSortIndex);
        }
        entityManager.flush();
        classService.cascadeReorderChapters(resourceId, dto);
    }

    @Transactional
    @Auth(permission = PermissionEnum.UPDATE_LEARNING_CONTENT)
    public void reorderActivity(Long resourceId, ReorderActivityDto dto) {

        if (classRepository.countCreatedOngoingClassByCourse(resourceId) > 0) {
            throw new DragAndDropIsNotAllowedException();
        }

        var actionId = dto.getFrom().getActivityId();
        var fromSortIndex = dto.getFrom().getOrder();
        var toSortIndex = dto.getTo().getOrder();
        var toChapterId = dto.getTo().getChapterId();
        var fromChapterId = dto.getFrom().getChapterId();
        var toChapter = chapterCourseRepository.findFetchActionsById(toChapterId)
                .orElseThrow(() -> new ChapterNotFoundException(toChapterId));
        var fromAction = chapterActivityCourseRepository.findActionByIdSortIndexAndChapter(actionId, fromChapterId, fromSortIndex)
                .orElseThrow(() -> new ChapterActionNotFoundException(actionId));
        if (toSortIndex == null) {
            chapterActivityCourseRepository.deleteById(fromAction.getId());
            entityManager.flush();
            var maxSortIndexInChapter = 1;
            var newAction = createNewAction(actionId, fromAction);
            newAction.setOrder(maxSortIndexInChapter);
            newAction.setChapter(toChapter);
            entityManager.flush();
            return;
        }

        fromAction.setOrder(-1);
        entityManager.flush();
        var actions = chapterActivityCourseRepository.findActionByChapterAndHigherOrderExcept(toChapterId, toSortIndex);
        var newAction = fromAction;
        if (!Objects.equals(toChapterId, fromChapterId)) {
            chapterActivityCourseRepository.deleteById(fromAction.getId());
            entityManager.flush();
            newAction = createNewAction(actionId, fromAction);
            newAction.setChapter(toChapter);
            newAction.setOrder(-1);
            entityManager.flush();
        }

        if (toSortIndex.equals(AppConstant.TOP)) {
            actions.forEach(action -> action.setOrder(action.getOrder() + 1));
            newAction.setOrder(1);
        } else if (toSortIndex.equals(AppConstant.BOTTOM)) {
            var max = chapterActivityCourseRepository.findMaxSortIndexByChapter(toChapterId);
            newAction.setOrder(max + 1);
        } else {
            actions.forEach(action -> action.setOrder(action.getOrder() + 1));
            entityManager.flush();
            newAction.setOrder(toSortIndex);
        }
        classService.cascadeReorderActivity(resourceId, dto);
    }

    private ChapterActivityCourseEntity createNewAction(Long actionId, ChapterActivityCourseEntity fromAction) {
        var newAction = new ChapterActivityCourseEntity();
        newAction.setId(new ChapterActivityCourseKey());
        newAction.getId().setActivityId(actionId);
        newAction.getId().setActivityType(fromAction.getId().getActivityType());
        return newAction;
    }

    public CourseEntity findFetchClassById(Long courseId) {
        return courseRepository.findFetchClassById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
    }

    @Transactional
    // TODO: add auth
    public void addTextbooksToCourse(Long courseId, AddTextbookToCourseRequest dto) {
        courseTextbookRepository.saveAll(dto.getIdList()
                .stream()
                .filter(id -> !courseTextbookRepository.existsById(new CourseTextbookKey(courseId, id)))
                .map(id -> {
                    var entity = new CourseTextbookEntity();
                    entity.setCourse(entityManager.getReference(CourseEntity.class, courseId));
                    entity.setTextbook(entityManager.getReference(TextbookEntity.class, id));
                    return entity;
                })
                .collect(toList()));
        classService.cascadeAddTextbooks(courseId, dto);
    }

    @Transactional(readOnly = true)
    // TODO: add auth
    @Cacheable("textbook_not_in_course")
    public List<TextbookDto> getTextbooksNotInCourse(Long courseId, String keyword) {
        return textbookRepository.findNotInCourse(courseId, StringUtils.isNotBlank(keyword) ? keyword.toLowerCase() : "")
                .stream()
                .map(textBookMapper::mapToTextBookDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    // TODO: add auth
    @Cacheable("textbook_in_course")
    public List<TextbookDto> getTextbooksInCourse(Long courseId, String keyword) {
        return textbookRepository.findInCourse(courseId, StringUtils.isNotBlank(keyword) ? keyword.toLowerCase() : "")
                .stream()
                .map(textBookMapper::mapToTextBookDto)
                .collect(toList());
    }

    @Transactional
    public void deleteTextbookFromCourse(Long courseId, Long textbookId) {
        if (!courseTextbookRepository.existsById(new CourseTextbookKey(courseId, textbookId))) {
            return;
        }
        courseTextbookRepository.deleteById(new CourseTextbookKey(courseId, textbookId));
        classService.cascadeDeleteTextbook(courseId, textbookId);
        entityManager.createQuery("DELETE FROM UnitCourseTextBookEntity u " +
                        "WHERE u.textbook.id = (:textbookId) " +
                        "AND u.unit.id IN (SELECT c.id FROM UnitCourseEntity c where c.course.id = (:courseId))")
                .setParameter("textbookId", textbookId)
                .setParameter("courseId", courseId)
                .executeUpdate();
    }

    @Transactional(readOnly = true)
    public Long countTextbookOfCourse(Long courseId) {
        return courseTextbookRepository.countTextbook(courseId);
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_LIST_CLASS)
    @Cacheable("classes_in_course")
    public ClassPagedDto getClasses(Long courseId, BaseSearchCriteria criteria) {
        return classService.getClassesByCourse(courseId, criteria);
    }

    @Transactional(readOnly = true)
//    @Auth(permission = PermissionEnum.VIEW_LIST_UNIT_CLASS)
    @Cacheable("quizzes_by_course")
    public List<QuizDto> getQuizzesByCourse(Long resourceId) {
        var quizzes = quizCourseRepository.findByCourseId(resourceId);
        return quizzes
                .stream()
                .map(quizMapper::mapToQuizDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
//    @Auth(permission = PermissionEnum.VIEW_LIST_UNIT_CLASS)
    @Cacheable("unit_by_course")
    public List<UnitDto> getUnitsByCourse(Long resourceId) {
        var units = unitCourseRepository.findFetchByCourseId(resourceId);
        return units
                .stream()
                .map(unitMapper::mapToUnitDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_LEARNING_CONTENT)
    @Cacheable("unit_groups_by_textbook_in_course")
    public List<UnitByTextbookDto> getUnitsGroupByTextbook(Long resourceId) {
        var units = unitCourseRepository.findFetchByCourseId(resourceId);
        var textbooks = getTextbooksInCourse(resourceId, StringUtils.EMPTY);
        var unitByTextbook = textbooks.stream()
                .map(textbook -> {
                    var dto = new UnitByTextbookDto();
                    dto.setTextbookId(textbook.getId());
                    dto.setName(textbook.getName());
                    dto.setUnits(units.stream()
                            .filter(unit -> unit.getTextbooks()
                                    .stream()
                                    .anyMatch(t -> Objects.equals(t.getTextbook().getId(), textbook.getId())))
                            .map(unitMapper::mapToUnitDto)
                            .collect(toList()));
                    return dto;
                })
                .collect(toList());

        var notInTextbooks = units.stream()
                .filter(t -> CollectionUtils.isEmpty(t.getTextbooks()))
                .map(unitMapper::mapToUnitDto)
                .collect(toList());
        var result = new ArrayList<UnitByTextbookDto>();
        result.addAll(unitByTextbook);

        if (CollectionUtils.isEmpty(notInTextbooks)) {
            return result;
        }

        var chapterTextbookDto = new UnitByTextbookDto();
        chapterTextbookDto.setTextbookId(-1L);
        chapterTextbookDto.setName("Không thuộc giáo trình nào");
        chapterTextbookDto.setUnits(notInTextbooks);
        result.add(chapterTextbookDto);
        return result;
    }
}
