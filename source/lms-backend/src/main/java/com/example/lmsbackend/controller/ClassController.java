package com.example.lmsbackend.controller;

import com.example.lmsbackend.dto.classes.*;
import com.example.lmsbackend.dto.exam.QuizConfigDto;
import com.example.lmsbackend.dto.exam.QuizEntryDto;
import com.example.lmsbackend.dto.request.course.ReorderActivityDto;
import com.example.lmsbackend.dto.request.course.ReorderChapterDto;
import com.example.lmsbackend.dto.resource.TextbookDto;
import com.example.lmsbackend.dto.response.BaseResponse;
import com.example.lmsbackend.dto.response.course.*;
import com.example.lmsbackend.enums.ClassStatus;
import com.example.lmsbackend.service.ClassService;
import com.example.lmsbackend.utils.SearchCriteriaUtils;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.lmsbackend.constant.AppConstant.API_PREFIX;

@RestController
@RequestMapping(API_PREFIX)
@RequiredArgsConstructor
public class ClassController {

    private final ClassService classService;
    private final SearchCriteriaUtils searchCriteriaUtils;

    @GetMapping("/classes")
    public ResponseEntity<ClassPagedDto> getClasses(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                    @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                    @RequestParam(value = "keyword", required = false) String keyword,
                                                    @RequestParam(value = "filter", required = false) String filter,
                                                    @RequestParam(value = "sort", required = false) String sort) {
        return ResponseEntity.ok(classService.getClasses(searchCriteriaUtils.buildSearchCriteria(keyword, filter, sort, page, size)));
    }

    /**
     * Currently, do not use this endpoint with pagination, but the response is the same as pagination request
     * The classDto items only contain ID and NAME field.
     */
    @GetMapping("/user/{userId}/classes")
    public ResponseEntity<ClassPagedDto> getClassesByUser(@PathVariable Long userId, @RequestParam(value = "status", defaultValue = "AVAILABLE") String status) {
        return ResponseEntity.ok(classService.getClassByUser(userId, ClassStatus.valueOf(status)));
    }

    @PostMapping("/classes")
    public ResponseEntity<ClassDto> createClass(@Valid @RequestBody CreateClassRequestDto dto) {
        return ResponseEntity.ok(classService.createClass(dto.getCourseId(), dto));
    }

    @GetMapping("/classes/{id}")
    public ResponseEntity<ClassDto> findClass(@PathVariable Long id) {
        return ResponseEntity.ok(classService.findClass(id));
    }

    @PutMapping("/classes/{id}")
    public ResponseEntity<ClassDto> updateClass(@PathVariable Long id, @Valid @RequestBody UpdateClassRequestDto dto) {
        return ResponseEntity.ok(classService.updateClass(id, dto));
    }

    @GetMapping("/classes/{classId}/learning_content")
    public ResponseEntity<List<ChapterDto>> getLearningContent(@PathVariable Long classId) {
        return ResponseEntity.ok(classService.getLearningContent(classId));
    }

    @GetMapping("/classes/{classId}/learning_content/textbooks")
    public ResponseEntity<List<ChapterGroupByTextbookDto>> getLearningContentGroupByTextbook(@PathVariable Long classId) {
        return ResponseEntity.ok(classService.getLearningContentGroupByTextbook(classId));
    }

    @PostMapping("/classes/{classId}/learning_content/chapters")
    public ResponseEntity<ChapterDto> createChapter(@PathVariable Long classId, @Valid @RequestBody ChapterDto dto) {
        return ResponseEntity.ok(classService.createChapter(classId, dto));
    }

    @PutMapping("/classes/{classId}/learning_content/chapters/{chapterId}")
    public ResponseEntity<ChapterDto> updateChapter(@PathVariable Long classId,
                                                    @PathVariable Long chapterId,
                                                    @Valid @RequestBody ChapterDto dto) {
        return ResponseEntity.ok(classService.updateChapter(classId, chapterId, dto));
    }

    @DeleteMapping("/classes/{classId}/learning_content/chapters/{chapterId}")
    public ResponseEntity<Long> deleteChapter(@PathVariable Long classId,
                                              @PathVariable Long chapterId) {
        return ResponseEntity.ok(classService.deleteChapter(classId, chapterId));
    }

    @ApiResponses(
            value = @ApiResponse(code = 400, message = "Exception code: CLASS_NOT_FOUND, STUDENT_NOT_FOUND_EXCEPTION, STUDENT_HAS_ALREADY_IN_CLASS")
    )
    @PostMapping("/classes/{classId}/students")
    public ResponseEntity<BaseResponse> addStudents(@PathVariable Long classId, @Valid @RequestBody AddStudentRequestDto dto) {
        classService.addStudents(classId, dto.getStudentIds());
        return ResponseEntity.ok(new BaseResponse());
    }

    @ApiResponses(
            value = @ApiResponse(code = 400, message = "Exception code: CLASS_NOT_FOUND, STAFF_NOT_FOUND, TEACHER_HAS_ALREADY_IN_CLASS")
    )
    @PostMapping("/classes/{classId}/teachers")
    public ResponseEntity<List<MemberDto>> addTeachers(@PathVariable Long classId, @Valid @RequestBody AddTeacherRequestDto dto) {
        return ResponseEntity.ok(classService.addTeachers(classId, dto));
    }

    @ApiResponses(
            value = @ApiResponse(code = 400, message = "Exception code: TEACHER_NOT_FOUND_IN_CLASS")
    )
    @DeleteMapping("/classes/{classId}/teachers/{teacherId}")
    public ResponseEntity<Long> deleteTeacherFromClass(@PathVariable Long classId, @PathVariable Long teacherId) {
        return ResponseEntity.ok(classService.deleteTeacherFromClass(classId, teacherId));
    }

    @DeleteMapping("/classes/{classId}/students/{studentId}")
    public ResponseEntity<Long> deleteStudentFromClass(@PathVariable Long classId, @PathVariable Long studentId) {
        return ResponseEntity.ok(classService.deleteStudentFromClass(classId, studentId));
    }

    @PutMapping("/classes/{classId}/teachers/{teacherId}")
    public ResponseEntity<AddTeacherRequestDto.TeacherDto> changeRoleOfTeacher(@PathVariable Long classId,
                                                                               @PathVariable Long teacherId,
                                                                               @Valid @RequestBody AddTeacherRequestDto.TeacherDto dto) {
        return ResponseEntity.ok(classService.changeRoleOfTeacher(classId, teacherId, dto));
    }

    @GetMapping("/classes/{classId}/teachers")
    public ResponseEntity<MemberPagedDto> getTeachers(@PathVariable Long classId) {
        return ResponseEntity.ok(classService.getTeachers(classId));
    }

    @ApiResponses(
            value = @ApiResponse(code = 400, message = "Exception code: CLASS_NOT_FOUND")
    )
    @GetMapping("/classes/{classId}/teachers/candidates")
    public ResponseEntity<TeacherCandidatesResponse> getTeacherCandidates(@PathVariable Long classId) {
        var candidates = classService.getTeacherCandidates(classId);
        var response = new TeacherCandidatesResponse();
        response.setStaffs(candidates);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/classes/{classId}/students")
    public ResponseEntity<MemberPagedDto> getStudents(@PathVariable Long classId,
                                                      @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                      @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                      @RequestParam(value = "filter", required = false) String filter,
                                                      @RequestParam(value = "keyword", required = false) String keyword,
                                                      @RequestParam(value = "sort", required = false) String sort) {
        return ResponseEntity.ok(classService.getStudents(classId, searchCriteriaUtils.buildSearchCriteria(keyword, filter, sort, page, size)));
    }

    @GetMapping("/classes/{classId}/members")
    public ResponseEntity<MemberPagedDto> getMembers(@PathVariable Long classId,
                                                     @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                     @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                     @RequestParam(value = "filter", required = false) String filter,
                                                     @RequestParam(value = "keyword", required = false) String keyword,
                                                     @RequestParam(value = "sort", required = false) String sort) {
        return ResponseEntity.ok(classService.getMembers(classId, searchCriteriaUtils.buildSearchCriteria(keyword, filter, sort, page, size)));
    }

    @PostMapping("/classes/{classId}/learning_content/chapters/{chapterId}/units")
    public ResponseEntity<UnitDto> createUnit(@PathVariable Long classId,
                                              @PathVariable Long chapterId,
                                              @Valid @RequestBody UnitDto dto) {
        return ResponseEntity.ok(classService.createUnit(classId, chapterId, dto));
    }

    @GetMapping("/classes/{classId}/learning_content/units/{unitId}")
    public ResponseEntity<UnitDto> getUnit(@PathVariable Long classId,
                                           @PathVariable Long unitId) {
        return ResponseEntity.ok(classService.getUnit(unitId));
    }

    @PutMapping("/classes/{classId}/learning_content/units/{unitId}")
    public ResponseEntity<UnitDto> updateUnit(@PathVariable Long classId,
                                              @PathVariable Long unitId,
                                              @Valid @RequestBody UnitDto dto) {
        return ResponseEntity.ok(classService.updateUnit(unitId, dto));
    }

    @PostMapping("/classes/{classId}/learning_content/units/{unitId}/publish")
    public ResponseEntity<UnitDto> publishUnit(@PathVariable Long classId,
                                               @PathVariable Long unitId) {
        return ResponseEntity.ok(classService.publishUnit(unitId));
    }

    @PostMapping("/classes/{classId}/learning_content/units/{unitId}/hide")
    public ResponseEntity<UnitDto> hideUnit(@PathVariable Long classId,
                                            @PathVariable Long unitId) {
        return ResponseEntity.ok(classService.hideUnit(unitId));
    }

    @GetMapping("/classes/{classId}/learning_content/units")
    public ResponseEntity<List<UnitDto>> getUnitsByClass(@PathVariable Long classId) {
        return ResponseEntity.ok(classService.getUnitsByClass(classId));
    }

    @GetMapping("/classes/{classId}/learning_content/units/textbooks")
    public ResponseEntity<List<UnitByTextbookDto>> getUnitsGroupByTextbook(@PathVariable Long classId) {
        return ResponseEntity.ok(classService.getUnitsGroupByTextbook(classId));
    }

    @DeleteMapping("/classes/{classId}/learning_content/units/{unitId}")
    public ResponseEntity<Long> deleteUnit(@PathVariable Long classId,
                                           @PathVariable Long unitId) {
        return ResponseEntity.ok(classService.deleteUnit(unitId));
    }

    @PostMapping("/classes/{classId}/learning_content/chapters/{chapterId}/quizzes")
    public ResponseEntity<QuizDto> createQuiz(@PathVariable Long classId,
                                              @PathVariable Long chapterId,
                                              @Valid @RequestBody QuizDto dto) {
        return ResponseEntity.ok(classService.createQuiz(classId, chapterId, dto));
    }

    @GetMapping("/classes/{classId}/learning_content/quizzes")
    public ResponseEntity<List<QuizDto>> getQuizzesByClass(@PathVariable Long classId) {
        return ResponseEntity.ok(classService.getQuizzes(classId));
    }

    @GetMapping("/classes/{classId}/learning_content/quizzes/{quizId}")
    public ResponseEntity<QuizDto> getQuiz(@PathVariable Long classId,
                                           @PathVariable Long quizId) {
        return ResponseEntity.ok(classService.getQuiz(quizId));
    }

    @GetMapping("/quiz/{quizId}/exam")
    public ResponseEntity<Long> getExamIdOfQuiz(@PathVariable Long quizId) {
        return ResponseEntity.ok(classService.getExamIdOfQuiz(quizId));
    }

    @PutMapping("/classes/{classId}/learning_content/quizzes/{quizId}")
    public ResponseEntity<QuizDto> updateQuiz(@PathVariable Long classId,
                                              @PathVariable Long quizId,
                                              @Valid @RequestBody QuizDto dto) {
        return ResponseEntity.ok(classService.updateQuiz(quizId, dto));
    }

    @PostMapping("/classes/{classId}/learning_content/quizzes/{quizId}/publish")
    public ResponseEntity<QuizEntryDto> publishQuiz(@PathVariable Long classId,
                                                    @PathVariable Long quizId) {
        return ResponseEntity.ok(classService.publishQuiz(classId, quizId));
    }

    @PostMapping("/classes/{classId}/learning_content/quizzes/{quizId}/hide")
    public ResponseEntity<QuizEntryDto> hideQuiz(@PathVariable Long classId,
                                                 @PathVariable Long quizId) {
        return ResponseEntity.ok(classService.hideQuiz(classId, quizId));
    }

    @PostMapping("/classes/{classId}/learning_content/quizzes/{quizId}/config")
    public ResponseEntity<QuizEntryDto> createOrUpdateQuizConfig(@PathVariable Long classId,
                                                                 @PathVariable Long quizId,
                                                                 @RequestBody QuizConfigDto dto) {

        return ResponseEntity.ok(classService.createOrUpdateQuizConfig(classId, quizId, dto));
    }

    @DeleteMapping("/classes/{classId}/learning_content/quizzes/{quizId}")
    public ResponseEntity<Long> deleteQuiz(@PathVariable Long classId,
                                           @PathVariable Long quizId) {
        return ResponseEntity.ok(classService.deleteQuiz(quizId));
    }

    @PutMapping("classes/{classId}/learning_content/reorder/actions")
    public ResponseEntity<List<ChapterDto>> reorderActions(@PathVariable Long classId,
                                                           @Valid @RequestBody ReorderActivityDto dto) {
        return ResponseEntity.ok(classService.reorderActivity(classId, dto));
    }

    @PutMapping("/classes/{classId}/learning_content/reorder/chapters")
    public ResponseEntity<List<ChapterDto>> reorderChapters(@PathVariable Long classId, @Valid @RequestBody ReorderChapterDto dto) {
        return ResponseEntity.ok(classService.reorderChapters(classId, dto));
    }

    // TODO: clarify path? why it's teacher
    @GetMapping("/classes/{classId}/scheduler/teacher/config")
    public ResponseEntity<ClassSchedulerConfig> getConfiguredSessions(@PathVariable Long classId) {
        return ResponseEntity.ok(classService.getConfiguredSessions(classId));
    }

    @GetMapping("/classes/{classId}/scheduler/student")
    public ResponseEntity<List<ClassSessionResponseDto>> getClassSessionsForStudent(@PathVariable Long classId) {
        return ResponseEntity.ok(classService.getClassSessionsForStudent(classId));
    }

    @GetMapping("/classes/{classId}/scheduler/teacher/not_configured")
    public ResponseEntity<List<ClassSessionResponseDto>> getClassSessionsNotConfigured(@PathVariable Long classId) {
        return ResponseEntity.ok(classService.getClassSessionsNotConfigured(classId));
    }

    @GetMapping("/classes/{classId}/scheduler/teacher/configured")
    public ResponseEntity<List<ClassSessionResponseDto>> getClassSessionsConfigured(@PathVariable Long classId) {
        return ResponseEntity.ok(classService.getClassSessionsConfigured(classId));
    }

    @GetMapping("/classes/{classId}/scheduler/session")
    public ResponseEntity<ClassSessionResponseDtoWrapper> getClassSessions(@PathVariable Long classId,
                                                                           @RequestParam("dateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
                                                                           @RequestParam("previous") Integer previous,
                                                                           @RequestParam("after") Integer after) {
        return ResponseEntity.ok(classService.getClassSessions(classId, dateTime, previous, after));
    }

    @GetMapping("/classes/{classId}/scheduler/session/upComing")
    public ResponseEntity<ClassSessionResponseDtoWrapper> getUpComingSessions(@PathVariable Long classId,
                                                                              @RequestParam("previous") Integer previous,
                                                                              @RequestParam("after") Integer after) {
        return ResponseEntity.ok(classService.getClassSessions(classId, LocalDateTime.now(), previous, after));
    }

    @PostMapping("/classes/{classId}/scheduler/config")
    public ResponseEntity<List<ClassSessionRequestDto>> configScheduler(@PathVariable Long classId, @Valid @RequestBody ClassSchedulerConfig dto) {
        return ResponseEntity.ok(classService.configScheduler(classId, dto));
    }

    @ApiResponses(
            value = @ApiResponse(code = 400, message = "Exception code: CLASS_NOT_FOUND, FINISHED_TIME_NOT_VALID, TEACHER_NOT_FOUND_IN_CLASS")
    )
    @PostMapping("/classes/{classId}/scheduler/session")
    public ResponseEntity<ClassSessionRequestDto> createSession(@PathVariable Long classId, @Valid @RequestBody ClassSessionRequestDto dto) {
        return ResponseEntity.ok(classService.createSession(classId, dto));
    }

    @PutMapping("/classes/{classId}/scheduler/session/{sessionId}")
    public ResponseEntity<ClassSessionRequestDto> updateSession(@PathVariable Long classId,
                                                                @PathVariable Long sessionId,
                                                                @Valid @RequestBody ClassSessionRequestDto dto) {
        return ResponseEntity.ok(classService.updateSession(classId, sessionId, dto));
    }

    @DeleteMapping("/classes/{classId}/scheduler/session/{sessionId}")
    public ResponseEntity<Long> deleteSession(@PathVariable Long classId,
                                              @PathVariable Long sessionId) {
        return ResponseEntity.ok(classService.deleteSession(classId, sessionId));
    }

    @DeleteMapping("/classes/{classId}")
    public ResponseEntity<Long> deleteClass(@PathVariable Long classId) {
        return ResponseEntity.ok(classService.deleteClass(classId));
    }

    @GetMapping("/classes/{classId}/resources/textbooks")
    public ResponseEntity<List<TextbookDto>> getTextbooksInClass(@PathVariable Long classId,
                                                                 @RequestParam(name = "keyword") String keyword) {
        return ResponseEntity.ok(classService.getTextbookInClass(classId, keyword));
    }

    @GetMapping("/classes/{classId}/resources/textbooks/count")
    public ResponseEntity<Long> countTextbooks(@PathVariable Long classId) {
        return ResponseEntity.ok(classService.countTextbookOfCourse(classId));
    }

    @GetMapping("/classes/{classId}/grade_tag")
    public ResponseEntity<List<GradeTagDto>> getAllGradeTagOfClass(@PathVariable Long classId) {
        return ResponseEntity.ok(classService.getAllGradeTagOfClass(classId));
    }
}
