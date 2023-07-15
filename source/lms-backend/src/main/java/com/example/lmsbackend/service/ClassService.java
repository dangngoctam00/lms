package com.example.lmsbackend.service;

import com.example.lmsbackend.config.security.CustomUserDetails;
import com.example.lmsbackend.config.security.aop.Auth;
import com.example.lmsbackend.criteria.BaseSearchCriteria;
import com.example.lmsbackend.domain.StaffEntity;
import com.example.lmsbackend.domain.UserEntity;
import com.example.lmsbackend.domain.classmodel.*;
import com.example.lmsbackend.domain.compositekey.UnitTextBookKey;
import com.example.lmsbackend.domain.coursemodel.*;
import com.example.lmsbackend.domain.event.*;
import com.example.lmsbackend.domain.exam.GradeTag;
import com.example.lmsbackend.domain.exam.GradeTagScope;
import com.example.lmsbackend.domain.exam.GradeTagStudentEntity;
import com.example.lmsbackend.domain.exam.QuizConfigEntity;
import com.example.lmsbackend.domain.resource.TextbookEntity;
import com.example.lmsbackend.domain.scheduler.ClassSessionEntity;
import com.example.lmsbackend.dto.classes.*;
import com.example.lmsbackend.dto.exam.QuizConfigDto;
import com.example.lmsbackend.dto.exam.QuizEntryDto;
import com.example.lmsbackend.dto.notification.CreateAnnouncementRequest;
import com.example.lmsbackend.dto.request.course.ReorderActivityDto;
import com.example.lmsbackend.dto.request.course.ReorderChapterDto;
import com.example.lmsbackend.dto.resource.TextbookDto;
import com.example.lmsbackend.dto.response.course.*;
import com.example.lmsbackend.dto.staff.StaffDTO;
import com.example.lmsbackend.dto.textbook.AddTextbookToCourseRequest;
import com.example.lmsbackend.enums.*;
import com.example.lmsbackend.exceptions.InvalidDataException;
import com.example.lmsbackend.exceptions.UnauthorizedException;
import com.example.lmsbackend.exceptions.aclass.ClassNotFoundException;
import com.example.lmsbackend.exceptions.aclass.*;
import com.example.lmsbackend.exceptions.course.ChapterActionNotFoundException;
import com.example.lmsbackend.exceptions.course.ChapterNotFoundException;
import com.example.lmsbackend.exceptions.course.QuizNotFoundException;
import com.example.lmsbackend.exceptions.exam.*;
import com.example.lmsbackend.exceptions.staff.StaffNotFoundException;
import com.example.lmsbackend.exceptions.student.StudentNotFoundException;
import com.example.lmsbackend.mapper.*;
import com.example.lmsbackend.mapper.exam.QuizConfigMapper;
import com.example.lmsbackend.repository.*;
import com.example.lmsbackend.service.announcement.AnnouncementService;
import com.example.lmsbackend.service.announcement.ClassAnnouncementOperation;
import com.example.lmsbackend.utils.MailUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static com.example.lmsbackend.constant.AppConstant.*;
import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassService {

    private final ClassRepository classRepository;
    private final ClassSessionRepository classSessionRepository;
    private final CourseService courseService;
    private final UnitClassRepository unitClassRepository;
    private final ClassMapper classMapper;
    private final ChapterMapper chapterMapper;
    private final QuizMapper quizMapper;
    private final UnitMapper unitMapper;
    private final TextbookRepository textbookRepository;
    private final EventRepository eventRepository;

    private final QuizClassRepository quizClassRepository;
    private final ChapterClassRepository chapterClassRepository;
    private final ChapterActivityClassRepository chapterActivityClassRepository;
    private final UtilsService utilsService;
    private final ExamRepository examRepository;
    private final VotingRepository votingRepository;
    private final ClassLearningContentRepository classLearningContentRepository;
    private final GradeTagRepository gradeTagRepository;
    private final QuizConfigRepository quizConfigRepository;
    private final QuizConfigMapper quizConfigMapper;
    private final QuizSessionRepository quizSessionRepository;
    private final UserService userService;
    private final MailUtils mailUtils;
    private final ClassAnnouncementOperation classAnnouncementOperation;
    private final AttendanceService attendanceService;
    private final ClassTeacherRepository classTeacherRepository;
    private final ClassStudentRepository classStudentRepository;
    private final StaffRepository staffRepository;
    private final StudentRepository studentRepository;
    private final StaffMapper staffMapper;
    private final EventService eventService;
    private final QuestionRepository questionRepository;

    private final GradeTagStudentRepository gradeTagStudentRepository;
    private final ClassTextbookRepository classTextbookRepository;
    private final TextBookMapper textBookMapper;
    private final ChapterActivityCourseRepository chapterActivityCourseRepository;
    private final AnnouncementService announcementService;

    @PersistenceContext
    private final EntityManager entityManager;


    @Transactional
    @Auth(permission = PermissionEnum.CREATE_CLASS)
    public ClassDto createClass(Long resourceId, CreateClassRequestDto dto) {
        validateClassCode(dto);

        var entity = classMapper.mapToClassEntity(dto);

        addClassToCourse(entity, dto.getCourseId());

        entity = classRepository.save(entity);
        var calendar = eventService.createCalendar(CalendarType.CLASS, entity.getId());
        createLearningContent(entity, entity.getCourse().getContent());
        createAttendanceGradeTag(entity.getId());
        var response = classMapper.mapToClassDto(entity);
        response.setCalendarId(calendar.getId().toString());

        entityManager.flush();

        var textbooks = textbookRepository.findByCourse(dto.getCourseId());
        ClassEntity finalEntity = entity;
        classTextbookRepository.saveAll(textbooks.stream()
                .map(textbook -> {
                    var t = new ClassTextbookEntity();
                    t.setClassEntity(finalEntity);
                    t.setTextbook(textbook);
                    return t;
                })
                .collect(toList()));
        return response;
    }

    private void createAttendanceGradeTag(Long classId) {
        var tag = new GradeTag();
        tag.setTitle(ATTENDANCE_TAG);
        tag.setScope(GradeTagScope.CLASS);
        tag.setScopeId(classId);
        tag.setPrimitive(true);
        gradeTagRepository.save(tag);
    }

    private void addClassToCourse(ClassEntity entity, Long courseId) {
        var course = courseService.findFetchClassById(courseId);
        entity.setCourse(course);
    }

    private void validateClassCode(CreateClassRequestDto dto) {
        var isExists = classRepository.existsByCode(dto.getCode());
        if (isExists) {
            log.warn("Class code {} is already exists", dto.getCode());
            throw new ClassCodeAlreadyExistException(dto.getCode());
        }
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_LIST_CLASS)
    @Cacheable("classes")
    public ClassPagedDto getClasses(BaseSearchCriteria criteria) {
        var classes = classRepository.getClasses(criteria);
        var classesDto = new ClassPagedDto();
        MapperUtils.mapPagedDto(classesDto, classes);
        classesDto.setListData(
                classes.stream()
                        .map(classMapper::mapToClassDto)
                        .collect(toList())
        );
        return classesDto;
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_LIST_CLASS)
    @Cacheable("class_in_course")
    public ClassPagedDto getClassesByCourse(Long courseId, BaseSearchCriteria criteria) {
        var classes = classRepository.getClassesByCourse(courseId, criteria);
        var classesDto = new ClassPagedDto();
        MapperUtils.mapPagedDto(classesDto, classes);
        classesDto.setListData(
                classes.stream()
                        .map(classMapper::mapToClassDto)
                        .collect(toList())
        );
        return classesDto;
    }

    public void cascadeAddChapter(ChapterCourseEntity chapterCourse, Long courseId) {
        var ongoingClass = classRepository.findCreatedOrOngoingIdClassByCourse(courseId);
        var classesLearningContentList = classLearningContentRepository.findFetchChaptersByClassIn(ongoingClass);

        classesLearningContentList.forEach(learningContent -> {
            var currentMaxSortIndex = chapterClassRepository.findMaxCurrentSortIndex(learningContent.getId()).orElse(0) + 1;
            var chapter = chapterMapper.mapToChapterClass(chapterCourse);
            chapter.setOrder(currentMaxSortIndex);
            chapter.setLearningContent(learningContent);
            chapter.setChapterCourse(chapterCourse);
        });
    }

    public void cascadeAddUnit(UnitCourseEntity unitCourse, Long chapterCourseId, Long courseId) {
        var ongoingClass = classRepository.findCreatedOrOngoingIdClassByCourse(courseId);
        var chaptersClass = chapterClassRepository.findByChapterCourseAndClassIn(chapterCourseId, ongoingClass);
        chaptersClass.forEach(chapterClass -> {
            var unitClass = mapUnit(unitCourse);
            unitClass.setClassEntity(chapterClass.getLearningContent().getClassEntity());
            unitClass.setUnitCourse(unitCourse);
            unitClassRepository.save(unitClass);
            prepareSortIndex(chapterClass, null, unitClass);
        });
    }

    public void cascadeAddQuiz(QuizCourseEntity quizCourse, Long chapterCourseId, Long courseId) {
        var ongoingClass = classRepository.findCreatedOrOngoingIdClassByCourse(courseId);
        var chaptersClass = chapterClassRepository.findByChapterCourseAndClassIn(chapterCourseId, ongoingClass);
        chaptersClass.forEach(chapterClass -> {
            var quizClass = quizMapper.mapToQuizClass(quizCourse);
            if (quizCourse.getExam() != null) {
                quizClass.setExam(quizCourse.getExam());
            }
            quizClass.setClassEntity(chapterClass.getLearningContent().getClassEntity());
            quizClass.setQuizCourse(quizCourse);
            var quizCourseTag = quizCourse.getTag();
            quizClass.setTag(quizCourseTag);
            var config = getDefaultConfig();
            config.setQuiz(quizClass);
            quizClass = quizClassRepository.save(quizClass);
            prepareSortIndex(chapterClass, quizClass, null);
            entityManager.flush();
            var students = studentRepository.findStudentByClass(chapterClass.getLearningContent().getId());
            gradeTagStudentRepository.saveAll(students.stream()
                    .map(s -> new GradeTagStudentEntity(quizCourseTag, s, 0D))
                    .collect(toList()));
        });
    }

    private void prepareSortIndex(ChapterClassEntity chapterClass, QuizClassEntity quizClass, UnitClassEntity unitClass) {
        var quizIdList = chapterClass.getActions()
                .stream()
                .filter(x -> StringUtils.equals(x.getId().getActivityType(), ActionType.QUIZ.name()))
                .map(x -> x.getId().getActivityId())
                .collect(toList());
        var unitIdList = chapterClass.getActions()
                .stream()
                .filter(x -> StringUtils.equals(x.getId().getActivityType(), ActionType.UNIT.name()))
                .map(x -> x.getId().getActivityId())
                .collect(toList());
        var votingIdList = chapterClass.getActions()
                .stream()
                .filter(x -> StringUtils.equals(x.getId().getActivityType(), ActionType.QUIZ.name()))
                .map(x -> x.getId().getActivityId())
                .collect(toList());
        var quizIdClass = quizClassRepository.getQuizClassIdNotInCourseIn(quizIdList);
        var unitIdClass = unitClassRepository.getUnitClassIdNotInCourseIn(unitIdList);
        if (quizClass != null) {
            buildChapterSortIndex(chapterClass, quizClass.getId(), ActionType.QUIZ.name(), unitIdClass, quizIdClass, votingIdList, chapterClass.getActions());
        } else {
            buildChapterSortIndex(chapterClass, unitClass.getId(), ActionType.UNIT.name(), unitIdClass, quizIdClass, votingIdList, chapterClass.getActions());
        }
    }

    private void createLearningContent(ClassEntity entity, CourseLearningContentEntity courseLearningContent) {
        var classLearningContent = new ClassLearningContentEntity();
        var courseChapters = new ArrayList<>(courseLearningContent.getChapters());

        var units = persistUnit(entity.getCourse().getId(), entity);
        var quizzes = persistQuiz(entity.getCourse().getId(), entity);

        entityManager.flush();

        for (ChapterCourseEntity courseChapter : courseChapters) {
            var chapter = chapterMapper.mapToChapterClass(courseChapter);
            chapter.setLearningContent(classLearningContent);
            chapter.setChapterCourse(courseChapter);
            courseChapter.getActions().forEach(courseAction -> {
                var action = new ChapterActivityClassEntity();
                action.setId(new ChapterActivityClassKey());
                action.setChapter(chapter);
                action.setOrder(chapter.getOrder());
                var id = courseAction.getId();
                if (StringUtils.equals(id.getActivityType(), ActionType.UNIT.name())) {
                    var unit = units.stream()
                            .filter(u -> Objects.equals(u.getUnitCourse().getId(), id.getActivityId()))
                            .findAny()
                            .get();
                    action.getId().setActivityId(unit.getId());
                    action.getId().setActivityType(ActionType.UNIT.name());
                } else if (StringUtils.equals(id.getActivityType(), ActionType.QUIZ.name())) {
                    var quiz = quizzes.stream()
                            .filter(u -> Objects.equals(u.getQuizCourse().getId(), id.getActivityId()))
                            .findAny()
                            .get();
                    action.getId().setActivityType(ActionType.QUIZ.name());
                    action.getId().setActivityId(quiz.getId());
                }
            });
        }

        classLearningContent.setClassEntity(entity);
        classLearningContentRepository.save(classLearningContent);
    }

    private List<QuizClassEntity> persistQuiz(Long courseId, ClassEntity classEntity) {
        var quizzes = courseService.findQuizzesInCourse(courseId);

        return quizClassRepository.saveAll(quizzes.stream()
                .map(quizCourse -> {
                    var quizClass = quizMapper.mapToQuizClass(quizCourse);
                    if (quizCourse.getExam() != null) {
                        quizClass.setExam(quizCourse.getExam());
                    }
                    quizClass.setQuizCourse(quizCourse);
                    quizClass.setClassEntity(classEntity);
                    var quizCourseTag = quizCourse.getTag();
                    quizClass.setTag(quizCourseTag);
                    var config = getDefaultConfig();
                    config.setQuiz(quizClass);
                    return quizClass;
                })
                .collect(toList()));
    }

    private List<UnitClassEntity> persistUnit(Long courseId, ClassEntity classEntity) {
        var units = courseService.findUnitsInCourse(courseId);
        return unitClassRepository.saveAll(units.stream()
                .map(unit -> {
                    var unitClass = mapUnit(unit);
                    unitClass.setUnitCourse(unit);
                    unitClass.setClassEntity(classEntity);
                    return unitClass;
                })
                .collect(toList()));
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_CLASS)
    @Cacheable("class")
    public ClassDto findClass(Long resourceId) {
        return classRepository.findClassInformationById(resourceId)
                .map(classMapper::mapToClassDto)
                .orElseThrow(() -> new ClassNotFoundException(resourceId));
    }

    @Transactional
    @Auth(permission = PermissionEnum.UPDATE_CLASS)
    public ClassDto updateClass(Long resourceId, UpdateClassRequestDto dto) {
        var aClass = classRepository.findClassInformationById(resourceId)
                .orElseThrow(() -> new ClassNotFoundException(resourceId));
        validateUpdateClass(aClass, dto);
        classMapper.mapUpdateClassInformation(aClass, dto);
        classRepository.save(aClass);
        return classMapper.mapToClassDto(aClass);
    }

    private void validateUpdateClass(ClassEntity aClass, UpdateClassRequestDto dto) {
//        if (aClass.getStatus() == ClassStatus.ENDED
//                && !StringUtils.equals(dto.getStatus(), ClassStatus.ENDED.name())
//                && (dto.getEndedAt() != null && (dto.getEndedAt().isBefore(LocalDate.now()) || dto.getEndedAt().isEqual(LocalDate.now())))) {
//            throw new InvalidDataException("To change status of class from 'ENDED' to another, the end date must be left empty or after current date");
//        }
        if (StringUtils.equals(dto.getStatus(), ClassStatus.ENDED.name()) && (dto.getEndedAt() == null || dto.getEndedAt().isAfter(LocalDate.now()))) {
            log.warn("To change status of class to 'ENDED', the end date must be current date or before");
            throw new InvalidDataException("Để kết thúc lớp học, ngày kết thúc phải là ngày hôm nay hoặc trước đó");
        }
    }

    @Transactional
    @Auth(permission = PermissionEnum.ADD_STUDENT)
    public List<MemberDto> addStudents(Long resourceId, List<Long> idList) {
        if (!classRepository.existsById(resourceId)) {
            throw new ClassNotFoundException(resourceId);
        }
        return idList.stream()
                .map(id -> addStudent(resourceId, id))
                .collect(toList());
    }

    @Transactional
    @Auth(permission = PermissionEnum.ADD_TEACHER)
    public List<MemberDto> addTeachers(Long resourceId, AddTeacherRequestDto dto) {
        if (!classRepository.existsById(resourceId)) {
            throw new ClassNotFoundException(resourceId);
        }
        return dto.getIdList().stream()
                .map(teacher -> addTeacher(resourceId, teacher.getId(), teacher.getRole()))
                .collect(toList());
    }

    private MemberDto addTeacher(Long classId, Long teacherId, String role) {
        var staff = staffRepository.findById(teacherId)
                .orElseThrow(StaffNotFoundException::new);
        if (classTeacherRepository.existsById(new ClassTeacherKey(teacherId, classId))) {
            throw new TeacherHasAlreadyInClassException(classId, teacherId);
        }
        var entity = new ClassTeacherEntity();
        entity.setClassEntity(entityManager.getReference(ClassEntity.class, classId));
        entity.setTeacher(staff);
        entity.setRole(TeacherRole.valueOf(role));
        classTeacherRepository.save(entity);
        return classMapper.mapToMemberDto(entity);
    }

    private MemberDto addStudent(Long classId, Long studentId) {
        var student = studentRepository.findById(studentId)
                .orElseThrow(StudentNotFoundException::new);
        if (classStudentRepository.existsById(new ClassStudentKey(studentId, classId))) {
            throw new StudentHasAlreadyInClassException(classId, studentId);
        }
        var entity = new ClassStudentEntity();
        entity.setClassEntity(entityManager.getReference(ClassEntity.class, classId));
        entity.setStudent(student);
        classStudentRepository.save(entity);

        ClassEntity classEntity = classRepository.getById(classId);

        List<GradeTag> gradeTagsInClass = gradeTagRepository.findAllByScopeAndScopeId(GradeTagScope.CLASS, classId);
        List<GradeTag> gradeTagsInCourse = gradeTagRepository.findAllByScopeAndScopeId(GradeTagScope.COURSE, classEntity.getCourse().getId());
//        var tagsId = List.of(gradeTagsInClass, gradeTagsInCourse)
//                .stream()
//                .flatMap(List::stream)
//                .map(GradeTag::getId)
//                .collect(toList());
//        var countByTag = gradeTagStudentRepository.countByTag(tagsId);
        for (GradeTag gradeTag : gradeTagsInClass) {
//            var opt = countByTag.stream()
//                    .filter(x -> Objects.equals(x.getTagId(), gradeTag.getId()))
//                    .findAny();
//            if (opt.isPresent() && opt.get().getCount() > 0) {
                gradeTagStudentRepository.save(new GradeTagStudentEntity(gradeTag, student,  0D));
//            }
        }

        for (GradeTag gradeTag : gradeTagsInCourse) {
//            var opt = countByTag.stream()
//                    .filter(x -> Objects.equals(x.getTagId(), gradeTag.getId()))
//                    .findAny();
//            if (opt.isPresent() && opt.get().getCount() > 0) {
                gradeTagStudentRepository.save(new GradeTagStudentEntity(gradeTag, student, 0D));
//            }
        }

        attendanceService.createAttendanceForStudent(studentId, classId);

        return classMapper.mapToMemberDto(student);
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_LIST_STUDENT_IN_CLASS)
    public MemberPagedDto getStudents(Long resourceId, BaseSearchCriteria criteria) {
        var members = classStudentRepository.getStudents(resourceId, criteria);
        var membersPagedDto = new MemberPagedDto();
        MapperUtils.mapPagedDto(membersPagedDto, members);
        membersPagedDto.setListData(members);
        return membersPagedDto;
    }

    @Transactional(readOnly = true)
    public MemberPagedDto getMembers(Long classId, BaseSearchCriteria criteria) {
        var members = classRepository.getMembers(classId, criteria);
        var membersDto = members
                .stream()
                .map(this::mapToMember)
                .collect(toList());
        var membersPagedDto = new MemberPagedDto();
        membersPagedDto.setListData(membersDto);
        return membersPagedDto;
    }

    private MemberDto mapToMember(UserEntity entity) {
        var dto = new MemberDto();
        dto.setUserId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        return dto;
    }

    @Transactional
    @Auth(permission = PermissionEnum.CREATE_CLASS_SESSION)
    public ClassSessionRequestDto createSession(Long resourceId, ClassSessionRequestDto dto) {
        var session = classMapper.mapToClassSession(dto);
        session.setIsScheduled(false);

        validateClassSession(dto);

        var classEntity = classRepository.findFetchSessionsById(resourceId)
                .orElseThrow(() -> new ClassNotFoundException(resourceId));

        setTeacher(resourceId, dto, session);

        setUnit(dto, session);

        // DELAY set class to avoid Hibernate flush and generate error on null-constraint
        session.setClassEntity(classEntity);

        attendanceService.createOfficialAttendanceSessionForSession(resourceId, session);

        session = classSessionRepository.save(session);

        eventService.createClassSessionEvent(session);

        applyAllSessionsForTeacherAndRoom(resourceId, session.getId(), dto);

        if (BooleanUtils.isTrue(dto.getNotifyWithEmail())) {
            var announcement = buildAnnouncement(session, "Một buổi học mới đã được thêm vào lịch học");
            announcementService.createAnnouncement(resourceId, announcement);
            notifyUserByEmail(classEntity, session, "Một buổi học mới đã được thêm vào lịch học");
        }

        dto.setId(session.getId());
        return dto;
    }

    private CreateAnnouncementRequest buildAnnouncement(ClassSessionEntity session, String title) {
        var announcement = new CreateAnnouncementRequest();
        var currentUser = userService.getCurrentUser();
        announcement.setSenderId(currentUser.getId());
        announcement.setSendMailAsCopy(false);
        announcement.setTags("");
        announcement.setSubject(title);
        announcement.setReceiversId(List.of());
        var dateTimeFormatter = getDateTimeFormatter();
        announcement.setContent(String.format("<p><b>Thời gian bắt đầu:</b> %s</p>\n" +
                "<p><b>Thời gian kết thúc:</b> %s</p>\n" +
                "<p><b>Phòng:</b> %s</p>\n" +
                "<p><b>Giáo viên:</b> %s</p>\n" +
                "<p><b>Bài học:</b> %s</p>\n" +
                "<p><b>Ghi chú:</b> %s</p>", dateTimeFormatter.format(Timestamp.valueOf(session.getStartedAt())),
                dateTimeFormatter.format(Timestamp.valueOf(session.getFinishedAt())),
                session.getRoom() != null ? session.getRoom() : "",
                session.getTeacher() != null ? String.format("%s %s", session.getTeacher().getLastName(), session.getTeacher().getFirstName()) : "",
                session.getUnit() != null ? session.getUnit().getTitle() : "",
                session.getNote() != null ? session.getNote() : ""));
        return announcement;
    }

    private void notifyUserByEmail(ClassEntity classEntity, ClassSessionEntity session, String title) {
        var receivers = classAnnouncementOperation.getReceiversEmail(classEntity.getId());
        var tenantName = utilsService.getTenantName();
        var template = buildEmailTemplate(title, session);
        receivers.forEach(receiver -> mailUtils.sendMailWithTemplate(tenantName,
                receiver,
                String.format("Thông báo mới từ lớp %s", classEntity.getName()),
                template,
                NEW_SESSIONS_TEMPLATE_FILE));
    }

    private HashMap<String, Object> buildEmailTemplate(String title, ClassSessionEntity session) {
        var map = new HashMap<String, Object>();
        var teacherName = "";
        if (session.getTeacher() != null) {
            teacherName = String.format("%s %s", session.getTeacher().getLastName(), session.getTeacher().getFirstName());
            map.put("teacher", teacherName);
        }
        var dateTimeFormatter = getDateTimeFormatter();
        map.put("startedAt", dateTimeFormatter.format(java.sql.Timestamp.valueOf(session.getStartedAt())));
        if (session.getFinishedAt() != null) {
            map.put("endedAt", dateTimeFormatter.format(java.sql.Timestamp.valueOf(session.getFinishedAt())));
        }
        map.put("room", session.getRoom() == null ? "" : session.getRoom());
        map.put("title", title);
        map.put("note", session.getNote() == null ? "" : session.getNote());
        if (session.getUnit() != null) {
            map.put("unit", session.getUnit().getTitle());
        } else {
            map.put("unit", "");
        }
        return map;
    }

    @Transactional
    @Auth(permission = PermissionEnum.UPDATE_CLASS_SESSION)
    public ClassSessionRequestDto updateSession(Long classId, Long sessionId, ClassSessionRequestDto dto) {
        validateClassSession(dto);
        var session = classSessionRepository.findFetchTeacherUnitClass(sessionId)
                .orElseThrow(() -> new ClassSessionNotFoundException(sessionId));
        classMapper.mapToClassSession(session, dto);

        updateTeacher(classId, dto, session);
        updateUnit(dto, session);
        classSessionRepository.save(session);
        eventService.updateClassSessionEvent(session);

        if ((BooleanUtils.isTrue(dto.getApplyTeacherToAll()) && dto.getTeacherId() != null)
                || (BooleanUtils.isTrue(dto.getApplyRoomToAll()) && StringUtils.isNotBlank(dto.getRoom()))) {
            applyAllSessionsForTeacherAndRoom(classId, sessionId, dto);
        }

        if (BooleanUtils.isTrue(dto.getNotifyWithEmail())) {
            var announcement = buildAnnouncement(session, "Một buổi học đã được cập nhật");
            announcementService.createAnnouncement(classId, announcement);
            notifyUserByEmail(session.getClassEntity(), session, "Một buổi học đã được cập nhật");
        }

        return dto;
    }

    private void applyAllSessionsForTeacherAndRoom(Long classId, Long sessionId, ClassSessionRequestDto dto) {
        List<ClassSessionEntity> classSessionEntities = classSessionRepository.findSessionsByClassExcept(classId, sessionId);
        StaffEntity teacher = null;
        if (BooleanUtils.isTrue(dto.getApplyTeacherToAll()) && dto.getTeacherId() != null) {
            teacher = classTeacherRepository.findById(new ClassTeacherKey(dto.getTeacherId(), classId))
                    .orElseThrow(() -> new TeacherNotFoundInClassException(classId, dto.getTeacherId())).getTeacher();
        }

        StaffEntity finalTeacher = teacher;
        classSessionEntities.forEach(s -> {
            if (BooleanUtils.isTrue(dto.getApplyTeacherToAll()) && dto.getTeacherId() != null) {
                s.setTeacher(finalTeacher);
            }
            if (BooleanUtils.isTrue(dto.getApplyRoomToAll()) && StringUtils.isNotBlank(dto.getRoom())) {
                s.setRoom(dto.getRoom());
            }
            eventService.updateClassSessionEvent(s);
        });
    }

    private void setUnit(ClassSessionRequestDto dto, ClassSessionEntity session) {
        if (dto.getUnitId() != null) {
            var unit = unitClassRepository.findById(dto.getUnitId())
                    .orElseThrow(() -> new UnitNotFoundException(dto.getUnitId()));
            session.setUnit(unit);
        }
    }

    private void setTeacher(Long classId, ClassSessionRequestDto dto, ClassSessionEntity session) {
        if (dto.getTeacherId() != null) {
            var teacher = classTeacherRepository.findById(new ClassTeacherKey(dto.getTeacherId(), classId))
                    .orElseThrow(() -> new TeacherNotFoundInClassException(classId, dto.getTeacherId()));
            session.setTeacher(teacher.getTeacher());
        }
    }

    private EventEntity createClassSessionEvent(ClassSessionEntity session, CalendarEntity calendar) {
        var event = new EventEntity();
        event.setSummary(session.getClassEntity().getName());
        event.setDescription(String.format(CLASS_SESSION_DESCRIPTION_TEMPLATE, "__", "__"));
        event.getId().setEventId(session.getId());
        event.getId().setEventType(EventType.CLASS_SESSION);
        event.setCalendar(calendar);
        event.setStartedAt(session.getStartedAt());
        event.setEndedAt(session.getFinishedAt());
        event.setHidden(false);
        return event;
    }

    private void validateClassSession(ClassSessionRequestDto dto) {
        if (dto.getFinishedAt() != null && dto.getFinishedAt().isBefore(dto.getStartedAt())) {
            throw new FinishedTimeNotValidException(dto.getFinishedAt());
        }
    }

    private void updateTeacher(Long classId, ClassSessionRequestDto dto, ClassSessionEntity session) {
        if (dto.getTeacherId() != null) {
            var teacher = classTeacherRepository.findById(new ClassTeacherKey(dto.getTeacherId(), classId))
                    .orElseThrow(() -> new TeacherNotFoundInClassException(classId, dto.getTeacherId()));
            session.setTeacher(teacher.getTeacher());
        } else {
            session.setTeacher(null);
        }
    }

    private void updateUnit(ClassSessionRequestDto dto, ClassSessionEntity session) {
        if (dto.getUnitId() != null) {
            var unit = unitClassRepository.findFetchSessionsById(dto.getUnitId())
                    .orElseThrow(() -> new UnitNotFoundException(dto.getUnitId()));
            session.removeUnit(unit);
            session.setUnit(unit);
        }
    }

    private UnitClassEntity mapUnit(UnitCourseEntity unitCourse) {
        var unit = unitMapper.mapToUnitClass(unitCourse);
        if (CollectionUtils.isNotEmpty(unitCourse.getTextbooks())) {
            var textbooksId = unitCourse.getTextbooks()
                    .stream()
                    .map(UnitCourseTextBookEntity::getId)
                    .map(u -> u.getTextbookId())
                    .collect(toSet());
            var textbooks = textbookRepository.findByIdIn(textbooksId);
            var textBookUnit = textbooks
                    .stream()
                    .map(textbook -> {
                        var unitTextBook = new UnitClassTextBookEntity();
                        unitTextBook.setUnit(unit);
                        unitTextBook.setTextbook(textbook);
                        var textbookInUnit = unitCourse.getTextbooks()
                                .stream()
                                .filter(t -> t.getId().getTextbookId() == textbook.getId())
                                .findAny()
                                .orElseThrow(() -> new RuntimeException());
                        unitTextBook.setNote(textbookInUnit.getNote());
                        return unitTextBook;
                    })
                    .collect(toSet());
            unit.setTextbooks(textBookUnit);
        }
        return unit;
    }

    private UnitClassEntity mapUnit(UnitDto dto) {
        var unit = unitMapper.mapToUnitClassEntity(dto);
        if (CollectionUtils.isNotEmpty(dto.getTextbooks())) {
            var textbooksId = dto.getTextbooks()
                    .stream()
                    .map(UnitDto.TextbookInUnit::getTextbookId)
                    .collect(toSet());
            setTextbook(dto, unit, textbooksId);
        }
        return unit;
    }

    private void setTextbook(UnitCourseEntity unitCourse, UnitClassEntity unit, Set<Long> textbooksId) {
        var textbooks = textbookRepository.findByIdIn(textbooksId);
        var textBookUnit = textbooks
                .stream()
                .map(textbook -> {
                    var unitTextBook = new UnitClassTextBookEntity();
                    unitTextBook.setUnit(unit);
                    unitTextBook.setTextbook(textbook);
                    var textbookInUnit = unitCourse.getTextbooks()
                            .stream()
                            .filter(t -> t.getTextbook().getId() == textbook.getId())
                            .findAny()
                            .orElseThrow(() -> new RuntimeException());
                    unitTextBook.setNote(textbookInUnit.getNote());
                    return unitTextBook;
                })
                .collect(toSet());
        unit.setTextbooks(textBookUnit);
    }

    private void setTextbook(UnitDto dto, UnitClassEntity unit, Set<Long> textbooksId) {
        var textbooks = textbookRepository.findByIdIn(textbooksId);
        var textBookUnit = textbooks
                .stream()
                .map(textbook -> {
                    var unitTextBook = new UnitClassTextBookEntity();
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

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_CLASS)
    @Cacheable("learning_content_in_class")
    public List<ChapterDto> getLearningContent(Long resourceId) {
        if (!classRepository.existsById(resourceId)) {
            throw new ClassNotFoundException(resourceId);
        }
        var chapters = chapterClassRepository.findFetchActionsByLearningContent(resourceId);
        var actions = chapters.stream()
                .map(ChapterClassEntity::getActions)
                .flatMap(Set::stream)
                .collect(groupingBy(x -> x.getId().getActivityType()));
        var units = mapUnit(actions.getOrDefault(ActionType.UNIT.name(), new ArrayList<>()));
        var quizzes = mapQuiz(actions.getOrDefault(ActionType.QUIZ.name(), new ArrayList<>()));
        var votes = mapVote(actions.getOrDefault(ActionType.VOTING.name(), new ArrayList<>()));
        return chapters.stream()
                .map(chapter -> {
                    var dto = chapterMapper.mapToChapterDto(chapter);
                    mapUnits(actions, units, chapter, dto);
                    mapQuizzes(actions, quizzes, chapter, dto);
                    mapVotes(actions, votes, chapter, dto);
                    return dto;
                })
                .collect(toList());
    }

    private List<ChapterDto> getDetailLearningContent(Long resourceId) {
        if (!classRepository.existsById(resourceId)) {
            throw new ClassNotFoundException(resourceId);
        }
        var chapters = chapterClassRepository.findFetchActionsByLearningContent(resourceId);
        var actions = chapters.stream()
                .map(ChapterClassEntity::getActions)
                .flatMap(Set::stream)
                .collect(groupingBy(x -> x.getId().getActivityType()));

        var unitActions = actions.getOrDefault(ActionType.UNIT.name(), new ArrayList<>());
        Set<UnitClassEntity> unitsFetched;
        var currentUser = userService.getCurrentUser();
        if (currentUser.getAccountType() == AccountTypeEnum.STAFF) {
            unitsFetched = unitClassRepository.findFetchTextbookUnitsDtoByIdIn(unitActions
                    .stream()
                    .map(x -> x.getId().getActivityId())
                    .collect(toList()));
        } else {
            unitsFetched = unitClassRepository.findPublishedFetchTextbookUnitsDtoByIdIn(unitActions
                    .stream()
                    .map(x -> x.getId().getActivityId())
                    .collect(toList()));
        }
        var units = unitsFetched
                .stream()
                .collect(groupingBy(UnitClassEntity::getId, collectingAndThen(toList(), list -> {
                    var unit = list.get(0);
                    var unitDto = new UnitDto();
                    unitDto.setId(unit.getId());
                    unitDto.setTitle(unit.getTitle());
                    unitDto.setIsFromCourse(unit.getUnitCourse() != null);
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
        List<QuizClassEntity> quizzesFetched;
        if (currentUser.getAccountType() == AccountTypeEnum.STAFF) {
            quizzesFetched = quizClassRepository.findQuizByIdIn(quizActions
                    .stream()
                    .map(x -> x.getId().getActivityId())
                    .collect(toList()));
        } else {
            quizzesFetched = quizClassRepository.findPublishedQuizByIdIn(quizActions
                    .stream()
                    .map(x -> x.getId().getActivityId())
                    .collect(toList()));
        }
        var quizzes = quizzesFetched
                .stream()
                .collect(groupingBy(QuizClassEntity::getId, collectingAndThen(toList(), list -> {
                    var quiz = list.get(0);
                    var quizDto = new QuizDto();
                    quizDto.setId(quiz.getId());
                    quizDto.setTitle(quiz.getTitle());
                    quizDto.setIsFromCourse(quiz.getQuizCourse() != null);
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
    @Cacheable("learning_content_group_by_textbook")
    public List<ChapterGroupByTextbookDto> getLearningContentGroupByTextbook(Long classId) {
        var chapters = getDetailLearningContent(classId);
        var textbooks = getTextbookInClass(classId, StringUtils.EMPTY);
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

    private void mapQuizzes(Map<String, List<ChapterActivityClassEntity>> actions, Collection<QuizDto> quizzes, ChapterClassEntity chapter, ChapterDto dto) {
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

    private QuizConfigEntity getDefaultConfig() {
        var config = new QuizConfigEntity();
        config.setStartedAt(LocalDateTime.now());
        config.setValidBefore(LocalDateTime.now().plusDays(7L));
        config.setMaxAttempt(0);
        config.setPassScore(0D);
        config.setHaveTimeLimit(false);
        config.setViewPreviousSessions(false);
        return config;
    }

    private void mapUnits(Map<String, List<ChapterActivityClassEntity>> actions, Collection<UnitDto> units, ChapterClassEntity chapter, ChapterDto dto) {
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

    private void mapVotes(Map<String, List<ChapterActivityClassEntity>> actions, List<VotingDto> votes, ChapterClassEntity chapter, ChapterDto dto) {
        var votesIdByChapter = actions.getOrDefault(ActionType.VOTING.name(), new ArrayList<>())
                .stream()
                .filter(action -> Objects.equals(action.getChapter().getId(), chapter.getId()))
                .collect(toList());
        var votesByChapter = new ArrayList<VotingDto>();
        votes.forEach(vote -> {
            var actionOpt = votesIdByChapter.stream()
                    .filter(x -> Objects.equals(x.getId().getActivityId(), vote.getId()))
                    .findAny();
            if (actionOpt.isPresent()) {
                vote.setOrder(actionOpt.get().getOrder());
                votesByChapter.add(vote);
            }
        });
        dto.setVotes(votesByChapter.stream()
                .sorted(Comparator.comparingInt(VotingDto::getOrder))
                .collect(toList()));
    }

    private List<UnitDto> mapUnit(List<ChapterActivityClassEntity> actions) {
        return unitClassRepository.findUnitsByIdIn(actions
                .stream()
                .map(x -> x.getId().getActivityId())
                .collect(toList()));
    }

    private List<QuizDto> mapQuiz(List<ChapterActivityClassEntity> actions) {
        return quizClassRepository.findQuizzesByIdIn(actions
                .stream()
                .map(x -> x.getId().getActivityId())
                .collect(toList())
        );
    }

    private List<VotingDto> mapVote(List<ChapterActivityClassEntity> actions) {
        return votingRepository.findVotesDtoByIdIn(actions
                .stream()
                .map(x -> x.getId().getActivityId())
                .collect(toList()));
    }

    @Transactional(readOnly = true)
    @Cacheable("classes_of_user")
    public ClassPagedDto getClassByUser(Long userId, ClassStatus status) {
        var currUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<ClassDto> result = new ArrayList<>();
        if (currUser.getAccountType() == AccountTypeEnum.STAFF) {
            result = classTeacherRepository.findClassByTeacherAndStatus(userId, status);
        } else {
            result = classStudentRepository.findClassByStudentAndStatus(userId, status);
        }
        var dto = new ClassPagedDto();
        dto.setListData(result);
        return dto;
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_LIST_TEACHER_IN_CLASS)
    @Cacheable("teacher_in_class")
    public MemberPagedDto getTeachers(Long resourceId) {
        var result = classTeacherRepository.findTeachers(resourceId);
        var list = result.stream()
                .map(member -> classMapper.mapToMemberDto(member))
                .collect(toList());
        var membersDto = new MemberPagedDto();
        membersDto.setListData(list);
        return membersDto;
    }

    @Transactional
    @Auth(permission = PermissionEnum.CREATE_UNIT_CLASS)
    public UnitDto createUnit(Long resourceId, Long chapterId, UnitDto dto) {
        if (!classRepository.existsById(resourceId)) {
            throw new ClassNotFoundException(resourceId);
        }
        var chapter = chapterClassRepository.findFetchActionsById(chapterId)
                .orElseThrow(() -> new ChapterNotFoundException(chapterId));

        var unit = persistUnit(resourceId, dto);
        var sortIndex = buildChapterSortIndex(chapter, unit.getId(), ActionType.UNIT.name());

        dto.setId(unit.getId());
        unit.setOrder(sortIndex.getOrder());
        dto.setIsFromCourse(false);
        return dto;
    }

    @Transactional
    @Auth(permission = PermissionEnum.CREATE_QUIZ_CLASS)
    public QuizDto createQuiz(Long resourceId, Long chapterId, QuizDto dto) {
        if (!classRepository.existsById(resourceId)) {
            throw new ClassNotFoundException(resourceId);
        }
        var chapter = chapterClassRepository.findFetchActionsById(chapterId)
                .orElseThrow(() -> new ChapterNotFoundException(chapterId));

        var quiz = persistQuiz(dto, resourceId);
        var sortIndex = buildChapterSortIndex(chapter, quiz.getId(), ActionType.QUIZ.name());

        dto.setId(quiz.getId());
        dto.setIsFromCourse(false);
        quiz.setOrder(sortIndex.getOrder());
        return dto;
    }

    public void buildChapterSortIndex(ChapterClassEntity chapter,
                                      Long activityId,
                                      String activityType,
                                      List<Long> unitIdClass,
                                      List<Long> quizIdClass,
                                      List<Long> votingIdClass,
                                      Collection<ChapterActivityClassEntity> currentActivity) {
        var activityToBeAdded = buildChapterSortIndexWhenCascade(chapter, activityId, activityType);
        var minIndexOfActivityClass = Integer.MAX_VALUE;
        for (var current : currentActivity) {
            if ((StringUtils.equals(current.getId().getActivityType(), ActionType.QUIZ.name())
                    && quizIdClass.contains(current.getId().getActivityId()) && current.getOrder() < minIndexOfActivityClass)
                    || (StringUtils.equals(current.getId().getActivityType(), ActionType.UNIT.name())
                    && unitIdClass.contains(current.getId().getActivityId()) && current.getOrder() < minIndexOfActivityClass)
                    || (StringUtils.equals(current.getId().getActivityType(), ActionType.VOTING.name())
                    && votingIdClass.contains(current.getId().getActivityId()) && current.getOrder() < minIndexOfActivityClass)) {
                minIndexOfActivityClass = current.getOrder();
            }
        }
        if (minIndexOfActivityClass == Integer.MAX_VALUE) {
            var currentSortIndex = chapterActivityClassRepository.findMaxSortIndexByChapter(chapter.getId());
            activityToBeAdded.setOrder(currentSortIndex + 1);
        } else {
            activityToBeAdded.setOrder(minIndexOfActivityClass);
        }
        activityToBeAdded.setChapter(chapter);
        currentActivity
                .forEach(current -> {
                    if ((StringUtils.equals(current.getId().getActivityType(), ActionType.QUIZ.name())
                            && quizIdClass.contains(current.getId().getActivityId()))
                            || (StringUtils.equals(current.getId().getActivityType(), ActionType.UNIT.name())
                            && unitIdClass.contains(current.getId().getActivityId()))
                            || (StringUtils.equals(current.getId().getActivityType(), ActionType.VOTING.name())
                            && votingIdClass.contains(current.getId().getActivityId()))) {
                        current.setOrder(current.getOrder() + 1);
                    }
                });
    }

    public ChapterActivityClassEntity buildChapterSortIndexWhenCascade(ChapterClassEntity chapter,
                                                                       Long activityId,
                                                                       String activityType) {
        var chapterSortIndex = new ChapterActivityClassEntity();
        chapterSortIndex.setId(new ChapterActivityClassKey());
        chapterSortIndex.getId().setActivityId(activityId);
        chapterSortIndex.getId().setActivityType(activityType);
        return chapterSortIndex;
    }

    public ChapterActivityClassEntity buildChapterSortIndex(ChapterClassEntity chapter, Long activityId, String activityType) {
        var chapterSortIndex = new ChapterActivityClassEntity();
        chapterSortIndex.setId(new ChapterActivityClassKey());
        chapterSortIndex.getId().setActivityId(activityId);
        chapterSortIndex.getId().setActivityType(activityType);
        var currentMaxSortIndex = chapterActivityClassRepository.findMaxSortIndexByChapter(chapter.getId()) + 1;
        chapterSortIndex.setOrder(currentMaxSortIndex);
        chapterSortIndex.setChapter(chapter);
        return chapterSortIndex;
    }

    private UnitClassEntity persistUnit(Long classId, UnitDto dto) {
        var unit = mapUnit(dto);
        unit.setClassEntity(entityManager.getReference(ClassEntity.class, classId));
        return unitClassRepository.save(unit);
    }

    private QuizClassEntity persistQuiz(QuizDto dto, Long classId) {
        var quiz = quizMapper.mapToQuizClassEntity(dto);
        quiz.setClassEntity(entityManager.getReference(ClassEntity.class, classId));
        if (dto.getExamId() != null) {
            var exam = examRepository.findFetchQuizClassById(dto.getExamId())
                    .orElseThrow(() -> new ExamNotFoundException(dto.getExamId()));
            quiz.setExam(exam);
        }
        var config = getDefaultConfig();
        config.setQuiz(quiz);
        quiz = quizClassRepository.save(quiz);
        var tag = utilsService.persistScoreTag(dto.getTag(), classId, GradeTagScope.CLASS);
        quiz.setTag(tag);
        entityManager.flush();

        var students = studentRepository.findStudentByClass(classId);
        gradeTagStudentRepository.saveAll(students.stream()
                .map(s -> new GradeTagStudentEntity(tag, s, 0D))
                .collect(toList()));

        return quiz;
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_UNIT_CLASS)
    @Cacheable("unit_in_class")
    public UnitDto getUnit(Long resourceId) {
        var unit = unitClassRepository.findUnitFetchTextbookById(resourceId)
                .orElseThrow(() -> new UnitNotFoundException(resourceId));
        return unitMapper.mapToUnitDto(unit);
    }

    @Transactional
    public List<ChapterDto> reorderChapters(Long classId, ReorderChapterDto dto) {
        var fromId = dto.getFrom().getId();
        var fromSortIndex = dto.getFrom().getOrder();
        var toSortIndex = dto.getTo().getOrder();
        var chapter = chapterClassRepository.findById(fromId)
                .orElseThrow(() -> new ChapterNotFoundException(fromId));
        chapter.setOrder(-1);
        entityManager.flush();
        if (fromSortIndex < toSortIndex) {
            var chapters = chapterClassRepository.findByClassInRange(classId, fromSortIndex, toSortIndex);
            chapters.forEach(c -> c.setOrder(c.getOrder() - 1));
            chapter.setOrder(toSortIndex);
        } else if (fromId > toSortIndex) {
            var chapters = chapterClassRepository.findByClassInRange(classId, toSortIndex - 1, fromSortIndex - 1);
            chapters.forEach(c -> c.setOrder(c.getOrder() + 1));
            chapter.setOrder(toSortIndex);
        }
        entityManager.flush();
        return getLearningContent(classId);
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_LIST_UNIT_CLASS)
    @Cacheable("units_in_class")
    public List<UnitDto> getUnitsByClass(Long resourceId) {
        var currentUser = userService.getCurrentUser();
        Set<UnitClassEntity> units;
        if (currentUser.getAccountType() == AccountTypeEnum.STAFF) {
            units = unitClassRepository.findFetchByClassId(resourceId);
        } else {
            units = unitClassRepository.findPublishedFetchByClassId(resourceId);
        }
        return units
                .stream()
                .map(unitMapper::mapToUnitDto)
                .collect(toList());
    }

    @Transactional
    public List<ChapterDto> reorderActivity(Long classId, ReorderActivityDto dto) {
        var actionId = dto.getFrom().getActivityId();
        var fromSortIndex = dto.getFrom().getOrder();
        var toSortIndex = dto.getTo().getOrder();
        var toChapterId = dto.getTo().getChapterId();
        var fromChapterId = dto.getFrom().getChapterId();
        var toChapter = chapterClassRepository.findFetchActionsById(toChapterId)
                .orElseThrow(() -> new ChapterNotFoundException(toChapterId));
        var fromAction = chapterActivityClassRepository.findActionByIdSortIndexAndChapter(actionId, fromChapterId, fromSortIndex)
                .orElseThrow(() -> new ChapterActionNotFoundException(actionId));
        if (toSortIndex == null) {
            chapterActivityClassRepository.deleteById(fromAction.getId());
            entityManager.flush();
            var maxSortIndexInChapter = 1;
            var newAction = createNewAction(actionId, fromAction);
            newAction.setOrder(maxSortIndexInChapter);
            newAction.setChapter(toChapter);
            entityManager.flush();
            return getLearningContent(classId);
        }

        fromAction.setOrder(-1);
        entityManager.flush();
        var actions = chapterActivityClassRepository.findActivitiesByChapterAndHigherOrderExcept(toChapterId, toSortIndex);

        var newAction = fromAction;
        if (!Objects.equals(toChapterId, fromChapterId)) {
            chapterActivityClassRepository.deleteById(fromAction.getId());
            entityManager.flush();
            newAction = createNewAction(actionId, fromAction);
            newAction.setChapter(toChapter);
            newAction.setOrder(-1);
            entityManager.flush();
        }

        if (toSortIndex.equals(TOP)) {
            actions.forEach(action -> action.setOrder(action.getOrder() + 1));
            newAction.setOrder(1);
        } else if (toSortIndex.equals(BOTTOM)) {
            var max = chapterActivityClassRepository.findMaxSortIndexByChapter(toChapterId);
            newAction.setOrder(max + 1);
        } else {
            actions.forEach(action -> action.setOrder(action.getOrder() + 1));
            entityManager.flush();
            newAction.setOrder(toSortIndex);
        }

        return getLearningContent(classId);
    }

    private ChapterActivityClassEntity createNewAction(Long activityId, ChapterActivityClassEntity fromAction) {
        var newAction = new ChapterActivityClassEntity();
        newAction.setId(new ChapterActivityClassKey());
        newAction.getId().setActivityId(activityId);
        newAction.getId().setActivityType(fromAction.getId().getActivityType());
        return newAction;
    }

    @Transactional
    @Auth(permission = PermissionEnum.UPDATE_CLASS)
    public ChapterDto createChapter(Long resourceId, ChapterDto dto) {
        var learningContent = classRepository.findLearningContentById(resourceId)
                .orElseThrow(() -> new ClassNotFoundException(resourceId));

        var chapter = chapterMapper.mapToChapterClassEntity(dto);
        var currentMaxSortIndex = chapterClassRepository.findMaxCurrentSortIndex(resourceId).orElse(0) + 1;
        chapter.setOrder(currentMaxSortIndex);
        chapter.setLearningContent(learningContent);

        chapterClassRepository.save(chapter);

        return chapterMapper.mapToChapterDto(chapter);
    }

    @Transactional
    @Auth(permission = PermissionEnum.UPDATE_CLASS)
    public ChapterDto updateChapter(Long resourceId, Long chapterId, ChapterDto dto) {
        var chapter = chapterClassRepository.findById(chapterId)
                .orElseThrow(() -> new ChapterNotFoundException(chapterId));

        chapter.setTitle(dto.getTitle());
        return dto;
    }

    @Transactional
    @Auth(permission = PermissionEnum.UPDATE_CLASS)
    public Long deleteChapter(Long resourceId, Long chapterId) {
        return deleteChapter(chapterId);
    }

    public Long deleteChapter(Long chapterId) {
        if (!chapterClassRepository.existsById(chapterId)) {
            return chapterId;
        }

        var chapterActions = chapterActivityClassRepository.findChapterActionByChapter(chapterId)
                .stream()
                .collect(groupingBy(ChapterActivityClassKey::getActivityType, mapping(ChapterActivityClassKey::getActivityId, toList())));

        chapterClassRepository.deleteById(chapterId);

        entityManager.createQuery("DELETE FROM QuizClassEntity q WHERE q.id IN (:ids)")
                .setParameter("ids", chapterActions.getOrDefault(ActionType.QUIZ.name(), new ArrayList<>()))
                .executeUpdate();
        entityManager.createQuery("DELETE FROM UnitClassEntity q WHERE q.id IN (:ids)")
                .setParameter("ids", chapterActions.getOrDefault(ActionType.UNIT.name(), new ArrayList<>()))
                .executeUpdate();
        return chapterId;
    }

    public void cloneChapterOnDeleted(Long chapterCourseId) {
        chapterClassRepository.findByChapterCourseIdNotInEndedClass(chapterCourseId)
                .forEach(chapter -> deleteChapter(chapter.getId()));
    }

    @Transactional
    public void cloneChapterOnUpdated(ChapterCourseEntity chapterCourse) {
        chapterClassRepository.findByChapterCourseIdNotInEndedClass(chapterCourse.getId())
                .forEach(chapter -> chapter.setTitle(chapterCourse.getTitle()));
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void cloneQuizOnUpdated(QuizCourseEntity quizCourse, Long courseId) {
        var activeClasses = classRepository.findCreatedOrOngoingIdClassByCourse(courseId);
        var quizzesClass = quizClassRepository.findByQuizCourseIdAndClassIdIn(quizCourse.getId(), activeClasses);
        quizzesClass.forEach(quizClass -> {
            quizMapper.mapToQuizClass(quizCourse, quizClass);
            quizClass.removeExam();
            if (quizCourse.getExam() != null) {
                quizClass.setExam(quizCourse.getExam());
            }
            eventService.updateQuizEvent(quizClass);
        });
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void cloneUnitOnUpdated(UnitCourseEntity unitCourse) {
        unitClassRepository.deleteAllTextbookByUnitInCourseUnit(unitCourse.getId());
        var unitsClass = unitClassRepository.findByUnitCourseId(unitCourse.getId());
        unitsClass.forEach(unitClass -> {
            unitClass.setTitle(unitCourse.getTitle());
            unitClass.setContent(unitCourse.getContent());
            unitClass.setAttachment(unitCourse.getAttachment());
            if (CollectionUtils.isNotEmpty(unitCourse.getTextbooks())) {
                var textbooksId = unitCourse.getTextbooks()
                        .stream()
                        .map(UnitCourseTextBookEntity::getId)
                        .map(UnitTextBookKey::getTextbookId)
                        .collect(toSet());
                setTextbook(unitCourse, unitClass, textbooksId);
            }
        });
        entityManager.flush();
    }

    @Transactional
    @Auth(permission = PermissionEnum.UPDATE_UNIT_CLASS)
    public UnitDto updateUnit(Long resourceId, UnitDto dto) {
        unitClassRepository.deleteAllTextbookByUnit(resourceId);
        var unit = unitClassRepository.findUnitFetchTextbookById(resourceId)
                .orElseThrow(() -> new UnitNotFoundException(resourceId));
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

        return unitMapper.mapToUnitDto(managedUnit);
    }

    @Transactional
    @Auth(permission = PermissionEnum.DELETE_UNIT_CLASS)
    public Long deleteUnit(Long resourceId) {
        deleteChapterActivity(resourceId, ActionType.UNIT.name());
        entityManager.createQuery("DELETE FROM UnitClassEntity q WHERE q.id = (:id)")
                .setParameter("id", resourceId)
                .executeUpdate();
        return resourceId;
    }

    public void cascadeDeleteUnit(Long courseId, Long unitCourseId) {
        var activeClassId = classRepository.findCreatedOrOngoingIdClassByCourse(courseId);
        var chaptersId = chapterClassRepository.findByClassIn(activeClassId);
        deleteChapterActionUnitType(unitCourseId, chaptersId);
    }

    public void deleteChapterActionUnitType(Long unitCourseId, List<Long> chaptersId) {
        var unitsInActiveClass = chapterActivityClassRepository.findActivityIdByChapterIdIn(chaptersId, ActionType.UNIT.name());
        var unitsIdByUnitCourseId = unitClassRepository.findUnitsIdByUnitCourseId(unitCourseId);
        var tobeDeletedUnits = unitsIdByUnitCourseId.stream()
                .filter(unitsInActiveClass::contains)
                .collect(toList());
        deleteChapterActivity(tobeDeletedUnits, ActionType.UNIT.name());
        entityManager.createQuery("DELETE FROM UnitClassEntity q WHERE q.id IN (:idList)")
                .setParameter("idList", tobeDeletedUnits)
                .executeUpdate();
    }

    public void deleteChapterActionQuizType(Long quizCourseId) {
        deleteChapterActivity(quizClassRepository.findQuizzesIdByQuizCourseId(quizCourseId), ActionType.QUIZ.name());
    }

    public void deleteChapterActivity(List<Long> ids, String type) {
        entityManager.createQuery("DELETE FROM ChapterActivityClassEntity c WHERE c.id.activityId IN (:ids) AND c.id.activityType = (:type)")
                .setParameter("ids", ids)
                .setParameter("type", type)
                .executeUpdate();
    }

    private void deleteChapterActivity(Long id, String type) {
        entityManager.createQuery("DELETE FROM ChapterActivityClassEntity c WHERE c.id.activityId = (:id) AND c.id.activityType = (:type)")
                .setParameter("id", id)
                .setParameter("type", type)
                .executeUpdate();
    }

    @Transactional
    @Auth(permission = PermissionEnum.UPDATE_QUIZ_CLASS)
    public QuizDto updateQuiz(Long resourceId, QuizDto dto) {
        var quiz = quizClassRepository.findById(resourceId)
                .orElseThrow(() -> new QuizNotFoundException(resourceId));

        quizMapper.mapToQuizEntity(quiz, dto);
        Long examId = quiz.getExam() != null ? quiz.getExam().getId() : null;
        if (!Objects.equals(dto.getExamId(), examId)) {
            if (quizSessionRepository.countQuizSessionByQuiz(quiz.getId()) != 0) {
                throw new UpdateQuizException("Không thể thay đổi đề kiểm tra khi đã có học sinh làm bài");
            }
        }
        if (dto.getExamId() != null) {
            var exam = examRepository.findFetchQuizClassById(dto.getExamId())
                    .orElseThrow(() -> new ExamNotFoundException(dto.getExamId()));
            if (quiz.getExam() != null) {
                quiz.removeExam();
            }
            quiz.setExam(exam);
        }
        eventService.updateQuizEvent(quiz);
        return quizMapper.mapToQuizDto(quiz);
    }

    public void cascadeDeleteQuiz(Long courseId, Long quizCourseId) {
        var activeClassId = classRepository.findCreatedOrOngoingIdClassByCourse(courseId);
        var chaptersId = chapterClassRepository.findByClassIn(activeClassId);
        deleteChapterActionQuizType(quizCourseId, chaptersId);
    }

    public void deleteChapterActionQuizType(Long quizCourseId, List<Long> chaptersId) {
        var quizzesInActiveClass = chapterActivityClassRepository.findActivityIdByChapterIdIn(chaptersId, ActionType.QUIZ.name());
        var quizzesIdByUnitCourseId = quizClassRepository.findQuizzesIdByQuizCourseId(quizCourseId);
        var tobeDeletedQuizzes = quizzesIdByUnitCourseId.stream()
                .filter(quizzesInActiveClass::contains)
                .collect(toList());
        deleteChapterActivity(tobeDeletedQuizzes, ActionType.QUIZ.name());
        entityManager.createQuery("DELETE FROM QuizClassEntity  q WHERE q.id IN (:idList)")
                .setParameter("idList", tobeDeletedQuizzes)
                .executeUpdate();
    }


    @Transactional
    @Auth(permission = PermissionEnum.DELETE_QUIZ_CLASS)
    public Long deleteQuiz(Long resourceId) {
        deleteChapterActivity(resourceId, ActionType.QUIZ.name());
        var tagId = entityManager.createQuery("SELECT q.tag.id FROM QuizClassEntity q WHERE q.id = :quizId")
                .setParameter("quizId", resourceId)
                .getResultList();
        entityManager.createQuery("DELETE FROM QuizClassEntity  q WHERE q.id = (:id)")
                .setParameter("id", resourceId)
                .executeUpdate();

        if (CollectionUtils.isNotEmpty(tagId)) {
            entityManager.createQuery("DELETE FROM GradeTag g WHERE g.id = :tagId")
                    .setParameter("tagId", tagId.get(0))
                    .executeUpdate();
        }
        return resourceId;
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_QUIZ_CLASS)
    @Cacheable("quiz_in_class")
    public QuizDto getQuiz(Long resourceId) {
        var quiz = quizClassRepository.findFetchById(resourceId, String.format("%s-%s", QuizClassEntity.Fields.config, QuizClassEntity.Fields.exam))
                .orElseThrow(() -> new QuizNotFoundException(resourceId));
        var currentUser = userService.getCurrentUser();
        if (quiz.getState() == ActivityState.PRIVATE && currentUser.getAccountType() == AccountTypeEnum.STUDENT) {
            throw new UnauthorizedException();
        }

        var currentUserId = userService.getCurrentUser().getId();
        var config = quiz.getConfig();
        var quizDto = quizMapper.mapToQuizDto(quiz);
        quizDto.setConfig(quizConfigMapper.mapToConfigDto(config));
        quizDto.setIsAllowedToContinueLastSession(isAllowedToContinueLastSession(resourceId, currentUserId));
        if (quizDto.getIsAllowedToContinueLastSession()) {
            quizDto.setIsAllowedToInitSession(false);
        } else {
            quizDto.setIsAllowedToInitSession(isAllowedToInitSession(config, resourceId, currentUserId));
        }
        if (quizDto.getIsAllowedToContinueLastSession()) {
            var unEndedSessionOpt = quizSessionRepository.existingUnEndedQuizSession(resourceId, currentUserId);
            if (unEndedSessionOpt.isEmpty()) {
                log.warn("Data is not correct, please check database for quiz {} and user {}", resourceId, currentUserId);
            } else {
                quizDto.setUnEndedSessionId(unEndedSessionOpt.get().toString());
            }
        }
        if (quiz.getExam() != null) {
            Long totalGradeOfExam = examRepository.totalGradeOfExam(quiz.getExam().getId());
            if (totalGradeOfExam != null) {
                quizDto.setTotalGrade(Math.toIntExact(totalGradeOfExam));
            }
        }
        return quizDto;
    }

    @Transactional(readOnly = true)
    @Cacheable("exam_id_of_quiz")
    public Long getExamIdOfQuiz(Long quizId) {
        return quizClassRepository.getExamIdOfQuiz(quizId);
    }

    private boolean isAllowedToInitSession(QuizConfigEntity config, Long quizId, Long userId) {
        var quizSessionsCount = quizSessionRepository.countQuizSessionByUser(quizId, userId);
        return !Objects.equals(config.getMaxAttempt(), quizSessionsCount)
                && config.getStartedAt() != null && LocalDateTime.now().isAfter(config.getStartedAt())
                && config.getValidBefore() != null && LocalDateTime.now().isBefore(config.getValidBefore());
    }

    private boolean isAllowedToContinueLastSession(Long quizId, Long userId) {
        return quizSessionRepository.isOpenedQuizSessionExistingByStudent(quizId, userId);
    }

    @Transactional
    @Auth(permission = PermissionEnum.UPDATE_QUIZ_CLASS)
    public QuizEntryDto publishQuiz(Long classId, Long resourceId) {
        // TODO: need to check whether to return course name and course id
        var quiz = quizClassRepository.findFetchConfigById(resourceId)
                .orElseThrow(() -> new QuizNotFoundException(resourceId));
        var config = quiz.getConfig();
        validateEnableToPublishQuiz(quiz, config);
        quiz.setState(ActivityState.PUBLIC);
        eventService.createOrUpdateQuizEvent(classId, quiz);
        return quizMapper.mapToQuizEntry(quiz);
    }

    private void validateEnableToPublishQuiz(QuizClassEntity quiz, QuizConfigEntity config) {
        if (quiz.getExam() == null) {
            log.warn("Quiz {} must be assigned an exam before published");
            throw new QuizIsNotAllowedToPublishException("Bài kiểm tra phải được gán đề kiểm tra trước khi hiển thị cho học sinh làm bài");
        }
        if (config == null || config.getPassScore() == null || config.getStartedAt() == null || config.getValidBefore() == null) {
            log.warn("Quiz {} must be fully configured before published", quiz.getId());
            throw new QuizIsNotAllowedToPublishException("Bài kiểm tra phải được thiết lập trước khi hiển thị cho học sinh làm bài");
        }
        var examId = quiz.getExam().getId();
        if (questionRepository.isExistingUnPointedQuestion(examId)) {
            log.warn("Cannot publish quiz {} because the exam {} have un-pointed questions", quiz.getId(), examId);
            throw new QuizIsNotAllowedToPublishException("Không thể hiển thị bài kiểm tra cho học sinh vì tồn tại câu hỏi chưa được gán điểm trong đề kiểm tra.");
        }
    }

    @Transactional
    @Auth(permission = PermissionEnum.UPDATE_QUIZ_CLASS)
    public QuizEntryDto hideQuiz(Long classId, Long resourceId) {
        var quiz = quizClassRepository.findById(resourceId)
                .orElseThrow(() -> new QuizNotFoundException(resourceId));
        quiz.setState(ActivityState.PRIVATE);
        eventService.createOrUpdateQuizEvent(classId, quiz);
        quizClassRepository.save(quiz);
        return quizMapper.mapToQuizEntry(quiz);
    }

    @Transactional
    @Auth(permission = PermissionEnum.UPDATE_QUIZ_CLASS)
    public QuizEntryDto createOrUpdateQuizConfig(Long classId, Long resourceId, QuizConfigDto dto) {
        if (!classRepository.existsById(classId)) {
            throw new ClassNotFoundException(classId);
        }

        var quiz = quizClassRepository.findFetchById(resourceId, String.format("%s", QuizClassEntity.Fields.config))
                .orElseThrow(() -> new QuizNotFoundInClassException(resourceId, classId));

        if (ActivityState.PUBLIC == quiz.getState()) {
            log.warn("Cannot configure quiz {} because it's state is PUBLIC", resourceId);
            throw new UnableConfigureQuizException();
        }

        var totalScore = examRepository.totalGradeOfExam(quiz.getExam().getId());

        if (dto.getPassScore() != null && dto.getPassScore() > totalScore) {
            log.warn("Pass score is greater than total score of exam");
            throw new UnableConfigureQuizException("Điểm đạt phải nhỏ hơn hoặc bằng tổng điểm của bài kiểm tra");
        }

        var configOpt = quizConfigRepository.findFetchById(resourceId);
        var configToBeCreatedOrUpdated = quizConfigMapper.mapToConfigEntity(dto);
        if (configOpt.isPresent()) {
            var config = configOpt.get();
            quizConfigMapper.mapToConfigEntity(config, configToBeCreatedOrUpdated);
            quizConfigRepository.save(config);
            dto.setId(config.getId());
        } else {
            configToBeCreatedOrUpdated.setQuiz(quiz);
            quizConfigRepository.save(configToBeCreatedOrUpdated);
            dto.setId(configToBeCreatedOrUpdated.getId());
        }
        eventService.createOrUpdateQuizEvent(classId, quiz);
        var response = quizMapper.mapToQuizEntry(quiz);
        response.setConfig(dto);
        return response;
    }

    @Transactional
    @Auth(permission = PermissionEnum.UPDATE_UNIT_CLASS)
    public UnitDto publishUnit(Long resourceId) {
        var unit = unitClassRepository.findById(resourceId)
                .orElseThrow(() -> new UnitNotFoundException(resourceId));
        unit.setState(ActivityState.PUBLIC);
        unitClassRepository.save(unit);
        return unitMapper.mapToUnitDto(unit);
    }

    @Transactional
    @Auth(permission = PermissionEnum.UPDATE_UNIT_CLASS)
    public UnitDto hideUnit(Long resourceId) {
        var unit = unitClassRepository.findById(resourceId)
                .orElseThrow(() -> new UnitNotFoundException(resourceId));
        unit.setState(ActivityState.PRIVATE);
        unitClassRepository.save(unit);
        return unitMapper.mapToUnitDto(unit);
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_LIST_QUIZ_CLASS)
    @Cacheable("quizzes_in_class")
    public List<QuizDto> getQuizzes(Long resourceId) {
        var accountType = userService.getCurrentUser().getAccountType();
        return quizClassRepository.findByClassAndRole(resourceId, accountType)
                .stream()
                .map(quizMapper::mapToQuizDto)
                .collect(toList());
    }

    @Transactional
    @Auth(permission = PermissionEnum.UPDATE_CLASS_SCHEDULER)
    public List<ClassSessionRequestDto> configScheduler(Long resourceId, ClassSchedulerConfig dto) {

        deleteExistingSessionsAndEvents(resourceId);

        var classEntity = classRepository.findFetchSessionsById(resourceId)
                .orElseThrow(() -> new ClassNotFoundException(resourceId));

        if (CollectionUtils.isEmpty(dto.getSessionRecurrence()) || dto.getNumberOfSession() == null || dto.getNumberOfSession() == 0) {
            classEntity.setDaysOfWeek(StringUtils.EMPTY);
            return new ArrayList<>();
        }

        updateClassSchedulerConfigure(dto, classEntity);

        var sessions = configureClassScheduler(dto, classEntity);

        createClassSessionsEvents(sessions, eventService.getCalendar(CalendarType.CLASS, resourceId));

        sessions.forEach(session -> attendanceService.createOfficialAttendanceSessionForSession(resourceId, session));
        return sessions.stream()
                .map(classMapper::mapToClassSessionDto)
                .collect(toList());
    }

    private void createClassSessionsEvents(List<ClassSessionEntity> sessions, CalendarEntity calendar) {
        eventRepository.saveAll(sessions.stream()
                .map(session -> createClassSessionEvent(session, calendar))
                .collect(toList()));
    }

    private List<ClassSessionEntity> configureClassScheduler(ClassSchedulerConfig dto, ClassEntity classEntity) {
        var startedAt = dto.getStartedAt();
        var daysScheduled = dto.getSessionRecurrence();
        var numberOfSessionsPerWeek = daysScheduled.size();

        var i = 0;
        var sessions = new ArrayList<ClassSessionEntity>();
        var dayOfWeek = daysScheduled.get(i % numberOfSessionsPerWeek);
        startedAt = startedAt.with(TemporalAdjusters.nextOrSame(DayOfWeek.valueOf(dayOfWeek.getDayOfWeek())));
        sessions.add(createSession(startedAt, classEntity, dayOfWeek));
        i++;
        while (i < dto.getNumberOfSession()) {
            dayOfWeek = daysScheduled.get(i % numberOfSessionsPerWeek);
            startedAt = startedAt.with(TemporalAdjusters.next(DayOfWeek.valueOf(dayOfWeek.getDayOfWeek())));
            sessions.add(createSession(startedAt, classEntity, dayOfWeek));
            i++;
        }
        classSessionRepository.saveAll(sessions);
        entityManager.flush();
        return sessions;
    }

    private void updateClassSchedulerConfigure(ClassSchedulerConfig dto, ClassEntity classEntity) {
        classEntity.setDaysOfWeek(dto.getSessionRecurrence()
                .stream()
                .map(x -> String.format("%s|%s-%s", x.getDayOfWeek(), x.getStartedAt(), x.getFinishedAt()))
                .collect(joining("[]")));
    }

    private void deleteExistingSessionsAndEvents(Long classId) {
        entityManager.createQuery("DELETE FROM EventEntity e WHERE e.id.eventType = 'CLASS_SESSION' AND e.id.eventId IN " +
                        "(SELECT c.id FROM ClassSessionEntity c WHERE c.classEntity.id = (:classId))")
                .setParameter("classId", classId)
                .executeUpdate();
        entityManager.createQuery("DELETE FROM ClassSessionEntity c WHERE c.classEntity.id = (:id)")
                .setParameter("id", classId)
                .executeUpdate();
    }

    private ClassSessionEntity createSession(LocalDate startedAt, ClassEntity classEntity, ClassSchedulerConfig.ClassSession dayOfWeek) {
        var session = new ClassSessionEntity();
        session.setClassEntity(classEntity);
        var startedHour = Integer.valueOf(dayOfWeek.getStartedAt().split(":")[0]);
        var startedMinute = Integer.valueOf(dayOfWeek.getStartedAt().split(":")[1]);
        var endedHour = Integer.valueOf(dayOfWeek.getFinishedAt().split(":")[0]);
        var endedMinute = Integer.valueOf(dayOfWeek.getFinishedAt().split(":")[1]);
        var startedTimestamp = LocalDateTime.of(startedAt.getYear(),
                startedAt.getMonth(),
                startedAt.getDayOfMonth(),
                startedHour,
                startedMinute);

        var endedTimestamp = LocalDateTime.of(startedAt.getYear(),
                startedAt.getMonth(),
                startedAt.getDayOfMonth(),
                endedHour,
                endedMinute);
        session.setStartedAt(startedTimestamp);
        session.setFinishedAt(endedTimestamp);
        session.setIsScheduled(true);

        return session;
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_CLASS_SCHEDULER)
    @Cacheable("configure_sessions")
    public ClassSchedulerConfig getConfiguredSessions(Long resourceId) {
        var classEntity = classRepository.findById(resourceId)
                .orElseThrow(() -> new ClassNotFoundException(resourceId));
        var dto = new ClassSchedulerConfig();
        dto.setStartedAt(classEntity.getStartedAt());
        var numberOfSession = classSessionRepository.countSessionsByClassAndScheduledStatus(resourceId, true);
        if (numberOfSession == null || numberOfSession == 0) {
            return dto;
        }
        dto.setNumberOfSession(numberOfSession);

        dto.setSessionRecurrence(Arrays.stream(StringUtils.split(classEntity.getDaysOfWeek(), "[]"))
                .map(x -> {
                    var y = StringUtils.split(x, "|");
                    var configuredSession = new ClassSchedulerConfig.ClassSession();
                    configuredSession.setDayOfWeek(y[0]);
                    configuredSession.setStartedAt(StringUtils.split(y[1], "-")[0]);
                    configuredSession.setFinishedAt(StringUtils.split(y[1], "-")[1]);
                    return configuredSession;
                })
                .collect(toList()));
        return dto;
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_CLASS_SCHEDULER)
    @Cacheable("class_sessions_for_student")
    public List<ClassSessionResponseDto> getClassSessionsForStudent(Long resourceId) {
        var sessions = classSessionRepository.findSessionsByClass(resourceId);
        return sessions.stream()
                .map(classMapper::mapToClassSessionResponseDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_CLASS_SCHEDULER)
    @Cacheable("class_sessions_not_configured")
    public List<ClassSessionResponseDto> getClassSessionsNotConfigured(Long resourceId) {
        var sessions = classSessionRepository.findSessionsByClass(resourceId, false);
        return sessions.stream()
                .map(classMapper::mapToClassSessionResponseDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_CLASS_SCHEDULER)
    @Cacheable("class_sessions_configured")
    public List<ClassSessionResponseDto> getClassSessionsConfigured(Long resourceId) {
        var sessions = classSessionRepository.findSessionsByClass(resourceId, true);
        return sessions.stream()
                .map(classMapper::mapToClassSessionResponseDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_CLASS_SCHEDULER)
    @Cacheable("class_sessions")
    public ClassSessionResponseDtoWrapper getClassSessions(Long resourceId, LocalDateTime dateTime, Integer previous, Integer after) {
        var sessions = classSessionRepository.findClassSessions(resourceId, dateTime.plusHours(7L), previous, after);
        var dto = new ClassSessionResponseDtoWrapper();
        dto.setDaysOfWeek(classRepository.getScheduler(resourceId));
        if (CollectionUtils.isNotEmpty(sessions)) {
            dto.setSessions(sessions.stream()
                    .map(classMapper::mapToClassSessionResponseDto)
                    .collect(toList()));
            dto.setHasAfter(classSessionRepository.hasMoreAfterByDate(resourceId, sessions.get(sessions.size() - 1).getStartedAt()));
            dto.setHasPrevious(classSessionRepository.hasMorePreviousByDate(resourceId, sessions.get(0).getStartedAt()));
        } else {
            dto.setHasPrevious(classSessionRepository.hasMorePreviousByDate(resourceId, dateTime));
        }
        return dto;
    }

    @Transactional
    @Auth(permission = PermissionEnum.DELETE_CLASS_SESSION)
    public Long deleteSession(Long classId, Long sessionId) {
        var classEntity = classRepository.findFetchSessionsById(classId)
                .orElseThrow(() -> new ClassNotFoundException(classId));
        // TODO
        var event = eventRepository.findById(new EventKey(EventType.CLASS_SESSION, sessionId));
        entityManager.createQuery("DELETE FROM ClassSessionEntity c WHERE c.id = (:id)")
                .setParameter("id", sessionId)
                .executeUpdate();
        if (event.isPresent()) {
            entityManager.createQuery("DELETE FROM EventEntity e WHERE e.id.eventId = (:eventId) AND e.id.eventType = (:eventType)")
                    .setParameter("eventId", event.get().getId().getEventId())
                    .setParameter("eventType", event.get().getId().getEventType())
                    .executeUpdate();
        }
        return sessionId;
    }

    @Transactional(readOnly = true)
    public Long getClassIdFromQuiz(Long quizId) {
        var chapterId = chapterActivityClassRepository.findChapterIdByAction(ActionType.QUIZ.name(), quizId)
                .orElseThrow(() -> new QuizNotFoundException(quizId));
        return classRepository.findClassIdByChapter(chapterId)
                .orElseThrow(() -> new ChapterNotFoundException(chapterId));
    }

    @Transactional(readOnly = true)
    public Long getClassIdFromQuizSession(UUID sessionId) {
        var chapterId = chapterActivityClassRepository.findChapterIdByQuizSession(sessionId)
                .orElseThrow(() -> new QuizSessionNotFoundException(sessionId.toString()));
        return classRepository.findClassIdByChapter(chapterId)
                .orElseThrow(() -> new ChapterNotFoundException(chapterId));
    }

    @Transactional
    @Auth(permission = PermissionEnum.DELETE_TEACHER)
    public Long deleteTeacherFromClass(Long resourceId, Long teacherId) {
        if (!classTeacherRepository.existsById(new ClassTeacherKey(teacherId, resourceId))) {
            throw new TeacherNotFoundInClassException(teacherId, resourceId);
        }
        entityManager.createQuery("DELETE FROM ClassTeacherEntity c WHERE c.id.classId = (:classId) " +
                        "AND c.id.teacherId = (:teacherId)")
                .setParameter("classId", resourceId)
                .setParameter("teacherId", teacherId)
                .executeUpdate();
        return teacherId;
    }

    @Transactional
    @Auth(permission = PermissionEnum.REMOVE_STUDENT_FROM_CLASS)
    public Long deleteStudentFromClass(Long resourceId, Long studentId) {
        if (!classStudentRepository.existsById(new ClassStudentKey(studentId, resourceId))) {
            throw new TeacherNotFoundInClassException(studentId, resourceId);
        }
        entityManager.createQuery("DELETE FROM ClassStudentEntity c WHERE c.id.classId = (:classId) " +
                        "AND c.id.studentId = (:studentId)")
                .setParameter("classId", resourceId)
                .setParameter("studentId", studentId)
                .executeUpdate();

        entityManager.createQuery("DELETE FROM QuizSessionEntity q WHERE q.quiz.id IN (SELECT qq FROM QuizClassEntity qq WHERE qq.classEntity.id = (:classId)) " +
                        "AND q.student.id = (:studentId)")
                .setParameter("classId", resourceId)
                .setParameter("studentId", studentId)
                .executeUpdate();

        entityManager.createQuery("DELETE FROM GradeTagStudentEntity g WHERE g.student.id = (:studentId) " +
                        "AND g.tag.id IN " +
                        "(SELECT gg FROM GradeTag gg WHERE (gg.scope = 'CLASS' AND gg.scopeId = (:classId)) OR (gg.scope = 'COURSE' AND gg.scopeId = (SELECT c.course.id FROM ClassEntity c WHERE c.id = (:classId))) )")
                .setParameter("classId", resourceId)
                .setParameter("studentId", studentId)
                .executeUpdate();
        return studentId;
    }

    @Transactional
    @Auth(permission = PermissionEnum.UPDATE_TEACHER)
    public AddTeacherRequestDto.TeacherDto changeRoleOfTeacher(Long resourceId, Long teacherId, AddTeacherRequestDto.TeacherDto dto) {
        entityManager.createQuery("UPDATE ClassTeacherEntity c SET c.role = (:role) " +
                        "WHERE c.id.classId = (:classId) " +
                        "AND c.id.teacherId = (:teacherId)")
                .setParameter("role", TeacherRole.valueOf(dto.getRole()))
                .setParameter("classId", resourceId)
                .setParameter("teacherId", teacherId)
                .executeUpdate();
        return dto;
    }

    @Transactional(readOnly = true)
    @Cacheable("teacher_candidates")
    public List<StaffDTO> getTeacherCandidates(Long classId) {
        validateExistingClass(classId);
        return classRepository.findTeacherCandidatesByClassId(classId)
                .stream()
                .map(staffMapper::mapFromStaffEntityToStaffDTO)
                .collect(toList());
    }

    private void validateExistingClass(Long classId) {
        if (!classRepository.existsById(classId)) {
            throw new ClassNotFoundException(classId);
        }
    }

    @Auth(permission = {PermissionEnum.DELETE_CLASS})
    @Transactional
    public Long deleteClass(Long resourceId) {
        validateExistingClass(resourceId);
        classRepository.deleteById(resourceId);
        gradeTagRepository.deleteByScopeAndScopeId(GradeTagScope.CLASS, resourceId);
        return resourceId;
    }

    public void cascadeDeleteClass(Long courseId) {
        classRepository.findClassIdByCourseId(courseId)
                .forEach(this::deleteClass);
    }

    public List<Long> getClassOfUser() {
        var currentUser = userService.getCurrentUser();
        if (currentUser.getAccountType() == AccountTypeEnum.STAFF) {
            return classTeacherRepository.findClassIdByTeacher(currentUser.getId());
        }
        return classStudentRepository.findClassIdByStudent(currentUser.getId());
    }

    @Transactional
    public void cascadeAddTextbooks(Long courseId, AddTextbookToCourseRequest dto) {
        var activeClasses = classRepository.findCreatedOrOngoingIdClassByCourse(courseId);
        var collect = activeClasses.stream()
                .flatMap(activeClass -> dto.getIdList()
                        .stream()
                        .filter(id -> !classTextbookRepository.existsById(new ClassTextbookKey(activeClass, id)))
                        .map(id -> {
                            var entity = new ClassTextbookEntity();
                            entity.setClassEntity(entityManager.getReference(ClassEntity.class, activeClass));
                            entity.setTextbook(entityManager.getReference(TextbookEntity.class, id));
                            return entity;
                        }))
                .collect(toList());
        classTextbookRepository.saveAll(collect);
    }

    @Transactional(readOnly = true)
    // TODO: add auth
    @Cacheable("textbook_in_class")
    public List<TextbookDto> getTextbookInClass(Long classId, String keyword) {
        return textbookRepository.findInClass(classId, StringUtils.isNotBlank(keyword) ? keyword.toLowerCase() : "")
                .stream()
                .map(textBookMapper::mapToTextBookDto)
                .collect(toList());
    }

    @Transactional
    public void cascadeDeleteTextbook(Long courseId, Long textbookId) {
        var activeClasses = classRepository.findCreatedOrOngoingIdClassByCourse(courseId);
        activeClasses
                .forEach(classId -> {
                    if (classTextbookRepository.existsById(new ClassTextbookKey(classId, textbookId))) {
                        classTextbookRepository.deleteById(new ClassTextbookKey(classId, textbookId));
                        entityManager.createQuery("DELETE FROM UnitClassTextBookEntity u " +
                                        "WHERE u.textbook.id = (:textbookId) " +
                                        "AND u.unit.id IN (SELECT c.id FROM UnitClassEntity c WHERE c.classEntity.id = (:classId))")
                                .setParameter("textbookId", textbookId)
                                .setParameter("classId", classId)
                                .executeUpdate();
                    }
                });
    }

    @Transactional(readOnly = true)
    public Long countTextbookOfCourse(Long classId) {
        return classTextbookRepository.countTextbooks(classId);
    }

    @Transactional
    public void cascadeReorderChapters(Long courseId, ReorderChapterDto dto) {
        var activeClasses = classRepository.findCreatedOrOngoingIdClassByCourse(courseId);
        activeClasses
                .forEach(classId -> {
                    var fromChapterCourseId = dto.getFrom().getId();
                    var fromChapterCourseSortIndex = dto.getFrom().getOrder();
                    var toChapterCourseSortIndex = dto.getTo().getOrder();
                    var chapter = chapterClassRepository.findByClassAndChapterCourse(classId, fromChapterCourseId)
                            .orElseThrow(() -> new ChapterNotFoundException(fromChapterCourseId));
                    chapter.setOrder(-1);
                    entityManager.flush();
                    if (fromChapterCourseSortIndex < toChapterCourseSortIndex) {
                        var chapters = chapterClassRepository.findByClassInRange(classId, fromChapterCourseSortIndex, toChapterCourseSortIndex);
                        chapters.forEach(c -> c.setOrder(c.getOrder() - 1));
                        chapter.setOrder(toChapterCourseSortIndex);
                    } else if (fromChapterCourseId > toChapterCourseSortIndex) {
                        var chapters = chapterClassRepository.findByClassInRange(classId, toChapterCourseSortIndex - 1, fromChapterCourseSortIndex - 1);
                        chapters.forEach(c -> c.setOrder(c.getOrder() + 1));
                        chapter.setOrder(toChapterCourseSortIndex);
                    }
                    entityManager.flush();
                });
    }

    @Transactional
    public void cascadeReorderActivity(Long courseId, ReorderActivityDto dto) {
        var activeClasses = classRepository.findCreatedOrOngoingIdClassByCourse(courseId);
        activeClasses
                .forEach(classId -> {
                    var activityCourseId = dto.getFrom().getActivityId();
                    var fromSortIndex = dto.getFrom().getOrder();
                    var toSortIndex = dto.getTo().getOrder();
                    var toChapterCourseId = dto.getTo().getChapterId();
                    var fromChapterCourseId = dto.getFrom().getChapterId();
                    var fromChapter = chapterClassRepository.findIdClassAndChapterCourse(classId, fromChapterCourseId)
                            .orElseThrow(() -> new ChapterNotFoundException(toChapterCourseId));
                    var toChapter = chapterClassRepository.findFetchActivitiesByClassAndChapterCourse(classId, toChapterCourseId)
                            .orElseThrow(() -> new ChapterNotFoundException(toChapterCourseId));
                    var fromChapterActivity = chapterActivityCourseRepository.findActionByIdSortIndexAndChapter(activityCourseId, fromChapterCourseId, fromSortIndex)
                            .orElseThrow(() -> new ChapterNotFoundException(fromChapterCourseId));
                    ChapterActivityClassEntity fromActivity = null;
                    if (fromChapterActivity.getId().getActivityType() == ActionType.UNIT.name()) {
                        var id = unitClassRepository.findUnitsIdByUnitCourseIdAndClassId(activityCourseId, classId)
                                .orElseThrow(() -> new ChapterActionNotFoundException(activityCourseId));
                        fromActivity = chapterActivityClassRepository.findById(new ChapterActivityClassKey(id, ActionType.UNIT.name(), fromChapter))
                                .orElseThrow(() -> new ChapterActionNotFoundException(activityCourseId));
                    } else {
                        var id = quizClassRepository.findIdByQuizCourseIdAndClassId(activityCourseId, classId)
                                .orElseThrow(() -> new ChapterActionNotFoundException(activityCourseId));
                        fromActivity = chapterActivityClassRepository.findById(new ChapterActivityClassKey(id, ActionType.QUIZ.name(), fromChapter))
                                .orElseThrow(() -> new ChapterActionNotFoundException(activityCourseId));
                    }
                    if (toSortIndex == null) {
                        chapterActivityClassRepository.deleteById(fromActivity.getId());
                        entityManager.flush();
                        var maxSortIndexInChapter = 1;
                        var newAction = createNewAction(fromActivity.getId().getActivityId(), fromActivity);
                        newAction.setOrder(maxSortIndexInChapter);
                        newAction.setChapter(toChapter);
                        entityManager.flush();
                        return;
                    }

                    fromActivity.setOrder(-1);
                    entityManager.flush();

                    var activities = chapterActivityClassRepository.findActivitiesByChapterAndHigherOrderExcept(toChapter.getId(), toSortIndex);
                    var newAction = fromActivity;
                    if (!Objects.equals(toChapterCourseId, fromChapterCourseId)) {
                        chapterActivityClassRepository.deleteById(fromActivity.getId());
                        entityManager.flush();
                        newAction = createNewAction(fromActivity.getId().getActivityId(), fromActivity);
                        log.info("to chapter :{}", toChapter.getId());
                        newAction.setChapter(toChapter);
                        newAction.setOrder(-1);
                        entityManager.flush();
                    }

                    if (TOP.equals(toSortIndex)) {
                        activities.forEach(action -> action.setOrder(action.getOrder() + 1));
                        newAction.setOrder(1);
                    } else if (BOTTOM.equals(toSortIndex)) {
                        var maxChapterCourseActivityOpt = chapterActivityCourseRepository.findMaxActivityByChapter(toChapterCourseId);
                        var maxChapterCourseActivityOrder = 0;
                        if (maxChapterCourseActivityOpt.isPresent()) {
                            maxChapterCourseActivityOrder = maxChapterCourseActivityOpt.get().getOrder();
                        }
                        newAction.setOrder(maxChapterCourseActivityOrder + 1);
                        int finalMaxChapterCourseActivityOrder = maxChapterCourseActivityOrder;
                        activities
                                .forEach(activity -> {
                                    if (activity.getOrder() > finalMaxChapterCourseActivityOrder) {
                                        activity.setOrder(activity.getOrder() + 1);
                                    }
                                });
                    } else {
                        activities.forEach(action -> action.setOrder(action.getOrder() + 1));
                        entityManager.flush();
                        newAction.setOrder(toSortIndex);
                    }
                });
    }

    @Transactional(readOnly = true)
    @Cacheable("grade_tag_of_class")
    public List<GradeTagDto> getAllGradeTagOfClass(Long classId) {
        var classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new ClassNotFoundException(classId));
        var courseId = classEntity.getCourse().getId();
        var classTags = gradeTagRepository.findAllByScopeAndScopeId(GradeTagScope.CLASS, classId);
        var courseTags = gradeTagRepository.findAllByScopeAndScopeId(GradeTagScope.COURSE, courseId);
        classTags.addAll(courseTags);
        return classTags.stream()
                .map(tag -> {
                    var dto = new GradeTagDto();
                    dto.setTitle(tag.getTitle());
                    dto.setId(tag.getId());
                    return dto;
                })
                .collect(toList());
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_LIST_UNIT_CLASS)
    @Cacheable("unit_groups_by_textbook_in_course")
    public List<UnitByTextbookDto> getUnitsGroupByTextbook(Long resourceId) {
        Set<UnitClassEntity> units;
        var currentUser = userService.getCurrentUser();
        if (currentUser.getAccountType() == AccountTypeEnum.STAFF) {
            units = unitClassRepository.findFetchByClassId(resourceId);
        } else {
            units = unitClassRepository.findPublishedFetchByClassId(resourceId);
        }
        var textbooks = getTextbookInClass(resourceId, StringUtils.EMPTY);

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