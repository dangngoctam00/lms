package com.example.lmsbackend.config.security.aop.rule;

import com.example.lmsbackend.config.security.CustomUserDetails;
import com.example.lmsbackend.config.security.aop.PermissionSecurity;
import com.example.lmsbackend.constant.LimitValue;
import com.example.lmsbackend.enums.AccountTypeEnum;
import com.example.lmsbackend.enums.ResourceType;
import com.example.lmsbackend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostRule implements Rule {

    private final PostRepository postRepository;

    @Override
    public ResourceType getResourceType() {
        return ResourceType.POST;
    }

    @Override
    public boolean authorize(CustomUserDetails currUser, PermissionSecurity permission, long resourceId) {
        if (permission.getIsLimitByOwner() == LimitValue.LIMIT) {
            return postRepository.isPostOwnedByUser(resourceId, currUser.getId());
        }
        if (permission.getIsLimitByLearn() == LimitValue.LIMIT && AccountTypeEnum.STUDENT == currUser.getAccountType()) {
            return postRepository.isStudentInClassByPost(currUser.getId(), resourceId);
        }
        if (permission.getIsLimitByTeaching() == LimitValue.LIMIT && AccountTypeEnum.STAFF == currUser.getAccountType()) {
            return postRepository.isTeacherInClassByPost(currUser.getId(), resourceId);
        }
        return true;
    }
}
