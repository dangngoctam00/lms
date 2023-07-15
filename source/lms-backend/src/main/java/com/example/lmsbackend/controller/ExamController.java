package com.example.lmsbackend.controller;

import com.example.lmsbackend.criteria.BaseSearchCriteria;
import com.example.lmsbackend.dto.exam.*;
import com.example.lmsbackend.dto.exam.writing.UpdatePointDto;
import com.example.lmsbackend.service.exam.ExamService;
import com.example.lmsbackend.utils.SearchCriteriaUtils;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.example.lmsbackend.constant.AppConstant.API_PREFIX;

@Slf4j
@RestController
@RequestMapping(API_PREFIX)
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;
    private final SearchCriteriaUtils searchCriteriaUtils;

    @PostMapping("/course/{courseId}/exam")
    public ResponseEntity<ExamDto> createExam(@PathVariable Long courseId,
                                              @RequestBody CreateExamRequestDto dto) {
        return ResponseEntity.ok(examService.createExamContent(courseId, dto));
    }

    @GetMapping("/course/{courseId}/exam/{examId}")
    public ResponseEntity<ExamDto> getExam(@PathVariable Long courseId,
                                           @PathVariable Long examId,
                                           @RequestParam(value = "page", defaultValue = "1") Integer page,
                                           @RequestParam(value = "size", defaultValue = "5") Integer size) {
        return ResponseEntity.ok(examService.getExamContent(examId, page, size));
    }

    @PatchMapping("/course/{courseId}/exam/{examId}")
    public ResponseEntity<ExamDto> updateExam(@PathVariable Long courseId, @PathVariable Long examId, @RequestBody UpdateExamRequestDto dto) {
        return ResponseEntity.ok(examService.updateExam(examId, dto));
    }

    @DeleteMapping("/exam/{examId}")
    public ResponseEntity<Long> deleteExam(@PathVariable Long examId) {
        return ResponseEntity.ok(examService.deleteExam(examId));
    }

    @DeleteMapping("/exam/{examId}/question/{questionId}")
    public ResponseEntity<Long> deleteQuestion(@PathVariable Long examId, @PathVariable Long questionId) {
        return ResponseEntity.ok(examService.deleteQuestion(examId, questionId));
    }

    @GetMapping("/course/{courseId}/exam/{examId}/question")
    public ResponseEntity<QuestionPagedResponseDto> getQuestionsByExam(@PathVariable Long courseId,
                                                                       @PathVariable Long examId,
                                                                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                       @RequestParam(value = "size", defaultValue = "1") Integer size) {
        return ResponseEntity.ok(examService.getQuestionsByExam(examId, page, size));
    }

    @GetMapping("/exam/{examId}/question/{questionId}")
    public ResponseEntity<QuestionDto> getQuestionsInExamById(@PathVariable Long examId, @PathVariable Long questionId) {
        // TODO: return order in exam
        return ResponseEntity.ok(examService.getQuestionById(examId, questionId));
    }

    @GetMapping("/course/{courseId}/exam")
    public ResponseEntity<ExamContentPagedDto> getExamsByCourse(@PathVariable Long courseId,
                                                                @RequestParam(value = "keyword", required = false) String keyword,
                                                                @RequestParam(value = "filter", required = false) String filter,
                                                                @RequestParam(value = "sort", required = false) String sort,
                                                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                @RequestParam(value = "size", defaultValue = "5") Integer size) {
        return ResponseEntity.ok(examService.getExamsByCourse(courseId, searchCriteriaUtils.buildSearchCriteria(keyword, filter, sort, page, size)));
    }

    @GetMapping("/course/{courseId}/exam/search")
    public ResponseEntity<ExamContentPagedDto> getAllExamsByCourse(@PathVariable Long courseId,
                                                                   @RequestParam(value = "keyword", required = false) String keyword) {
        return ResponseEntity.ok(examService.searchExamsByCourse(courseId, keyword));
    }

    @GetMapping("/exam")
    public ResponseEntity<ExamContentPagedDto> getExams(@RequestParam(value = "keyword", required = false) String keyword,
                                                        @RequestParam(value = "filter", required = false) String filter,
                                                        @RequestParam(value = "sort", required = false) String sort,
                                                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                        @RequestParam(value = "size", defaultValue = "5") Integer size) {
        return ResponseEntity.ok(examService.getExams(searchCriteriaUtils.buildSearchCriteria(keyword, filter, sort, page, size)));
    }

    @PostMapping("/course/{courseId}/exam/{examId}/question")
    public ResponseEntity<QuestionDto> createQuestion(@PathVariable Long courseId,
                                                      @PathVariable Long examId,
                                                      @RequestBody QuestionDto dto) {
        return ResponseEntity.ok(examService.createQuestion(examId, dto));
    }

    @PostMapping("/course/{courseId}/exam/{examId}/group_question")
    public ResponseEntity<QuestionDto> createGroupQuestion(@PathVariable Long courseId,
                                                           @PathVariable Long examId,
                                                           @RequestBody QuestionDto dto) {
        return ResponseEntity.ok(examService.createQuestionGroup(examId, dto));
    }

    @PostMapping("/course/{courseId}/exam/{examId}/group_question/{groupId}")
    public ResponseEntity<QuestionDto> createGroupQuestionInGroup(@PathVariable Long courseId,
                                                                  @PathVariable Long examId,
                                                                  @PathVariable Long groupId,
                                                                  @Valid @RequestBody QuestionDto dto) {
        return ResponseEntity.ok(examService.createQuestionInGroup(examId, groupId, dto));
    }

    @PutMapping("/exam/{examId}/question/{questionId}")
    public ResponseEntity<Long> updateQuestion(@PathVariable Long examId,
                                               @PathVariable Long questionId,
                                               @RequestBody QuestionDto dto) {
        return ResponseEntity.ok(examService.updateQuestion(examId, questionId, dto));
    }

    @PutMapping("/exam/{examId}/group_question/{groupId}/question/{questionId}")
    public ResponseEntity<Long> updateQuestionInGroup(@PathVariable Long examId,
                                                      @PathVariable Long groupId,
                                                      @PathVariable Long questionId,
                                                      @RequestBody QuestionDto dto) {
        return ResponseEntity.ok(examService.updateQuestion(examId, questionId, dto));
    }


    /**
     * Get exam information and config when student click into quiz in class.
     */
    @GetMapping("/class/{classId}/quiz/{quizId}")
    public ResponseEntity<QuizEntryDto> getQuizDetail(@PathVariable Long classId,
                                                      @PathVariable Long quizId) {
        return ResponseEntity.ok(examService.getQuizDetail(classId, quizId));
    }

    @GetMapping("/class/{classId}/quiz/{quizId}/session/{sessionId}")
    public ResponseEntity<QuizSessionResponseDto> getQuizSession(@PathVariable Long classId,
                                                                 @PathVariable String sessionId,
                                                                 @PathVariable Long quizId,
                                                                 @RequestParam(value = "question", required = false) Long question,
                                                                 @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                 @RequestParam(value = "size", defaultValue = "5") Integer size) {
        return ResponseEntity.ok(examService.getQuizSession(sessionId, question, page, size));
    }

    /**
     * This endpoint is used to build navigation question when a student taking quiz
     */
    @GetMapping("/exam/{examId}/session/{sessionId}")
    public ResponseEntity<List<QuestionInExamByTypeAndIdDto>> getAllQuestionsIdAndTypeWhileTakingQuiz(@PathVariable String sessionId, @PathVariable Long examId) {
        return ResponseEntity.ok(examService.getAllQuestionsIdAndTypeWhileTakingQuiz(sessionId, examId));
    }

    @GetMapping("/class/{classId}/quiz/{quizId}/session/{sessionId}/result")
    public ResponseEntity<SessionResultDto> getQuizSessionResult(@PathVariable Long quizId,
                                                                 @PathVariable Long classId,
                                                                 @PathVariable String sessionId,
                                                                 @RequestParam(value = "question", required = false) Long question,
                                                                 @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                 @RequestParam(value = "size", defaultValue = "5") Integer size) {
        return ResponseEntity.ok(examService.getQuizSessionResult(sessionId, question, page, size));
    }

    @PostMapping("/session/{sessionId}/question/{questionId}/flag")
    public ResponseEntity<?> flagQuestion(@PathVariable String sessionId, @PathVariable Long questionId) {
        examService.flagQuestion(sessionId, questionId);
        return ResponseEntity.status(200).build();
    }

    @PostMapping("/session/{sessionId}/question/{questionId}/unFlag")
    public ResponseEntity<?> unFlagQuestion(@PathVariable String sessionId, @PathVariable Long questionId) {
        examService.unFlagQuestion(sessionId, questionId);
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/course/{courseId}/class/{classId}/quiz/{quizId}/question")
    public ResponseEntity<QuizEntryDto> getQuizAndQuestionsById(@PathVariable Long courseId,
                                                                @PathVariable Long classId,
                                                                @PathVariable Long quizId,
                                                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                @RequestParam(value = "size", defaultValue = "5") Integer size) {
        return ResponseEntity.ok(examService.getQuizAndQuestionsById(classId, quizId, page, size));
    }


    @PostMapping("/class/{classId}/quiz/{quizId}/session/init")
    public ResponseEntity<QuizSessionDto> initExamSession(@PathVariable("classId") Long classId,
                                                          @PathVariable("quizId") Long quizId,
                                                          @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                          @RequestParam(value = "size", defaultValue = "1") Integer size) {
        return ResponseEntity.ok(examService.initExamSession(quizId, classId, page, size));
    }

    @PostMapping("/course/{courseId}/class/{classId}/quiz/{quizId}/session/{sessionId}/question/{questionId}")
    public ResponseEntity<?> answerQuestion(@PathVariable Long courseId,
                                                           @PathVariable Long classId,
                                                           @PathVariable Long quizId,
                                                           @PathVariable String sessionId,
                                                           @PathVariable Long questionId,
                                                           @Valid @RequestBody AnswerSessionDto answer) {
        examService.answerQuestion(sessionId, questionId, answer);
        return ResponseEntity.ok().build();
    }


    /**
     * Update point for question manually (WRITING in this case)
     */
    @PostMapping("/course/{courseId}/class/{classId}/quiz/{quizId}/session/{sessionId}/question/{questionId}/point")
    public ResponseEntity<?> updatePointForQuestion(@PathVariable Long courseId,
                                                    @PathVariable Long classId,
                                                    @PathVariable Long quizId,
                                                    @PathVariable String sessionId,
                                                    @PathVariable Long questionId,
                                                    @RequestBody UpdatePointDto dto) {
        examService.gradeQuestion(quizId, sessionId, questionId, dto);
        return ResponseEntity.ok().build();
    }

    @ApiResponses(
            value = @ApiResponse(code = 400, message = "Exception code: QUIZ_SESSION_NOT_FOUND, QUIZ_SESSION_ENDED")
    )
    @PostMapping("/class/{classId}/quiz/{quizId}/session/{sessionId}/end")
    public ResponseEntity<String> endQuizSession(@PathVariable Long classId,
                                                 @PathVariable Long quizId,
                                                 @PathVariable String sessionId) {
        examService.endQuizSession(sessionId);
        return ResponseEntity.ok().build();
    }

    /**
     * Get list overview result of 1 quiz for one student or class if student is left empty
     */
    // UPDATE PATH
    @GetMapping("/class/{classId}/quiz/{quizId}/session")
    public ResponseEntity<SessionResultPagedDto> getSessionResultOverviewByUserAndExam(@PathVariable Long classId,
                                                                                       @PathVariable Long quizId,
                                                                                       @RequestParam(value = "user", required = false) Long userId) {
        return ResponseEntity.ok(examService.getSessionResultOverview(quizId, userId, new BaseSearchCriteria()));
    }

    @GetMapping("/class/{classId}/quiz/{quizId}/session/student")
    public ResponseEntity<SessionResultPagedDto> getSessionsResultForStudent(@PathVariable Long classId,
                                                                             @PathVariable Long quizId) {
        return ResponseEntity.ok(examService.getSessionsResultForStudent(quizId));
    }

    @GetMapping("/quiz/{quizId}/session_result/management")
    public ResponseEntity<TeacherQuizResultDto> getQuizResultManagement(@PathVariable Long quizId,
                                                                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                        @RequestParam(value = "size", defaultValue = "5") Integer size,
                                                                        @RequestParam(value = "keyword", required = false) String keyword,
                                                                        @RequestParam(value = "sort", required = false) String sort,
                                                                        @RequestParam(value = "filter", required = false) String filter) {
        return ResponseEntity.ok(examService.getQuizResultManagement(quizId, searchCriteriaUtils.buildSearchCriteria(keyword, filter, sort, page, size)));
    }


    @PostMapping("/course/{courseId}/class/{classId}/quiz/{quizId}/result")
    public ResponseEntity<Long> processAndSaveQuizResultById(@PathVariable Long courseId,
                                                             @PathVariable Long classId,
                                                             @PathVariable Long quizId) {
        return ResponseEntity.ok(examService.processAndSaveQuizResultById(classId, quizId));
    }

    // TODO
    @GetMapping("/course/{courseId}/class/{classId}/quiz/{quizId}/result")
    public ResponseEntity<String> getQuizResultById(@PathVariable Long courseId,
                                                    @PathVariable Long classId,
                                                    @PathVariable Long quizId) {
        return ResponseEntity.ok(examService.getQuizResultById(quizId));
    }

    @GetMapping("quizzes/{quizId}/statistic")
    public ResponseEntity<List<QuestionStatistic>> getStatisticsQuiz(@PathVariable Long quizId) {
        return ResponseEntity.ok(examService.statisticQuiz(quizId));
    }

    @GetMapping(value = "/statistic_quiz/{quizId}", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> statisticsQuiz(@PathVariable Long quizId) throws JRException {
        return ResponseEntity.ok().body(examService.statisticsQuiz(quizId));
    }


    // For testing purpose
    @GetMapping("/exam/test")
    public ResponseEntity<String> testSearchQuestionByExam(@RequestParam("examId") Long examId,
                                                           @RequestParam("keyword") String keyword,
                                                           @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                           @RequestParam(value = "size", defaultValue = "5") Integer size) {
        examService.testSearchQuestionByExam(examId, keyword, page, size);
        return ResponseEntity.ok("Hmm :)");
    }

    @GetMapping("/exam/questionList")
    public ResponseEntity<String> testGetQuestionsJoinTable() {
        examService.testGetQuestionJoinTable();
        return ResponseEntity.ok("Hmm");
    }
}
