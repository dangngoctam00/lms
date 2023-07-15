package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.classmodel.AttendanceStudentEntity;
import com.example.lmsbackend.repository.custom.AttendanceStudentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttendanceStudentRepository extends JpaRepository<AttendanceStudentEntity, Long>, AttendanceStudentRepositoryCustom {

    @Query("SELECT a FROM AttendanceStudentEntity a LEFT JOIN FETCH a.attendanceTime WHERE a.attendanceTime.id IN " +
            "(SELECT c.id FROM SessionAttendanceTimeEntity c WHERE c.session.id = ?1)")
    List<AttendanceStudentEntity> findAttendanceStudentBySession(Long sessionId);

    @Query("SELECT a FROM AttendanceStudentEntity a LEFT JOIN FETCH a.attendanceTime WHERE a.attendanceTime.id IN " +
            "(SELECT c.id FROM SessionAttendanceTimeEntity c WHERE c.session.id = ?1) AND a.student.id.studentId = ?2 AND a.student.id.classId = ?3")
    List<AttendanceStudentEntity> findAttendanceStudentBySessionAndStudent(Long sessionId, Long studentId, Long classId);
}
