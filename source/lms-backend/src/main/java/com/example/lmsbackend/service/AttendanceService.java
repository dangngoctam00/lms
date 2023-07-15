package com.example.lmsbackend.service;

import com.example.lmsbackend.domain.StudentEntity;
import com.example.lmsbackend.domain.classmodel.AttendanceStudentEntity;
import com.example.lmsbackend.domain.classmodel.ClassStudentKey;
import com.example.lmsbackend.domain.classmodel.SessionAttendanceTimeEntity;
import com.example.lmsbackend.domain.exam.GradeTagScope;
import com.example.lmsbackend.domain.exam.GradeTagStudentEntity;
import com.example.lmsbackend.domain.scheduler.ClassSessionEntity;
import com.example.lmsbackend.dto.classes.*;
import com.example.lmsbackend.enums.AttendanceState;
import com.example.lmsbackend.enums.SessionAttendanceStrategy;
import com.example.lmsbackend.exceptions.aclass.*;
import com.example.lmsbackend.mapper.ClassMapper;
import com.example.lmsbackend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.*;

import static com.example.lmsbackend.constant.AppConstant.ATTENDANCE_TAG;
import static com.example.lmsbackend.utils.UserUtils.getNameOfUser;
import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceService {

    private final SessionSessionAttendanceTimeTimeRepository sessionAttendanceTimeRepository;
    private final AttendanceStudentRepository attendanceStudentRepository;
    private final ClassSessionRepository classSessionRepository;
    private final GradeTagStudentRepository gradeTagStudentRepository;
    private final ClassStudentRepository classStudentRepository;
    private final GradeTagRepository gradeTagRepository;

    private final ClassMapper classMapper;

    @PersistenceContext
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public OverallAttendanceDto getOverallAttendance(Long classId) {
        var dto = new OverallAttendanceDto();
        var sessionsId = classSessionRepository.findSessionsIdByClass(classId);
        var attendanceStudents = attendanceStudentRepository.findOfficialAttendanceBySessionIdIn(sessionsId);
        var statistics = new AttendanceStatistics();
        statistics.setAverageAttendance(calculateAttendanceRate(attendanceStudents));

        var byStudent = attendanceStudents.stream()
                .collect(groupingBy(attendanceStudent -> attendanceStudent.getStudent().getStudent().getId()));

        var byAttendanceSession = attendanceStudents.stream()
                .collect(groupingBy(AttendanceStudentEntity::getAttendanceTime));

        var happenedSession = byAttendanceSession.keySet()
                .stream()
                .filter(sessionTime -> LocalDateTime.now().isAfter(sessionTime.getSession().getFinishedAt()))
                .collect(toList());
        dto.setHappenedSession(happenedSession.size());
        Map<Long, Integer> presentCount = new HashMap<>();
        byStudent.forEach((student, list) -> {
            presentCount.put(student,
                    (int) list.stream()
                            .filter(as -> LocalDateTime.now().isAfter(as.getAttendanceTime().getSession().getFinishedAt())
                                    && (as.getState() == AttendanceState.PRESENT || as.getState() == AttendanceState.LATE))
                            .count());
        });

        dto.setPresentCount(presentCount);

                var map = new HashMap<String, SessionAttendanceDto>();
        byAttendanceSession.forEach((attendanceSession, attendanceSessionList) -> {
            var startedAt = attendanceSession.getSession().getStartedAt().toString();
            var sessionAttendanceDto = new SessionAttendanceDto();
            map.put(startedAt, sessionAttendanceDto);
            var metaInfo = classMapper.mapToAttendanceMetaData(attendanceSession.getSession());
            sessionAttendanceDto.setMetaInfo(metaInfo);
            sessionAttendanceDto.setStudentAttendance(attendanceSessionList.stream()
                    .collect(groupingBy(a -> a.getStudent().getStudent().getId(), collectingAndThen(toList(), list -> {
                        var studentAttendance = list.get(0);
                        var studentAttendanceDto = new StudentAttendanceDto();
                        studentAttendanceDto.setState(studentAttendance.getState().name());
                        studentAttendanceDto.setName(getNameOfUser(studentAttendance.getStudent().getStudent()));
                        studentAttendanceDto.setAvatar(studentAttendance.getStudent().getStudent().getAvatar());
                        studentAttendanceDto.setAttendanceRate(calculateAttendanceRate(byStudent.get(studentAttendance.getStudent().getStudent().getId())));
                        return studentAttendanceDto;
                    }))));
        });
        dto.setAttendanceInfo(map);
        dto.setStatistics(statistics);

        var h = map.values().stream()
                .findAny();
        if (h.isPresent()) {
            var t = h.get()
                    .getStudentAttendance()
                    .values();
            statistics.setNumOfStudentWithPerfectAttendance((int) t.stream()
                    .filter(z -> z.getAttendanceRate() != null)
                    .filter(z -> Objects.equals(z.getAttendanceRate().intValue(), 100))
                    .count());

            if (statistics.getAverageAttendance() != null) {
                statistics.setNumOfStudentWithAboveAverageAttendance((int) t.stream()
                        .filter(z -> z.getAttendanceRate() != null)
                        .filter(z -> z.getAttendanceRate() >= statistics.getAverageAttendance())
                        .count());

                statistics.setNumOfStudentWithBelowAverageAttendance((int) t.stream()
                        .filter(z -> z.getAttendanceRate() != null)
                        .filter(z -> z.getAttendanceRate() < statistics.getAverageAttendance())
                        .count());
            } else {
                statistics.setNumOfStudentWithBelowAverageAttendance(0);
                statistics.setNumOfStudentWithBelowAverageAttendance(0);
            }
        }
        return dto;
    }

    private Double calculateAttendanceRate(Collection<AttendanceStudentEntity> attendanceList) {
        var valuedAttendanceList = attendanceList.stream()
                .filter(a -> a.getState() != AttendanceState.NONE)
                .collect(toList());
        if (valuedAttendanceList.size() == 0) return 0.0;
        var presentCount = valuedAttendanceList.stream()
                .filter(attendance -> attendance.getState() == AttendanceState.PRESENT)
                .count();
        var lateCount = valuedAttendanceList.stream()
                .filter(attendance -> attendance.getState() == AttendanceState.LATE)
                .count();
        return (presentCount + 0.5 * lateCount) / (double) valuedAttendanceList.size() * 100;
    }

    public SessionAttendanceTimeEntity createOfficialAttendanceSessionForSession(Long classId, ClassSessionEntity session) {
        var attendanceTime = new SessionAttendanceTimeEntity();

        attendanceTime.setIsOfficial(true);

        var students = classStudentRepository.getStudentsByClassId(classId);
        students.stream()
                .map(student -> {
                    var studentAttendance = new AttendanceStudentEntity();
                    studentAttendance.setStudent(student);
                    studentAttendance.setAttendanceTime(attendanceTime);
                    return studentAttendance;
                })
                .collect(toList());
        attendanceTime.setSession(session);
        return attendanceTime;
    }

    @Transactional
    public void createNewAttendanceOfSession(Long classId, Long sessionId) {
        var session = classSessionRepository.findFetchAttendancesById(sessionId)
                .orElseThrow(() -> new ClassSessionNotFoundException(sessionId));

        var attendance = new SessionAttendanceTimeEntity();
        attendance.setSession(session);

        sessionAttendanceTimeRepository.save(attendance);

        var students = classStudentRepository.getStudentsByClassId(classId);
        students.forEach(student -> {
            var studentAttendance = new AttendanceStudentEntity();
            studentAttendance.setStudent(student);
            studentAttendance.setAttendanceTime(attendance);
        });

        entityManager.flush();
        updateOfficialAttendance(sessionId);
//        return students.stream()
//                .collect(groupingBy(student -> student.getStudent().getId(), collectingAndThen(toList(), list -> {
//                    var s = list.get(0);
//                    var dto = new StudentAttendanceDto();
//                    dto.setName(getNameOfUser(s.getStudent()));
//                    dto.setAvatar(s.getStudent().getAvatar());
//                    dto.setState(AttendanceState.NONE.name());
//                    return dto;
//                })));
    }

    @Transactional
    public void changeStudentAttendanceBatchBySession(Long classId, Long sessionId, StudentAttendanceStateDto dto) {
        checkValidSession(sessionId);
        entityManager.createQuery("UPDATE AttendanceStudentEntity s SET s.state = (:state) WHERE s.attendanceTime.id IN " +
                        "(SELECT a FROM SessionAttendanceTimeEntity a WHERE a.session.id = (:sessionId) AND a.isOfficial = true)")
                .setParameter("state", AttendanceState.valueOf(dto.getState()))
                .setParameter("sessionId", sessionId)
                .executeUpdate();
        entityManager.flush();
        gradeAttendanceScore(classId);
    }

    private void checkValidSession(Long sessionId) {
        var session = classSessionRepository.findById(sessionId)
                        .orElseThrow(() -> new ClassSessionNotFoundException(sessionId));
        if (LocalDateTime.now().isBefore(session.getStartedAt())) {
            throw new ChangeAttendanceInvalidException();
        }
    }

    @Transactional
    public void changeStudentAttendanceBySession(Long classId, Long sessionId, Long studentId, StudentAttendanceStateDto dto) {
        checkValidSession(sessionId);
        entityManager.createQuery("UPDATE AttendanceStudentEntity s SET s.state = (:state) WHERE s.attendanceTime.id IN " +
                        "(SELECT a FROM SessionAttendanceTimeEntity a WHERE a.session.id = (:sessionId) AND a.isOfficial = true) " +
                        "AND s.student.id.studentId = (:studentId) " +
                        "AND s.student.id.classId = (:classId)")
                .setParameter("state", AttendanceState.valueOf(dto.getState()))
                .setParameter("sessionId", sessionId)
                .setParameter("studentId", studentId)
                .setParameter("classId", classId)
                .executeUpdate();
        entityManager.flush();
        gradeAttendanceScore(classId, studentId);
    }

    @Transactional
    public void changeStudentAttendanceBatchByTimeAndSession(Long classId, Long sessionId, Long timeId, StudentAttendanceStateDto dto) {
        checkValidSession(sessionId);
        entityManager.createQuery("UPDATE AttendanceStudentEntity s SET s.state = (:state) WHERE s.attendanceTime.id = (:timeId)")
                .setParameter("state", AttendanceState.valueOf(dto.getState()))
                .setParameter("timeId", timeId)
                .executeUpdate();
        entityManager.flush();
        updateOfficialAttendance(sessionId);
        entityManager.flush();
        gradeAttendanceScore(classId);
    }

    @Transactional
    public void changeStudentAttendanceByTimeAndSession(Long classId, Long studentId, Long sessionId, Long timeId, StudentAttendanceStateDto dto) {
        checkValidSession(sessionId);
        entityManager.createQuery("UPDATE AttendanceStudentEntity s SET s.state = (:state) WHERE s.attendanceTime.id = (:timeId) " +
                        "AND s.student.id.studentId = (:studentId) " +
                        "AND s.student.id.classId = (:classId)")
                .setParameter("state", AttendanceState.valueOf(dto.getState()))
                .setParameter("timeId", timeId)
                .setParameter("studentId", studentId)
                .setParameter("classId", classId)
                .executeUpdate();
        entityManager.flush();

        var strategy = classSessionRepository.findAttendanceStrategy(sessionId)
                .orElseThrow(() -> new ClassSessionNotFoundException(sessionId));
        var attendanceList = attendanceStudentRepository.findAttendanceStudentBySessionAndStudent(sessionId, studentId, classId);
        if (StringUtils.equals(strategy, SessionAttendanceStrategy.LAST_TIME.name())) {
            handleLastTimeStrategy(attendanceList);
        } else if (StringUtils.equals(strategy, SessionAttendanceStrategy.ANY.name())) {
            handleAnyStrategy(attendanceList);
        }
        entityManager.flush();
        gradeAttendanceScore(classId, studentId);
    }

    private void updateOfficialAttendance(Long sessionId) {
        var strategy = classSessionRepository.findAttendanceStrategy(sessionId)
                .orElseThrow(() -> new ClassSessionNotFoundException(sessionId));
        var attendanceStudentList = attendanceStudentRepository.findAttendanceStudentBySession(sessionId);
        var attendanceStudentByStudent = attendanceStudentList.stream()
                .collect(groupingBy(a -> a.getStudent().getId()));
        if (StringUtils.equals(strategy, SessionAttendanceStrategy.LAST_TIME.name())) {
            attendanceStudentByStudent.forEach((student, attendanceList) -> {
                handleLastTimeStrategy(attendanceList);
            });
        } else if (StringUtils.equals(strategy, SessionAttendanceStrategy.ANY.name())) {
            attendanceStudentByStudent.forEach((student, attendanceList) -> {
                handleAnyStrategy(attendanceList);
            });
        }
    }

    private void handleAnyStrategy(List<AttendanceStudentEntity> attendanceList) {
        var officialOpt = attendanceList.stream()
                .filter(a -> BooleanUtils.isTrue(a.getAttendanceTime().getIsOfficial()))
                .findAny();
        if (officialOpt.isPresent()) {
            var attendance = getAttendanceStateBasedOnPriority(attendanceList);
            officialOpt.get().setState(attendance);
        }
    }

    private void handleLastTimeStrategy(List<AttendanceStudentEntity> attendanceList) {
        var officialOpt = attendanceList.stream()
                .filter(a -> BooleanUtils.isTrue(a.getAttendanceTime().getIsOfficial()))
                .findAny();
        if (officialOpt.isPresent()) {
            var attendance = attendanceList.stream()
                    .reduce((a, b) -> {
                        if (a.getAttendanceTime().getIsOfficial()) return b;
                        if (b.getAttendanceTime().getIsOfficial()) return a;
                        if (a.getAttendanceTime().getCreatedAt().isAfter(b.getAttendanceTime().getCreatedAt())) {
                            return a;
                        }
                        return b;
                    });
            if (attendance.isPresent()) {
                officialOpt.get().setState(attendance.get().getState());
            }
        }
    }

    private AttendanceState getAttendanceStateBasedOnPriority(List<AttendanceStudentEntity> attendanceList) {
        var highestPriorityState = AttendanceState.NONE;
        for (int i = 0; i < attendanceList.size(); ++i) {

            var attendanceStudentEntity = attendanceList.get(i);
            if (BooleanUtils.isTrue(attendanceStudentEntity.getAttendanceTime().getIsOfficial())) {
                continue;
            }
            var state = attendanceStudentEntity.getState();
            if (state == AttendanceState.ABSENT) {
                return AttendanceState.ABSENT;
            } else if (state.getPriority() > highestPriorityState.getPriority()) {
                highestPriorityState = state;
            }
        }
        return highestPriorityState;
    }

    @Transactional
    public void deleteAttendanceTime(Long sessionId, Long timeId) {
        if (sessionAttendanceTimeRepository.existsByIdAndOfficial(timeId)) {
            throw new SummaryAttendanceColumnCannotBeDeletedException();
        }
        entityManager.createQuery("DELETE FROM SessionAttendanceTimeEntity a WHERE a.id = (:timeId)")
                .setParameter("timeId", timeId)
                .executeUpdate();
        entityManager.flush();
        updateOfficialAttendance(sessionId);
    }

    @Transactional(readOnly = true)
    public MeetingAttendanceDto getMeetingAttendance(Long sessionId) {
        var session = classSessionRepository.findFetchAttendancesById(sessionId)
                .orElseThrow(() -> new ClassSessionNotFoundException(sessionId));
        var attendanceTimeList = sessionAttendanceTimeRepository.findBySessionId(sessionId);
        var dto = new MeetingAttendanceDto();
        dto.setMetaInfo(classMapper.mapToAttendanceMetaData(session));
        var attendanceInfo = dto.getAttendanceInfo();
        var index = 0;
        for (int i = 0; i < attendanceTimeList.size(); ++i) {
            var attendanceTimeDto = new AttendanceTimeDto();
            var attendanceTime = attendanceTimeList.get(i);
            var isOfficial = BooleanUtils.isTrue(attendanceTime.getIsOfficial());
            var title = "";
            if (BooleanUtils.isTrue(isOfficial)) {
                title = "Tổng kết";
            } else {
                index++;
                title = String.format("Lần %s", index);
            }
            attendanceInfo.put(title, attendanceTimeDto);
            mapAttendanceTime(attendanceTimeDto, attendanceTime);
        }
        return dto;
    }

    private void mapAttendanceTime(AttendanceTimeDto attendanceTimeDto, SessionAttendanceTimeEntity attendanceTime) {
        var metaInfo = classMapper.mapToAttendanceTimeMetaData(attendanceTime);
        attendanceTimeDto.setMetaInfo(metaInfo);
        var studentAttendance = attendanceTime.getAttendances()
                .stream()
                .collect(groupingBy(attendance -> attendance.getStudent().getStudent().getId(), collectingAndThen(toList(), list -> {
                    var byStudent = list.get(0);
                    var studentAttendanceDto = new StudentAttendanceDto();
                    studentAttendanceDto.setName(getNameOfUser(byStudent.getStudent().getStudent()));
                    studentAttendanceDto.setAvatar(byStudent.getStudent().getStudent().getAvatar());
                    studentAttendanceDto.setState(byStudent.getState().name());

                    if (byStudent.getAttendanceTime().getIsOfficial()) {
                        studentAttendanceDto.setAttendanceRate(calculateAttendanceRate(List.of(byStudent)));
                    }

                    return studentAttendanceDto;
                })));

        attendanceTimeDto.setStudentAttendance(studentAttendance);
    }

    @Transactional
    public void changeAttendanceSessionStrategy(Long sessionId, AttendanceSessionStrategyDto dto) {
        var session = classSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ClassSessionNotFoundException(sessionId));
        session.setStrategy(SessionAttendanceStrategy.valueOf(dto.getStrategy()));
        updateOfficialAttendance(sessionId);
        entityManager.flush();
    }

//    public void createAttendanceForNewStudent(ClassMemberEntity student, Long classId) {
//        var attendanceSessions = attendanceSessionRepository.findByClassId(classId);
//        attendanceSessions.forEach(attendanceSession -> {
//            attendanceSession.getAttendances()
//                    .forEach(attendance -> {
//                        var attendanceStudent = new AttendanceStudentEntity();
//                        attendanceStudent.setStudent(student);
//                        attendanceStudent.setAttendance(attendance);
//                    });
//        });
//    }

    @Transactional
    public void addAttendanceSessionNote(Long sessionId, AttendanceSessionNoteDto dto) {
        var attendance = classSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ClassSessionNotFoundException(sessionId));

        attendance.setNoteInSession(dto.getNote());
    }

    public void gradeAttendanceScore(Long classId, Long studentId) {
        var tag = gradeTagRepository.findTagByTitleAndScope(ATTENDANCE_TAG, GradeTagScope.CLASS, classId)
                .orElseThrow(GradeTagNotFoundException::new);
        var scoreOpt = gradeTagStudentRepository.findByTagScopeAndStudent(ATTENDANCE_TAG, GradeTagScope.CLASS, classId, studentId);
        var sessionsId = classSessionRepository.findSessionsIdByClass(classId);
        var attendanceList = attendanceStudentRepository.findOfficialAttendanceByStudentIdAndSessionIdIn(studentId, sessionsId);
        // TODO : get attendance score from config
        var dumpAttendanceScore = 10;
        var rate = calculateAttendanceRate(attendanceList);
        if (scoreOpt.isPresent()) {
            scoreOpt.get().setGrade(rate * dumpAttendanceScore / 100);
        } else {
            var score = new GradeTagStudentEntity();
            score.setTag(tag);
            score.setStudent(attendanceList.stream().findAny().get().getStudent().getStudent());
            score.setGrade(rate * dumpAttendanceScore / 100);
            gradeTagStudentRepository.save(score);
        }
    }

    public void gradeAttendanceScore(Long classId) {
        var tag = gradeTagRepository.findTagByTitleAndScope(ATTENDANCE_TAG, GradeTagScope.CLASS, classId)
                .orElseThrow(() -> {
                    log.warn("Grade tag {} is not found", ATTENDANCE_TAG);
                    return new GradeTagNotFoundException();
                });
        var scores = gradeTagStudentRepository.findByTagAndScope(ATTENDANCE_TAG, GradeTagScope.CLASS, classId);
        var sessionsId = classSessionRepository.findSessionsIdByClass(classId);
        // TODO : get attendance score from config
        var dumpAttendanceScore = 10;
        var attendanceStudents = attendanceStudentRepository.findOfficialAttendanceBySessionIdIn(sessionsId);
        var attendanceStudentGradeList = attendanceStudents.stream()
                .collect(groupingBy(attendance -> attendance.getStudent().getStudent().getId(), collectingAndThen(toList(), list -> {
                    var student = list.get(0).getStudent().getStudent();
                    var rate = calculateAttendanceRate(list);
                    var attendanceStudentScoreOpt = scores.stream()
                            .filter(s -> Objects.equals(s.getStudent().getId(), student.getId()))
                            .findAny();
                    GradeTagStudentEntity attendanceStudentScore;
                    if (attendanceStudentScoreOpt.isPresent()) {
                        attendanceStudentScore = attendanceStudentScoreOpt.get();
                        attendanceStudentScore.setGrade(rate * dumpAttendanceScore / 100);
                    } else {
                        attendanceStudentScore = new GradeTagStudentEntity();
                        attendanceStudentScore.setTag(tag);
                        attendanceStudentScore.setStudent((StudentEntity) student);
                        attendanceStudentScore.setGrade(rate * dumpAttendanceScore / 100);
                    }
                    return attendanceStudentScore;
                })))
                .values();
        gradeTagStudentRepository.saveAll(attendanceStudentGradeList);
    }

    @Transactional
    public void createAttendanceForStudent(Long studentId, Long classId) {
        var student = classStudentRepository.findById(new ClassStudentKey(studentId, classId))
                .orElseThrow(() -> new StudentNotFoundInClassException(studentId, classId));
        var sessions = classSessionRepository.findFetchAttendance(classId);
        sessions.forEach(session -> {
            var sessionTime = session.getAttendancesTime();
            sessionTime.stream()
                    .map(time -> {
                        var studentAttendance = new AttendanceStudentEntity();
                        studentAttendance.setStudent(student);
                        studentAttendance.setAttendanceTime(time);
                        return studentAttendance;
                    })
                    .collect(toList());
        });

    }
}
