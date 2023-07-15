package com.example.lmsbackend.config.security.aop.rule;

import com.example.lmsbackend.config.security.CustomUserDetails;
import com.example.lmsbackend.config.security.aop.PermissionSecurity;
import com.example.lmsbackend.constant.LimitValue;
import com.example.lmsbackend.enums.AccountTypeEnum;
import com.example.lmsbackend.enums.ResourceType;
import com.example.lmsbackend.repository.ClassTeacherRepository;
import com.example.lmsbackend.repository.ExamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExamRule implements Rule {

    private final ExamRepository examRepository;
    private final ClassTeacherRepository classTeacherRepository;

    @Override
    public ResourceType getResourceType() {
        return ResourceType.EXAM;
    }

    @Override
    public boolean authorize(CustomUserDetails currUser, PermissionSecurity permission, long resourceId) {
        if (permission.getIsLimitByOwner() == LimitValue.LIMIT) {
            return examRepository.isCreatedByUser(resourceId, currUser.getUsername());
        }
        if (permission.getIsLimitByTeaching() == LimitValue.LIMIT && AccountTypeEnum.STAFF == currUser.getAccountType()) {
            return classTeacherRepository.isTeacherInClassByExam(resourceId, currUser.getId());
        }
        return true;
    }
}
