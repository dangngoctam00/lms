package lms.quiz.controller;

import dto.LmsApiResponse;
import lms.quiz.dto.ExamDto;
import lms.quiz.mapper.ExamMapper;
import lms.quiz.service.ExamQuestionService;
import lms.quiz.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;
    private final ExamMapper examMapper;
    private final ExamQuestionService examQuestionService;

    @PostMapping
    LmsApiResponse<Void> createExam(@RequestBody ExamDto request) {
        examService.createExam(examMapper.mapToExamEntity(request));
        return LmsApiResponse.ok();
    }

    @GetMapping("/{id}")
    LmsApiResponse<ExamDto> getById(@PathVariable Long id) {
        return LmsApiResponse.ok(examQuestionService.getExamAndQuestions(id));
    }

    @GetMapping("/{id}/published")
    LmsApiResponse<ExamDto> getPublishedById(@PathVariable Long id) {
        return LmsApiResponse.ok(examQuestionService.getPublishedExamById(id));
    }

    @PostMapping("/{id}/draft")
    LmsApiResponse<Void> createDraft(@PathVariable Long id) {
        examService.createDraft(id);
        return LmsApiResponse.ok();
    }
}
