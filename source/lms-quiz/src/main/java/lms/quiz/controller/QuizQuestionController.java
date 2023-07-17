package lms.quiz.controller;

import dto.LmsApiResponse;
import lms.quiz.dto.QuizQuestionDto;
import lms.quiz.mapper.QuestionMapper;
import lms.quiz.service.QuizQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz-questions")
@RequiredArgsConstructor
public class QuizQuestionController {

    private final QuizQuestionService questionService;
    private final QuestionMapper questionMapper;

    @PostMapping
    LmsApiResponse<Void> createQuestion(@RequestBody QuizQuestionDto request) {
        questionService.createQuestion(questionMapper.mapToQuestionEntity(request));
        return LmsApiResponse.ok();
    }

    @GetMapping("/{id}")
    LmsApiResponse<QuizQuestionDto> getById(@PathVariable Long id) {
        return LmsApiResponse.ok(questionMapper.mapToQuestionDto(questionService.getById(id)));
    }
}
