package com.example.lmsbackend.config.security.aop.rule;

import com.example.lmsbackend.config.security.CustomUserDetails;
import com.example.lmsbackend.config.security.aop.PermissionSecurity;
import com.example.lmsbackend.constant.LimitValue;
import com.example.lmsbackend.enums.AccountTypeEnum;
import com.example.lmsbackend.enums.ResourceType;
import com.example.lmsbackend.repository.GradeTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GradeRule implements Rule {
    private final GradeTagRepository gradeTagRepository;

    @Override
    public ResourceType getResourceType() {
        return ResourceType.GRADE_TAG;
    }

    @Override
    public boolean authorize(CustomUserDetails currUser, PermissionSecurity permission, long resourceId) {
        if (permission.getIsLimitByTeaching() == LimitValue.LIMIT && AccountTypeEnum.STAFF == currUser.getAccountType()) {
            return gradeTagRepository.isTeachingClassByGradeTag(resourceId, currUser.getId());
        }
        if (permission.getIsLimitByLearn() == LimitValue.LIMIT) {
            return gradeTagRepository.isLearningByGradeTag(resourceId, currUser.getId());
        }
        return true;
    }
}
