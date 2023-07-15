package com.example.lmsbackend.config.security.aop.rule;

import com.example.lmsbackend.config.security.CustomUserDetails;
import com.example.lmsbackend.config.security.aop.PermissionSecurity;
import com.example.lmsbackend.constant.LimitValue;
import com.example.lmsbackend.enums.AccountTypeEnum;
import com.example.lmsbackend.enums.ResourceType;
import com.example.lmsbackend.repository.ClassStudentRepository;
import com.example.lmsbackend.repository.ClassTeacherRepository;
import com.example.lmsbackend.repository.QuizClassRepository;
import com.example.lmsbackend.service.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuizClassRule implements Rule {

    private final ClassService classService;
    private final ClassTeacherRepository classTeacherRepository;
    private final QuizClassRepository quizClassRepository;
    private final ClassStudentRepository classStudentRepository;

    @Override
    public ResourceType getResourceType() {
        return ResourceType.QUIZ_CLASS;
    }

    @Override
    public boolean authorize(CustomUserDetails currUser, PermissionSecurity permission, long quizClassId) {
        var classId = classService.getClassIdFromQuiz(quizClassId);
        if (permission.getIsLimitByOwner() == LimitValue.LIMIT) {
            return quizClassRepository.izQuizCreatedByUser(quizClassId, currUser.getUsername());
        }
        if (permission.getIsLimitByTeaching() == LimitValue.LIMIT && AccountTypeEnum.STAFF == currUser.getAccountType()) {
            return classTeacherRepository.isTeacherInClass(classId, currUser.getId());
        }
        if (permission.getIsLimitByLearn() == LimitValue.LIMIT && AccountTypeEnum.STUDENT == currUser.getAccountType()) {
            return classStudentRepository.isStudentInClass(currUser.getId(), classId);
        }
        return true;
    }
}
