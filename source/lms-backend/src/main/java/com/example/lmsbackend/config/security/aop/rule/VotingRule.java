package com.example.lmsbackend.config.security.aop.rule;

import com.example.lmsbackend.config.security.CustomUserDetails;
import com.example.lmsbackend.config.security.aop.PermissionSecurity;
import com.example.lmsbackend.constant.LimitValue;
import com.example.lmsbackend.enums.AccountTypeEnum;
import com.example.lmsbackend.enums.ActionType;
import com.example.lmsbackend.enums.ResourceType;
import com.example.lmsbackend.repository.ChapterActivityClassRepository;
import com.example.lmsbackend.repository.ClassStudentRepository;
import com.example.lmsbackend.repository.ClassTeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VotingRule implements Rule {

    private final ClassTeacherRepository classTeacherRepository;
    private final ClassStudentRepository classStudentRepository;
    private final ChapterActivityClassRepository chapterActivityClassRepository;

    @Override
    public ResourceType getResourceType() {
        return ResourceType.VOTING;
    }

    @Override
    public boolean authorize(CustomUserDetails currUser, PermissionSecurity permission, long resourceId) {
        var classId = chapterActivityClassRepository.findClassIdByActivity(resourceId, ActionType.VOTING.name());
        if (permission.getIsLimitByLearn() == LimitValue.LIMIT && AccountTypeEnum.STUDENT == currUser.getAccountType()) {
            return classStudentRepository.isStudentInClass(currUser.getId(), classId);
        }
        if (permission.getIsLimitByTeaching() == LimitValue.LIMIT && AccountTypeEnum.STAFF == currUser.getAccountType()) {
            return classTeacherRepository.isTeacherInClass(classId, currUser.getId());
        }
        return true;
    }
}
