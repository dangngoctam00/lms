package com.example.lmsbackend.config.security.aop.rule;

import com.example.lmsbackend.config.security.CustomUserDetails;
import com.example.lmsbackend.config.security.aop.PermissionSecurity;
import com.example.lmsbackend.constant.LimitValue;
import com.example.lmsbackend.enums.AccountTypeEnum;
import com.example.lmsbackend.enums.ResourceType;
import com.example.lmsbackend.repository.ClassStudentRepository;
import com.example.lmsbackend.repository.ClassTeacherRepository;
import com.example.lmsbackend.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseRule implements Rule {

    private final CourseRepository courseRepository;
    private final ClassTeacherRepository classTeacherRepository;

    private final ClassStudentRepository classStudentRepository;

    @Override
    public ResourceType getResourceType() {
        return ResourceType.COURSE;
    }

    @Override
    public boolean authorize(CustomUserDetails currUser, PermissionSecurity permission, long courseId) {
        if (permission.getIsLimitByOwner() == LimitValue.LIMIT) {
            return courseRepository.isCreatedByUser(courseId, currUser.getUsername());
        }
        if (permission.getIsLimitByTeaching() == LimitValue.LIMIT && currUser.getAccountType() == AccountTypeEnum.STAFF) {
            return classTeacherRepository.isTeacherTeachingAnyClassInCourse(courseId, currUser.getId());
        }
        if (permission.getIsLimitByLearn() == LimitValue.LIMIT && currUser.getAccountType() == AccountTypeEnum.STUDENT){
            return classStudentRepository.isStudentInAnyClassOfCourse(courseId, currUser.getId());
        }
        return true;
    }
}
