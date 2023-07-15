package com.example.lmsbackend.service.announcement;

import com.example.lmsbackend.domain.UserEntity;
import com.example.lmsbackend.repository.StaffRepository;
import com.example.lmsbackend.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ClassAnnouncementOperation implements AnnouncementOperation {

    private final StudentRepository studentRepository;
    private final StaffRepository staffRepository;

    @Override
    public List<String> getReceiversEmail(Long id) {
        return Stream.of(studentRepository.findStudentEmailsByClass(id), staffRepository.findTeacherEmailsByClass(id))
                .flatMap(List::stream)
                .collect(toList());
    }

    @Override
    public List<UserEntity> getReceivers(Long id) {
        return Stream.of(studentRepository.findStudentByClass(id), staffRepository.findTeacherByClass(id))
                .flatMap(List::stream)
                .collect(toList());
    }
}
