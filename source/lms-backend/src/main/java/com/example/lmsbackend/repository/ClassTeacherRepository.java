package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.classmodel.ClassTeacherEntity;
import com.example.lmsbackend.domain.classmodel.ClassTeacherKey;
import com.example.lmsbackend.dto.classes.ClassDto;
import com.example.lmsbackend.enums.ClassStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClassTeacherRepository extends JpaRepository<ClassTeacherEntity, ClassTeacherKey> {

    boolean existsById(ClassTeacherKey id);

    @Query("SELECT CASE WHEN COUNT (c) > 0 THEN true ELSE false END FROM ClassTeacherEntity c " +
            "WHERE c.classEntity.id IN (" +
            "SELECT cla.id FROM ClassEntity cla WHERE cla.course.id = (" +
            "SELECT exam.course.id FROM ExamEntity exam WHERE exam.id = ?1)) " +
            "AND c.teacher.id = ?2")
    Boolean isTeacherInClassByExam(Long examId, Long teacherId);

    @Query("SELECT CASE WHEN COUNT (c) > 0 THEN true ELSE false END FROM ClassTeacherEntity c " +
            "WHERE c.classEntity.id = (" +
            "SELECT cla.classEntity.id FROM ClassSessionEntity cla WHERE cla.id = ?1) " +
            "AND c.teacher.id = ?2")
    Boolean isTeacherInClassByClassSession(Long sessionId, Long teacherId);

    @Query("SELECT CASE WHEN COUNT (c) > 0 THEN true ELSE false END FROM ClassTeacherEntity c " +
            "WHERE c.classEntity.id = ?1 " +
            "AND c.teacher.id = ?2")
    Boolean isTeacherInClass(Long classId, Long teacherId);

    @Query("SELECT new com.example.lmsbackend.dto.classes.ClassDto(c.classEntity.id, c.classEntity.name) FROM ClassTeacherEntity c " +
            "WHERE c.teacher.id = ?1 AND c.classEntity.status = ?2")
    List<ClassDto> findClassByTeacherAndStatus(Long teacherId, ClassStatus status);

    @Query("SELECT c.classEntity.id FROM ClassTeacherEntity c WHERE c.teacher.id = ?1")
    List<Long> findClassIdByTeacher(Long teacherId);

    @Query("SELECT c FROM ClassTeacherEntity c LEFT JOIN FETCH c.teacher WHERE c.classEntity.id = ?1")
    List<ClassTeacherEntity> findTeachers(Long classId);

    @Query("SELECT CASE WHEN COUNT (c) > 0 THEN true ELSE false END FROM ClassTeacherEntity c " +
            "WHERE c.classEntity.id IN (" +
            " SELECT cla.id FROM ClassEntity cla WHERE cla.course.id = ?1) " +
            "AND c.teacher.id = ?2")
    Boolean isTeacherTeachingAnyClassInCourse(Long courseId, Long teacherId);
}
