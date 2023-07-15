package com.example.lmsbackend.config.security.aop.rule;

import com.example.lmsbackend.config.security.CustomUserDetails;
import com.example.lmsbackend.config.security.aop.PermissionSecurity;
import com.example.lmsbackend.constant.LimitValue;
import com.example.lmsbackend.domain.BranchEntity;
import com.example.lmsbackend.domain.StudentEntity;
import com.example.lmsbackend.domain.UserEntity;
import com.example.lmsbackend.enums.ResourceType;
import com.example.lmsbackend.repository.ClassStudentRepository;
import com.example.lmsbackend.repository.StudentRepository;
import com.example.lmsbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StudentRule implements Rule {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    private final ClassStudentRepository classStudentRepository;

    @Override
    public ResourceType getResourceType() {
        return ResourceType.STUDENT;
    }

    @Override
    public boolean authorize(CustomUserDetails currUser, PermissionSecurity permission, long studentID) {
        if (permission.getIsLimitByBranch() == LimitValue.LIMIT && !checkLimitByBranch(currUser, studentID)) {
            return false;
        }
        if (permission.getIsLimitByTeaching() == LimitValue.LIMIT) {
            return classStudentRepository.isStudentLearningTeacher(studentID, currUser.getId());
        }
        return true;
    }

    boolean checkLimitByBranch(CustomUserDetails currUser, long studentID) {
        Optional<StudentEntity> studentEntityOptional = studentRepository.findById(studentID);
        UserEntity userEntity = userRepository.getById(currUser.getId());
        if (studentEntityOptional.isEmpty()) return false;
        List<Long> branchIDs = studentEntityOptional.get().getBranches()
                .stream().map(BranchEntity::getId)
                .collect(Collectors.toList());
        for (BranchEntity branchEntity : userEntity.getBranches()) {
            if (branchIDs.contains(branchEntity.getId())) return true;
        }
        return false;
    }

}
