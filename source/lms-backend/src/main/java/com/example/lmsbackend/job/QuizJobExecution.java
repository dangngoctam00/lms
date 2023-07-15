package com.example.lmsbackend.job;

import com.example.lmsbackend.multitenancy.utils.TenantContext;
import com.example.lmsbackend.repository.QuizSessionRepository;
import com.example.lmsbackend.service.exam.ExamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class QuizJobExecution {

    private QuizSessionRepository quizSessionRepository;
    private ExamService examService;

    @Autowired
    public QuizJobExecution(QuizSessionRepository quizSessionRepository, @Lazy ExamService examService) {
        this.quizSessionRepository = quizSessionRepository;
        this.examService = examService;
    }

    public void endExamSession(UUID sessionId, String tenantId, Long userId) {
        TenantContext.setTenantId(tenantId);
        var sessionOpt = quizSessionRepository.findById(sessionId);
        if (sessionOpt.isPresent()) {
            if (sessionOpt.get().getSubmittedAt() == null) {
                examService.endQuizSession(sessionId.toString(), userId);
                log.debug("The job execute end quiz session {} successfully", sessionId);
            }
        } else {
            log.warn("Session {} is not found", sessionId);
        }
    }
}
