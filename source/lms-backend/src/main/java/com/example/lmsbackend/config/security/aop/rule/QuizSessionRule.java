package com.example.lmsbackend.config.security.aop.rule;

import com.example.lmsbackend.config.security.CustomUserDetails;
import com.example.lmsbackend.config.security.aop.PermissionSecurity;
import com.example.lmsbackend.constant.LimitValue;
import com.example.lmsbackend.enums.AccountTypeEnum;
import com.example.lmsbackend.enums.ResourceType;
import com.example.lmsbackend.repository.ClassStudentRepository;
import com.example.lmsbackend.repository.ClassTeacherRepository;
import com.example.lmsbackend.repository.QuizSessionRepository;
import com.example.lmsbackend.service.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class QuizSessionRule implements Rule {

    private final QuizSessionRepository quizSessionRepository;
    private final ClassService classService;
    private final ClassTeacherRepository classTeacherRepository;
    private final ClassStudentRepository classStudentRepository;

    @Override
    public ResourceType getResourceType() {
        return ResourceType.QUIZ_SESSION;
    }

    @Override
    public boolean authorize(CustomUserDetails currUser, PermissionSecurity permission, String resourceId) {
        var sessionId = UUID.fromString(resourceId);
        var classId = classService.getClassIdFromQuizSession(sessionId);
        if (permission.getIsLimitByOwner() == LimitValue.LIMIT) {
            return quizSessionRepository.isSessionCreatedByUser(sessionId, currUser.getId());
        }
        if (permission.getIsLimitByLearn() == LimitValue.LIMIT && AccountTypeEnum.STUDENT == currUser.getAccountType()) {
            return classStudentRepository.isStudentInClass(currUser.getId(), classId);
        }
        if (permission.getIsLimitByTeaching() == LimitValue.LIMIT && AccountTypeEnum.STAFF == currUser.getAccountType()) {
            return classTeacherRepository.isTeacherInClass(classId, currUser.getId());
        }
        return true;
    }
}
