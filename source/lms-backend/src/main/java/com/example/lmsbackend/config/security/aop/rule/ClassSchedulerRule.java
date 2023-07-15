package com.example.lmsbackend.config.security.aop.rule;

import com.example.lmsbackend.config.security.CustomUserDetails;
import com.example.lmsbackend.config.security.aop.PermissionSecurity;
import com.example.lmsbackend.constant.LimitValue;
import com.example.lmsbackend.enums.ResourceType;
import com.example.lmsbackend.repository.ClassSessionRepository;
import com.example.lmsbackend.repository.ClassTeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClassSchedulerRule implements Rule {

    private final ClassSessionRepository classSessionRepository;
    private final ClassTeacherRepository classTeacherRepository;

    @Override
    public ResourceType getResourceType() {
        return ResourceType.CLASS_SESSION;
    }

    @Override
    public boolean authorize(CustomUserDetails currUser, PermissionSecurity permission, long resourceId) {
        if (permission.getIsLimitByOwner() == LimitValue.LIMIT) {
            return classSessionRepository.isSessionCreatedBy(resourceId, currUser.getUsername());
        }
        if (permission.getIsLimitByTeaching() == LimitValue.LIMIT) {
            return classTeacherRepository.isTeacherInClassByClassSession(resourceId, currUser.getId());
        }
        return true;
    }
}
