package lms.quiz.service;

import exception.ResourceNotFound;
import lms.quiz.domain.ExamEntity;
import lms.quiz.domain.QuizQuestionEntity;
import lms.quiz.dto.ExamDto;
import lms.quiz.enums.DraftContext;
import lms.quiz.mapper.ExamMapper;
import lms.quiz.mapper.QuestionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ExamQuestionService {

    private final QuizQuestionService questionService;
    private final ExamService examService;
    private final QuestionMapper questionMapper;
    private final ExamMapper examMapper;
    private final DraftService draftService;

    public ExamDto getExamAndQuestions(Long examId) {
        ExamEntity exam = examService.getById(examId);
        ExamDto examDto = examMapper.mapToExamDto(exam);
        examDto.setQuestions(questionService.getQuestionsByExam(examId)
                .stream()
                .map(questionMapper::mapToQuestionDto)
                .collect(Collectors.toList()));
        return examDto;
    }

    public ExamDto getPublishedExamById(Long id) {
        Optional<Integer> revisionNumber = draftService.getRevisionNumber(DraftContext.EXAM, id);
        ExamEntity exam;
        if (revisionNumber.isPresent()) {
            exam = examService.getByIdAndRevision(id, revisionNumber.get())
                    .orElseThrow(ResourceNotFound::new);
            List<QuizQuestionEntity> questions = questionService.getQuestionsByExamAndRevision(id, revisionNumber.get());
            ExamDto examDto = examMapper.mapToExamDto(exam);
            examDto.setQuestions(questions.stream()
                            .map(questionMapper::mapToQuestionDto)
                            .collect(Collectors.toList()));
            return examDto;
        } else {
            exam = examService.getById(id);
        }
        ExamDto examDto = examMapper.mapToExamDto(exam);
        examDto.setQuestions(getQuestions(revisionNumber, id).stream()
                .map(questionMapper::mapToQuestionDto)
                .collect(Collectors.toList()));
        return examDto;
    }

    private List<QuizQuestionEntity> getQuestions(Optional<Integer> revisionOpt, Long examId) {
        if (revisionOpt.isPresent()) {
            return questionService.getQuestionsByExamAndRevision(examId, revisionOpt.get());
        }
        return questionService.getQuestionsByExam(examId);
    }
}
