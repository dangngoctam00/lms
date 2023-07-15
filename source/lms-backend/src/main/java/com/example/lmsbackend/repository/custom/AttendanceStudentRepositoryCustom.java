package com.example.lmsbackend.repository.custom;

import com.example.lmsbackend.domain.classmodel.AttendanceStudentEntity;

import java.util.List;
import java.util.Set;

public interface AttendanceStudentRepositoryCustom {

    Set<AttendanceStudentEntity> findOfficialAttendanceBySessionIdIn(List<Long> sessionsId);

    Set<AttendanceStudentEntity> findOfficialAttendanceByStudentIdAndSessionIdIn(Long studentId, List<Long> sessionsId);
}
