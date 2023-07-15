package com.example.lmsbackend.service;

import com.example.lmsbackend.domain.RoleEntity;
import com.example.lmsbackend.domain.UserEntity;
import com.example.lmsbackend.domain.classmodel.ClassEntity;
import com.example.lmsbackend.domain.classmodel.QuizClassEntity;
import com.example.lmsbackend.domain.classmodel.VotingEntity;
import com.example.lmsbackend.domain.coursemodel.CourseEntity;
import com.example.lmsbackend.dto.request.breadcrumb.GetBreadcrumbTitleRequest;
import com.example.lmsbackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class BreadcrumbService {
    private final ClassRepository classRepository;
    private final CourseRepository courseRepository;
    private final VotingRepository votingRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UnitClassRepository unitClassRepository;
    private final UnitCourseRepository unitCourseRepository;
    private final QuizCourseRepository quizCourseRepository;
    private final QuizClassRepository quizClassRepository;
    private final ExamRepository examRepository;
    private final TextbookRepository textbookRepository;
    private final ClassSessionRepository classSessionRepository;

    @Transactional
    @Cacheable("breadcrumb")
    public String getBreadcrumbTitle(GetBreadcrumbTitleRequest request) {
        if (request.getType().equals("classes") || request.getType().equals("class")) {
            ClassEntity classEntity = classRepository.getById(request.getId());
            return classEntity.getName();
        }
        if (request.getType().equals("courses")) {
            CourseEntity courseEntity = courseRepository.getById(request.getId());
            return courseEntity.getName();
        }
        if (request.getType().equals("voting")) {
            VotingEntity votingEntity = votingRepository.getById(request.getId());
            return votingEntity.getTitle();
        }
        if (request.getType().equals("roles")) {
            RoleEntity roleEntity = roleRepository.getById(request.getId());
            return roleEntity.getTitle();
        }
        if (request.getType().equals("staffs") || request.getType().equals("students")) {
            UserEntity userEntity = userRepository.getById(request.getId());
            return userEntity.getLastName() +  " " + userEntity.getFirstName();
        }
        if (request.getType().equals("units")) {
            if (StringUtils.equals(request.getScope(), "CLASS")) {
                return unitClassRepository.getNameById(request.getId());
            } else {
                return unitCourseRepository.getNameById(request.getId());
            }
        }
        if (request.getType().equals("quizzes")) {
            if (StringUtils.equals(request.getScope(), "CLASS")) {
                return quizClassRepository.getNameById(request.getId());
            } else {
                return quizCourseRepository.getNameById(request.getId());
            }
        }
        if (request.getType().equals("exams")) {
            return examRepository.getNameById(request.getId());
        }
        if (request.getType().equals("textbooks")) {
            return textbookRepository.getNameById(request.getId());
        }
        if (request.getType().equals("attendances")) {
            var start = classSessionRepository.findById(request.getId()).get().getStartedAt();
            return DateTimeFormatter.ofPattern("dd-MM-yyyy").format(start);
        }
        return String.valueOf(request.getId());
    }
}
