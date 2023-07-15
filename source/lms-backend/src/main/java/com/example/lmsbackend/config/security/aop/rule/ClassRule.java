package com.example.lmsbackend.config.security.aop.rule;

import com.example.lmsbackend.config.security.CustomUserDetails;
import com.example.lmsbackend.config.security.aop.PermissionSecurity;
import com.example.lmsbackend.constant.LimitValue;
import com.example.lmsbackend.enums.AccountTypeEnum;
import com.example.lmsbackend.enums.ResourceType;
import com.example.lmsbackend.repository.ClassStudentRepository;
import com.example.lmsbackend.repository.ClassTeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClassRule implements Rule {

    private final ClassTeacherRepository classTeacherRepository;
    private final ClassStudentRepository classStudentRepository;

    @Override
    public ResourceType getResourceType() {
        return ResourceType.CLASS;
    }

    @Override
    public boolean authorize(CustomUserDetails currUser, PermissionSecurity permission, long classId) {
        if (permission.getIsLimitByLearn() == LimitValue.LIMIT && AccountTypeEnum.STUDENT == currUser.getAccountType()) {
            return classStudentRepository.isStudentInClass(currUser.getId(), classId);
        }
        if (permission.getIsLimitByTeaching() == LimitValue.LIMIT && AccountTypeEnum.STAFF == currUser.getAccountType()) {
            return classTeacherRepository.isTeacherInClass(classId, currUser.getId());
        }
        return true;
    }
}
