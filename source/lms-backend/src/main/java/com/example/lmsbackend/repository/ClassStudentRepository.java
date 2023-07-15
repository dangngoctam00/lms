package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.classmodel.ClassStudentEntity;
import com.example.lmsbackend.domain.classmodel.ClassStudentKey;
import com.example.lmsbackend.dto.classes.ClassDto;
import com.example.lmsbackend.enums.ClassStatus;
import com.example.lmsbackend.repository.custom.ClassStudentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClassStudentRepository extends JpaRepository<ClassStudentEntity, ClassStudentKey>,
        QuerydslPredicateExecutor<ClassStudentEntity>, ClassStudentRepositoryCustom {

    boolean existsById(ClassStudentKey id);

    @Query("SELECT c FROM ClassStudentEntity c WHERE c.id.classId = ?1")
    List<ClassStudentEntity> getStudentsByClassId(Long classId);

    @Query("SELECT c.student.id FROM ClassStudentEntity c WHERE c.id.classId = ?1")
    List<Long> getStudentsIdByClassId(Long classId);

    @Query("SELECT CASE WHEN COUNT (c) > 0 THEN true ELSE false END FROM ClassStudentEntity c WHERE c.classEntity.id = ?2 " +
            "AND c.student.id = ?1")
    Boolean isStudentInClass(Long studentId, Long classId);

    @Query("SELECT CASE WHEN COUNT (c) > 0 THEN true ELSE false END FROM ClassStudentEntity c WHERE c.classEntity.id IN " +
            "(SELECT ce.id FROM ClassEntity ce WHERE ce.course.id = ?1) AND c.student.id = ?2")
    boolean isStudentInAnyClassOfCourse(Long courseId, Long studentId);

    @Query("SELECT new com.example.lmsbackend.dto.classes.ClassDto(c.classEntity.id, c.classEntity.name) FROM ClassStudentEntity c " +
            "WHERE c.student.id = ?1 AND c.classEntity.status = ?2")
    List<ClassDto> findClassByStudentAndStatus(Long studentId, ClassStatus status);

    @Query("SELECT c.classEntity.id FROM ClassStudentEntity c WHERE c.student.id = ?1")
    List<Long> findClassIdByStudent(Long studentId);

    @Query("SELECT case WHEN count(e) > 0 then true else false end FROM ClassStudentEntity e " +
            "where e.student.id = :studentID and e.classEntity.id in (" +
            "select classTeacher.classEntity.id from ClassTeacherEntity classTeacher where classTeacher.teacher.id = :teacherID" +
            ")")
    boolean isStudentLearningTeacher(@Param("studentID") long studentID, @Param("teacherID") Long teacherID);
}
