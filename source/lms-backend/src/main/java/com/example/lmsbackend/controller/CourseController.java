package com.example.lmsbackend.controller;

import com.example.lmsbackend.dto.classes.ClassDto;
import com.example.lmsbackend.dto.classes.ClassPagedDto;
import com.example.lmsbackend.dto.request.course.CourseDto;
import com.example.lmsbackend.dto.request.course.ReorderActivityDto;
import com.example.lmsbackend.dto.request.course.ReorderChapterDto;
import com.example.lmsbackend.dto.resource.TextbookDto;
import com.example.lmsbackend.dto.response.course.*;
import com.example.lmsbackend.dto.textbook.AddTextbookToCourseRequest;
import com.example.lmsbackend.service.CourseService;
import com.example.lmsbackend.utils.SearchCriteriaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.example.lmsbackend.constant.AppConstant.API_PREFIX;
import static com.example.lmsbackend.constant.ResponseMessage.CREATE_COURSE_SUCCESS;

@RestController
@RequestMapping(API_PREFIX)
@RequiredArgsConstructor
@Slf4j
public class CourseController {

    private final CourseService courseService;
    private final SearchCriteriaUtils searchCriteriaUtils;

    @PostMapping("/courses")
    public ResponseEntity<CreateCourseResponse> createCourse(@Valid @RequestBody CourseDto request) {
        courseService.createCourse(request);
        return ResponseEntity.ok(new CreateCourseResponse(CREATE_COURSE_SUCCESS));
    }

    @PutMapping("/courses/{id}")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseDto dto) {
        return ResponseEntity.ok(courseService.updateCourse(id, dto));
    }

    @PostMapping("/courses/{courseId}/learning_content/chapters")
    public ResponseEntity<ChapterDto> createChapter(@PathVariable Long courseId, @Valid @RequestBody ChapterDto dto) {
        return ResponseEntity.ok(courseService.createChapter(courseId, dto));
    }

    @PutMapping("/courses/{courseId}/learning_content/chapters/{chapterId}")
    public ResponseEntity<ChapterDto> updateChapter(@PathVariable Long courseId,
                                                    @PathVariable Long chapterId,
                                                    @Valid @RequestBody ChapterDto dto) {
        return ResponseEntity.ok(courseService.updateChapter(courseId, chapterId, dto));
    }

    @DeleteMapping("/courses/{courseId}/learning_content/chapters/{chapterId}")
    public ResponseEntity<Long> deleteChapter(@PathVariable Long courseId, @PathVariable Long chapterId) {
        return ResponseEntity.ok(courseService.deleteChapter(courseId, chapterId));
    }

    @GetMapping("/courses/{courseId}/learning_content/quizzes")
    public ResponseEntity<List<QuizDto>> getQuizzesByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getQuizzesByCourse(courseId));
    }

    @GetMapping("/courses/{courseId}/learning_content/units")
    public ResponseEntity<List<UnitDto>> getUnitsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getUnitsByCourse(courseId));
    }

    @GetMapping("/courses/{courseId}/learning_content/units/textbooks")
    public ResponseEntity<List<UnitByTextbookDto>> getUnitsGroupByTextbook(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getUnitsGroupByTextbook(courseId));
    }

    @PostMapping("/courses/{courseId}/learning_content/chapters/{chapterId}/units")
    public ResponseEntity<UnitDto> createUnit(@PathVariable Long courseId,
                                              @PathVariable Long chapterId,
                                              @Valid @RequestBody UnitDto dto) {
        return ResponseEntity.ok(courseService.createUnit(courseId, chapterId, dto));
    }

    @GetMapping("/courses/{courseId}/learning_content/units/{unitId}")
    public ResponseEntity<UnitDto> getUnit(@PathVariable Long courseId,
                                           @PathVariable Long unitId) {
        return ResponseEntity.ok(courseService.getUnit(courseId, unitId));
    }

    @PutMapping("/courses/{courseId}/learning_content/units/{unitId}")
    public ResponseEntity<UnitDto> updateUnit(@PathVariable Long courseId,
                                              @PathVariable Long unitId,
                                              @Valid @RequestBody UnitDto dto) {
        return ResponseEntity.ok(courseService.updateUnit(courseId, unitId, dto));
    }

    @DeleteMapping("/courses/{courseId}/learning_content/units/{unitId}")
    public ResponseEntity<Long> deleteUnit(@PathVariable Long courseId,
                                           @PathVariable Long unitId) {
        return ResponseEntity.ok(courseService.deleteUnit(courseId, unitId));
    }

    @PostMapping("/courses/{courseId}/learning_content/chapters/{chapterId}/quizzes")
    public ResponseEntity<QuizDto> createQuiz(@PathVariable Long courseId,
                                              @PathVariable Long chapterId,
                                              @Valid @RequestBody QuizDto dto) {
        return ResponseEntity.ok(courseService.createQuiz(courseId, chapterId, dto));
    }

    @GetMapping("/courses/{courseId}/learning_content/quizzes/{quizId}")
    public ResponseEntity<QuizDto> findQuiz(@PathVariable Long courseId,
                                            @PathVariable Long quizId) {
        return ResponseEntity.ok(courseService.findQuiz(courseId, quizId));
    }

    @PutMapping("/courses/{courseId}/learning_content/quizzes/{quizId}")
    public ResponseEntity<QuizDto> updateQuiz(@PathVariable Long courseId,
                                              @PathVariable Long quizId,
                                              @Valid @RequestBody QuizDto dto) {
        return ResponseEntity.ok(courseService.updateQuiz(courseId, quizId, dto));
    }

    @DeleteMapping("/courses/{courseId}/learning_content/quizzes/{quizId}")
    public ResponseEntity<Long> deleteQuiz(@PathVariable Long courseId,
                                           @PathVariable Long quizId) {
        return ResponseEntity.ok(courseService.deleteQuiz(courseId, quizId));
    }

    @GetMapping("/courses/{courseId}/learning_content")
    public ResponseEntity<List<ChapterDto>> getLearningContent(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getLearningContent(courseId));
    }

    @GetMapping("/courses/{courseId}/learning_content/textbooks")
    public ResponseEntity<List<ChapterGroupByTextbookDto>> getLearningContentGroupByTextbook(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getLearningContentGroupByTextbook(courseId));
    }

    @GetMapping("/courses")
    public ResponseEntity<CoursesPagedDto> findCourses(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                       @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                       @RequestParam(value = "keyword", defaultValue = "") String keyword,
                                                       @RequestParam(value = "sort", required = false) String sort) {
        return ResponseEntity.ok(courseService.findCourses(searchCriteriaUtils.buildSearchCriteria(keyword, null, sort, page, size)));
    }

    @GetMapping("/courses/search")
    public ResponseEntity<CoursesPagedDto> findCoursesSearch(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                       @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                       @RequestParam(value = "keyword", defaultValue = "") String keyword,
                                                       @RequestParam(value = "sort", required = false) String sort) {
        return ResponseEntity.ok(courseService.findCourses(searchCriteriaUtils.buildSearchCriteria(keyword, null, sort, 0, 0)));
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<CourseDto> findCourse(@PathVariable("id") Long id) {
        return ResponseEntity.ok(courseService.findCourseById(id));
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Long> deleteCourse(@PathVariable("id") Long id) {
        return ResponseEntity.ok(courseService.deleteCourse(id));
    }

    @GetMapping("/courses/{id}/ongoing_class")
    public ResponseEntity<List<ClassDto>> findOngoingClass(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.findOngoingClass(id));
    }

    @PutMapping("/courses/{courseId}/learning_content/reorder/chapters")
    public ResponseEntity<List<ChapterDto>> reorderChapters(@PathVariable Long courseId, @Valid @RequestBody ReorderChapterDto dto) {
        courseService.reorderChapters(courseId, dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/courses/{courseId}/learning_content/reorder/actions")
    public ResponseEntity<?> reorderActivity(@PathVariable Long courseId, @Valid @RequestBody ReorderActivityDto dto) {
        courseService.reorderActivity(courseId, dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/courses/{courseId}/resources/textbooks")
    public ResponseEntity<?> addTextbooksToCourse(@PathVariable Long courseId, @RequestBody AddTextbookToCourseRequest dto) {
        courseService.addTextbooksToCourse(courseId, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/courses/{courseId}/resources/textbooks/not")
    public ResponseEntity<List<TextbookDto>> getTextbooksNotInCourse(@PathVariable Long courseId, @RequestParam("keyword") String keyword) {
        return ResponseEntity.ok(courseService.getTextbooksNotInCourse(courseId, keyword));
    }

    @GetMapping("/courses/{courseId}/resources/textbooks")
    public ResponseEntity<List<TextbookDto>> getTextbooksInCourse(@PathVariable Long courseId, @RequestParam(name = "keyword") String keyword) {
        return ResponseEntity.ok(courseService.getTextbooksInCourse(courseId, keyword));
    }

    @DeleteMapping("/courses/{courseId}/resources/textbooks/{textbookId}")
    public ResponseEntity<?> deleteTextbookFromCourse(@PathVariable Long courseId, @PathVariable Long textbookId) {
        courseService.deleteTextbookFromCourse(courseId, textbookId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/courses/{courseId}/resources/textbooks/count")
    public ResponseEntity<Long> countTextbooks(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.countTextbookOfCourse(courseId));
    }

    @GetMapping("/courses/{courseId}/classes")
    public ResponseEntity<ClassPagedDto> getClasses(@PathVariable Long courseId,
                                                    @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                    @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                    @RequestParam(value = "keyword", required = false) String keyword,
                                                    @RequestParam(value = "filter", required = false) String filter,
                                                    @RequestParam(value = "sort", required = false) String sort) {
        return ResponseEntity.ok(courseService.getClasses(courseId, searchCriteriaUtils.buildSearchCriteria(keyword, filter, sort, page, size)));
    }
}
