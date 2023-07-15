package com.example.lmsbackend.config.security.aop.rule;

import com.example.lmsbackend.config.security.CustomUserDetails;
import com.example.lmsbackend.config.security.aop.PermissionSecurity;
import com.example.lmsbackend.constant.LimitValue;
import com.example.lmsbackend.enums.AccountTypeEnum;
import com.example.lmsbackend.enums.ResourceType;
import com.example.lmsbackend.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentRule implements Rule {

    private final CommentRepository commentRepository;

    @Override
    public ResourceType getResourceType() {
        return ResourceType.COMMENT;
    }

    @Override
    public boolean authorize(CustomUserDetails currUser, PermissionSecurity permission, long resourceId) {
        if (permission.getIsLimitByOwner() == LimitValue.LIMIT) {
            return commentRepository.isCommentOwnedByUser(resourceId, currUser.getId());
        }
        if (permission.getIsLimitByLearn() == LimitValue.LIMIT && AccountTypeEnum.STUDENT == currUser.getAccountType()) {
            return commentRepository.isStudentInClassByComment(resourceId, currUser.getId());
        }
        if (permission.getIsLimitByTeaching() == LimitValue.LIMIT && AccountTypeEnum.STAFF == currUser.getAccountType()) {
            return commentRepository.isTeacherInClassByComment(resourceId, currUser.getId());
        }
        return true;
    }
}
